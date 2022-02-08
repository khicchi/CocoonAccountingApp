package com.cocoon.controller;

import com.cocoon.dto.InvoiceDTO;
import com.cocoon.dto.ProductDTO;
import com.cocoon.service.InvoiceService;
import com.cocoon.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.OptionalDouble;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/sales-invoice")
public class InvoiceController {

    InvoiceService invoiceService;
    ProductService productService;

    public InvoiceController(InvoiceService invoiceService, ProductService productService) {
        this.invoiceService = invoiceService;
        this.productService = productService;
    }

    @GetMapping("/list")
    public String invoiceList(Model model){

            List<InvoiceDTO> invoices = invoiceService.getAllInvoices();
            model.addAttribute("invoices", invoices);

            for (InvoiceDTO invoice : invoices){
                List<ProductDTO> products = productService.getProductsByInvoiceId(invoice.getId());
                invoice.setInvoiceCostWithoutTax(products.stream().mapToInt(ProductDTO::getPrice).sum());

                List<Integer> taxes = products.stream().map(ProductDTO::getTax).collect(Collectors.toList());

            }

            double tax = productService.getProductsByInvoiceId(2L).stream()
                    .mapToInt(ProductDTO::getTax)
                    .average().getAsDouble();
            model.addAttribute("tax", tax);

        return "/invoice/sales-invoice-list";
    }


}
