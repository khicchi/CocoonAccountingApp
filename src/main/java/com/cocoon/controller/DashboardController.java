package com.cocoon.controller;

import com.cocoon.dto.InvoiceDTO;
import com.cocoon.dto.ProductDTO;
import com.cocoon.service.ClientVendorService;
import com.cocoon.service.InvoiceService;
import com.cocoon.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/dashboard")
public class DashboardController {


    private final InvoiceService invoiceService;

    public DashboardController(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }


//    @GetMapping("/invoice")
//    public String invoiceTopThreeList(Model model){
//
//        List<InvoiceDTO> invoices = invoiceService.getAllInvoicesSorted();
//        model.addAttribute("invoices", invoices);
//
//        return "dashboard";
//    }

}
