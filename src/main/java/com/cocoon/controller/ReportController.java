package com.cocoon.controller;

import com.cocoon.dto.InvoiceDTO;
import com.cocoon.dto.InvoiceProductDTO;
import com.cocoon.dto.ProductDTO;
import com.cocoon.entity.InvoiceProduct;
import com.cocoon.enums.ProductStatus;
import com.cocoon.enums.Unit;
import com.cocoon.repository.ClientVendorRepo;
import com.cocoon.repository.InvoiceProductRepo;
import com.cocoon.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.persistence.Column;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/report")
public class ReportController {


    private ProductService productService;
    private InvoiceService invoiceService;
    private InvoiceProductService invoiceProductService;

    private InvoiceDTO currentInvoiceDTO = new InvoiceDTO();

    @Autowired
    private InvoiceProductRepo invoiceProductRepo;

    private boolean active = true;

    public ReportController(InvoiceService invoiceService, ProductService productService, InvoiceProductService invoiceProductService) {
        this.invoiceService = invoiceService;
        this.productService = productService;
        this.invoiceProductService = invoiceProductService;
    }

    @GetMapping("/stock")
    public String getStock(Model model){

        ArrayList<InvoiceProduct> products = (ArrayList<InvoiceProduct>) invoiceProductRepo.getStockReportList();

        model.addAttribute("stock", new ArrayList<InvoiceProductDTO>());


//        currentInvoiceDTO = new InvoiceDTO();
//        currentInvoiceDTO.getInvoiceDate();
//        model.addAttribute("invoice", currentInvoiceDTO);
//        model.addAttribute("product", new ProductDTO());
//        model.addAttribute("products", productService.getAllProducts());
//        model.addAttribute("invoiceType", currentInvoiceDTO.getInvoiceType());
//        model.addAttribute("price", currentInvoiceDTO.getTotalCost());


        return "report/stock-report.html";
    }
}
