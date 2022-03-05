package com.cocoon.controller;

import com.cocoon.entity.InvoiceProduct;
import com.cocoon.repository.InvoiceProductRepo;
import com.cocoon.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;

@Controller
@RequestMapping("/report")
public class ReportController {


    private ProductService productService;
    private InvoiceService invoiceService;
    private InvoiceProductService invoiceProductService;
    private InvoiceProductRepo invoiceProductRepo;
    private final CompanyService companyService;

    public ReportController(InvoiceService invoiceService, ProductService productService,
                            InvoiceProductService invoiceProductService, InvoiceProductRepo invoiceProductRepo, CompanyService companyService) {
        this.invoiceService = invoiceService;
        this.productService = productService;
        this.invoiceProductService = invoiceProductService;
        this.invoiceProductRepo = invoiceProductRepo;
        this.companyService = companyService;
    }

    @GetMapping("/stock")
    public String getStock(Model model){

        ArrayList<InvoiceProduct> stock = (ArrayList<InvoiceProduct>) invoiceProductRepo.getStockReportListProducts();
//        ArrayList<InvoiceProductDTO> stock = invoiceProductService.getStockReportList();

        model.addAttribute("stock", stock);

        return "report/stock-report.html";
    }

    @ModelAttribute
    public void addAttributes(Model model) {
        model.addAttribute("date", new Date());
        model.addAttribute("localDateTime", LocalDateTime.now());
        model.addAttribute("localDate", LocalDate.now());
        model.addAttribute("java8Instant", Instant.now());
    }
}
