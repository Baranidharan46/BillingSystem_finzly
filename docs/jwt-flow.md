# JWT Authentication Flow

How authentication works in the Billing System, end to end. Two parts:
token issuance (login) and token verification (every protected request).

Relevant classes live in `org.example.billingsystem.security`:
`SecurityConfig`, `JwtFilter`, `JwtService`, `MyUserDetailService`, `UserPrincipal`,
`JwtAuthenticationEntryPoint`, `JwtAccessDeniedHandler`.

---

## Part A — Login: minting the token

```
                      POST /v1/customer/login  { username, password }
                                    │
                                    ▼
                      ┌─────────────────────────────┐
                      │   CustomerController         │
                      │   .loginCustomer()           │
                      └──────────────┬──────────────┘
                                     │ new UsernamePasswordAuthenticationToken
                                     ▼
                      ┌─────────────────────────────┐
                      │   AuthenticationManager      │  (SecurityConfig bean)
                      └──────────────┬──────────────┘
                                     ▼
                      ┌─────────────────────────────┐
                      │   DaoAuthenticationProvider  │
                      │   + BCryptPasswordEncoder    │
                      └──────────────┬──────────────┘
                                     ▼
                      ┌─────────────────────────────┐
                      │   MyUserDetailService        │
                      │   .loadUserByUsername()      │──► CustomerRepository
                      │   returns UserPrincipal      │     .findByUsername()
                      └──────────────┬──────────────┘
                          password matches?
                      ┌──────────────┴───────────────┐
                   NO │                               │ YES
                      ▼                               ▼
             BadCredentialsException        JwtService.generateToken(username)
             (no handler → ugly 401)        HS256 sign, 30-min expiry
                                                     │
                                                     ▼
                                        returns raw JWT string to client
```

---

## Part B — Protected request: verifying the token

```
    GET /v1/invoice/1     Header:  Authorization: Bearer <JWT>
              │
              ▼
┌───────────────────────────────────────────────────────────────┐
│  FILTER CHAIN  (order matters)                                  │
│                                                                 │
│   1) RequestLoggingFilter   (@Order HIGHEST_PRECEDENCE)         │
│        puts requestId on MDC, logs "--> GET /v1/invoice/1"      │
│                     │                                           │
│                     ▼                                           │
│   2) JwtFilter  (addFilterBefore UsernamePasswordAuthFilter)    │
│        ┌──────────────────────────────────────────────┐        │
│        │ header starts with "Bearer "?                 │        │
│        │     └─ token = substring(7)                   │        │
│        │ JwtService.extractUserName(token)             │        │
│        │     └─ throws? → username=null (swallowed)    │        │
│        │ username != null AND context not yet set?     │        │
│        │     └─ MyUserDetailService.loadUserByUsername │        │
│        │     └─ JwtService.validateToken(token, user)  │        │
│        │           (subject match && not expired)      │        │
│        │     └─ valid? set Authentication in           │        │
│        │              SecurityContextHolder            │        │
│        └──────────────────────────────────────────────┘        │
│                     │                                           │
│                     ▼                                           │
│   3) UsernamePasswordAuthenticationFilter (passes through)      │
│                     │                                           │
│                     ▼                                           │
│   4) Authorization rules (SecurityConfig)                       │
│        /v1/customer POST + /login = permitAll                   │
│        anyRequest() = authenticated                             │
└───────────────────────────────┬───────────────────────────────┘
                                 │
         ┌───────────────────────┼───────────────────────┐
SecurityContext empty?   authenticated but        authenticated + allowed
(no/invalid token)       not permitted                    │
         │                       │                         ▼
         ▼                       ▼               ┌──────────────────┐
JwtAuthenticationEntryPoint  JwtAccessDeniedHandler│  Controller     │
→ 401 {"error":              → 403 {"error":       │  → Service      │
   "Unauthorized..."}           "Forbidden..."}    │  → Repository   │
                                                   └────────┬────────┘
                                                            │
                                    app throws (e.g. InvoiceNotFound)?
                                                            ▼
                                            GlobalExceptionHandler
                                            (@ControllerAdvice) → 404 / 400
                                                            │
                                                            ▼
                          RequestLoggingFilter finally{} logs "<-- ... 200 (12 ms)"
```

---

## Three things to stress

1. **Two separate entry points for auth failures.** `JwtAuthenticationEntryPoint`
   returns 401 (no / invalid token); `JwtAccessDeniedHandler` returns 403 (valid
   token but not permitted). Both are distinct from `GlobalExceptionHandler`, which
   only catches *application* exceptions (404 / 400) *after* security passes.

2. **Stateless.** `SessionCreationPolicy.STATELESS` — the `SecurityContext` is
   rebuilt from the token on every request; nothing is stored server-side between
   calls.

3. **Filter order.** `RequestLoggingFilter` wraps everything (timing + requestId
   cover the whole request); `JwtFilter` runs before the username/password filter
   slot via `addFilterBefore(...)`.

## Token mechanics (JwtService)

- Algorithm: **HS256**, key Base64-decoded from `jwt.secret`.
- Expiry: **30 minutes** (`System.currentTimeMillis() + 1000*60*30`).
- Claim extraction via a generic `Function<Claims, T>` resolver.
- `extractUserName` exceptions are swallowed in `JwtFilter` (→ clean 401);
  `validateToken` separately catches `JwtException` / `IllegalArgumentException`.
