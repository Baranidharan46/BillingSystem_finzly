package org.example.billingsystem.mapper;

import org.example.billingsystem.dtoObject.PaymentRequestDTO;
import org.example.billingsystem.dtoObject.PaymentResponseDTO;
import org.example.billingsystem.model.Payment;
import org.example.billingsystem.status.PaymentMethod;

public class PaymentMapper {

    public static Payment toEntity(PaymentRequestDTO dto){
        Payment payment=new Payment();
        payment.setCustomerId(payment.getCustomerId());
        payment.setFinalAmount(payment.getFinalAmount());
        payment.setPaymentMethod(payment.getPaymentMethod());
        return payment;
    }

    public static PaymentResponseDTO toResponse(Payment payment) {
        PaymentResponseDTO paymentResponse=new PaymentResponseDTO();
        paymentResponse.setCustomerId(payment.getCustomerId());
        paymentResponse.setName(payment.getName());
        paymentResponse.setUnitsConsumed(payment.getUnitsConsumed());
        paymentResponse.setPaymentMethod(payment.getPaymentMethod());
        paymentResponse.setPaymentDate(payment.getPaymentDate());
        paymentResponse.setPaymentStatus(payment.getPaymentStatus());
        return paymentResponse;
    }
}
