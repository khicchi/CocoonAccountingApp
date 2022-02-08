package com.cocoon.controller;

import com.cocoon.dto.InvoiceDTO;
import com.cocoon.service.InvoiceService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/sales-invoice")
public class InvoiceController {

    InvoiceService invoiceService;

    public InvoiceController(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    @GetMapping("/list")
    public String invoiceCreate(Model model){

            model.addAttribute("invoices", invoiceService.getAllInvoices());

        return "/invoice/sales-invoice-list";
    }


}
