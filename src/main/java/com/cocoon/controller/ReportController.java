package com.cocoon.controller;

import com.cocoon.entity.InvoiceProduct;
import com.cocoon.repository.InvoiceProductRepo;
import com.cocoon.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;

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

    @ModelAttribute("company")
    public String getCompanyName() {
        return companyService.getCompanyByLoggedInUser().getTitle();
    }
}
