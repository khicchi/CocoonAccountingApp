package com.cocoon.controller;

import com.cocoon.dto.InvoiceDTO;
import com.cocoon.service.CompanyService;
import com.cocoon.service.InvoiceService;
import com.cocoon.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/dashboard")
public class DashboardController {

     private  final CompanyService companyService;
    private final InvoiceService invoiceService;

    public DashboardController(CompanyService companyService, InvoiceService invoiceService) {
        this.companyService = companyService;
        this.invoiceService = invoiceService;
    }


    @GetMapping()
    public String invoiceTopThreeList(Model model){

        List<InvoiceDTO> invoices = invoiceService.getAllInvoicesSorted();
        List<InvoiceDTO> updatedInvoices = invoices.stream().map(invoiceService::calculateInvoiceCost).collect(Collectors.toList());
        model.addAttribute("invoices", updatedInvoices);
        model.addAttribute("result", invoiceService.calculateTotalProfitLoss());
        return "dashboard";
    }

    @ModelAttribute("company")
    public String getCompanyName() {
        return companyService.getCompanyByLoggedInUser().getTitle();
    }

}