package com.cocoon.controller;

import com.cocoon.dto.InvoiceDTO;
import com.cocoon.dto.ProductDTO;
import com.cocoon.service.ClientVendorService;
import com.cocoon.service.InvoiceService;
import com.cocoon.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/sales-invoice")
public class InvoiceController {

    private List<ProductDTO> productsPerInvoice = new ArrayList<>();

    private InvoiceService invoiceService;
    private ProductService productService;
    private ClientVendorService clientVendorService;

    public InvoiceController(InvoiceService invoiceService, ProductService productService, ClientVendorService clientVendorService) {
        this.invoiceService = invoiceService;
        this.productService = productService;
        this.clientVendorService = clientVendorService;
    }

    @GetMapping("/list")
    public String invoiceList(Model model){

            List<InvoiceDTO> invoices = invoiceService.getAllInvoices();
            model.addAttribute("invoices", invoices);

            for (InvoiceDTO invoice : invoices){
                List<ProductDTO> products = productService.getProductsByInvoiceId(invoice.getId());
                int costWithoutTax = products.stream().mapToInt(ProductDTO::getPrice).sum();
                invoice.setInvoiceCostWithoutTax(costWithoutTax);
                int costWithTax = calculateTaxedCost(products);
                invoice.setTotalCost(costWithTax);
                invoice.setInvoiceCostWithTax(costWithTax - costWithoutTax);
            }

        return "invoice/sales-invoice-list";
    }

    private int calculateTaxedCost(List<ProductDTO> products){
        int result = 0;
        for (ProductDTO product : products){
            result += product.getPrice() + (product.getPrice() * product.getTax() * 0.01);// TODO - ayık kafayla hesaba bakcaz...
        }
        return result;
    }

    @GetMapping("/create")
    public String invoiceCreatePost(Model model){

        model.addAttribute("invoice", new InvoiceDTO());
        model.addAttribute("product", new ProductDTO());
        model.addAttribute("products", productService.getAllProducts());
        model.addAttribute("clients", clientVendorService.getAllClientsVendors());
        model.addAttribute("invoiceProducts", productsPerInvoice);

        return "invoice/sales-invoice-create";
    }

    @PostMapping("/create-product")
    public String productCreateForInvoice(Model model, ProductDTO productDTO){

        productsPerInvoice.add(productDTO);
        model.addAttribute("invoiceProducts", productsPerInvoice);

        return "redirect:/sales-invoice/create";
    }


}
