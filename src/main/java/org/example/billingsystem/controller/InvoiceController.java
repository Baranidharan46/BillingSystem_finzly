package org.example.billingsystem.controller;
import org.example.billingsystem.dtoObject.InvoiceRequestDTO;
import org.example.billingsystem.dtoObject.InvoiceResponseDTO;
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

    @PostMapping
    public ResponseEntity<InvoiceResponseDTO> generateInvoice(@RequestBody InvoiceRequestDTO dto, @RequestParam boolean isOnlinePayment, @RequestParam boolean isBeforeDueDate) {
        InvoiceResponseDTO responseDTO=invoiceService.generateInvoice(dto,isBeforeDueDate,isOnlinePayment);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }
    @GetMapping("/{customerId}")
    public ResponseEntity <InvoiceResponseDTO> getInvoiceById(@PathVariable Long customerId){
        InvoiceResponseDTO id=invoiceService.getInvoiceByCustomerId(customerId);
        return ResponseEntity.status(HttpStatus.OK).body(id);
    }
}
