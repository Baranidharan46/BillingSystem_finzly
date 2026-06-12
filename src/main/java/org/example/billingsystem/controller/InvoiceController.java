package org.example.billingsystem.controller;

import org.example.billingsystem.model.Customer;
import org.example.billingsystem.model.Invoice;
import org.example.billingsystem.service.InvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/invoice")
public class InvoiceController {
    @Autowired
    InvoiceService invoiceService;

    @PostMapping("/generateInvoice/{customerId}")
    public ResponseEntity<Invoice> generateInvoice(@PathVariable String customerId, @RequestParam boolean isOnlinePayment,@RequestParam boolean isBeforeDueDate) {
        Invoice invoice=invoiceService.generateInvoice(customerId,isOnlinePayment,isBeforeDueDate);
        return ResponseEntity.status(HttpStatus.CREATED).body(invoice);
    }
    @GetMapping("/getCustomerInvoiceById/{customerId}")
    public ResponseEntity <Invoice> getInvoiceById(@PathVariable String customerId){
        Invoice id=invoiceService.getInvoiceByCustomerId(customerId);
        return ResponseEntity.status(HttpStatus.OK).body(id);
    }
}
