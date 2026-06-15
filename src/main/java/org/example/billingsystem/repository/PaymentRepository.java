package org.example.billingsystem.repository;

import org.example.billingsystem.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment,Long> {
    Optional<Payment> findByCustomerId(Long customerId);
}
