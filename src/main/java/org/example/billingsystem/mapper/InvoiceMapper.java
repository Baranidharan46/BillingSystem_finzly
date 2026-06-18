package org.example.billingsystem.mapper;

import org.example.billingsystem.dtoObject.InvoiceRequestDTO;
import org.example.billingsystem.dtoObject.InvoiceResponseDTO;
import org.example.billingsystem.model.Invoice;
import org.example.billingsystem.status.PaymentMethod;
import org.example.billingsystem.status.PaymentStatus;

public class InvoiceMapper {
    public static Invoice toEntity(InvoiceRequestDTO dto, String getCustomerName, Double baseAmount, Double discount, Double finalAmount, PaymentStatus paymentStatus){
        Invoice invoice=new Invoice();
        invoice.setCustomerId(dto.getCustomerId());
        invoice.setName(getCustomerName);
        invoice.setBaseAmount(baseAmount);
        invoice.setDiscount(discount);
        invoice.setFinalAmount(finalAmount);
        invoice.setPaymentStatus(paymentStatus);
        invoice.setUnitsConsumed(dto.getUnitsConsumed());
        invoice.setDueDate(dto.getDueDate());
        return invoice;
    }

    public static InvoiceResponseDTO toResponseDTO(Invoice invoice){
        InvoiceResponseDTO responseDTO=new InvoiceResponseDTO();
        responseDTO.setInvoiceId(invoice.getInvoiceId());
        responseDTO.setCustomerId(invoice.getCustomerId());
        responseDTO.setName(invoice.getName());
        responseDTO.setBaseAmount(invoice.getBaseAmount());
        responseDTO.setFinalAmount(invoice.getFinalAmount());
        responseDTO.setDiscount(invoice.getDiscount());
        responseDTO.setUnitsConsumed(invoice.getUnitsConsumed());
        responseDTO.setPaymentStatus(invoice.getPaymentStatus());
        responseDTO.setDueDate(invoice.getDueDate());
        return responseDTO;
    }

}
