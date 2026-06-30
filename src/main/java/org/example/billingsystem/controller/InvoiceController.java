package org.example.billingsystem.controller;
import jakarta.validation.Valid;
import org.example.billingsystem.dtoObject.InvoiceRequestDTO;
import org.example.billingsystem.dtoObject.InvoiceResponseDTO;
import org.example.billingsystem.service.InvoiceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/invoice")
public class InvoiceController {

    private final InvoiceService invoiceService;

    public InvoiceController(InvoiceService invoiceService){
        this.invoiceService=invoiceService;
    }

    @PostMapping
    public ResponseEntity<InvoiceResponseDTO> generateInvoice(@Valid @RequestBody InvoiceRequestDTO dto, @RequestParam boolean isOnlinePayment, @RequestParam boolean isBeforeDueDate) {
        InvoiceResponseDTO responseDTO=invoiceService.generateInvoice(dto,isBeforeDueDate,isOnlinePayment);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }
    @GetMapping("/{customerId}")
    public ResponseEntity <InvoiceResponseDTO> getInvoiceById(@Valid @PathVariable Long customerId){
        InvoiceResponseDTO id=invoiceService.getInvoiceByCustomerId(customerId);
        return ResponseEntity.status(HttpStatus.OK).body(id);
    }
}
