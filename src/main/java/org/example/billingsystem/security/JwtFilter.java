package org.example.billingsystem.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
@Service
public class JwtFilter extends OncePerRequestFilter {

    JwtService jwtService;
    ApplicationContext context;
    public JwtFilter(JwtService jwtService,ApplicationContext context){
        this.jwtService=jwtService;
        this.context=context;
    }
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String authHeader=request.getHeader("Authorization");
        String token=null;
        String userName=null;

        if(authHeader != null && authHeader.startsWith("Bearer ")){
            token = authHeader.substring(7); // removes "Bearer " (7 chars)
            try {
                userName = jwtService.extractUserName(token);
            } catch (Exception e) {
                // Malformed/expired/invalid token: leave unauthenticated and let
                // the security entry point return a clean 401 for protected routes.
                userName = null;
            }
        }

        if(userName != null && SecurityContextHolder.getContext().getAuthentication() == null){
            UserDetails userDetails = context.getBean(MyUserDetailService.class)
                    .loadUserByUsername(userName);

            if(jwtService.validateToken(token, userDetails)){
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        filterChain.doFilter(request, response);

    }
}
