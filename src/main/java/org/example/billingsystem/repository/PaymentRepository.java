package org.example.billingsystem.repository;

import org.example.billingsystem.model.Payment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment,Long> {
    Optional<Payment> findFirstByCustomerIdOrderByPaymentDateDesc(Long customerId);
    Page<Payment> findByCustomerIdOrderByPaymentDateDesc(Long customerId, Pageable pageable);
}
