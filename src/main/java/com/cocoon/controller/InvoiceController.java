package com.cocoon.controller;

import com.cocoon.dto.InvoiceDTO;
import com.cocoon.dto.ProductDTO;
import com.cocoon.entity.Invoice;
import com.cocoon.service.ClientVendorService;
import com.cocoon.service.InvoiceService;
import com.cocoon.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/sales-invoice")
public class InvoiceController {

    private List<ProductDTO> productsPerInvoice = new ArrayList<>();

    private final InvoiceService invoiceService;
    private final ProductService productService;
    private final ClientVendorService clientVendorService;

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
        // TODO - invoice number kaybolmayacak...

        return "redirect:/sales-invoice/create";
    }

    @PostMapping("/create")
    public String createInvoice(Model model, InvoiceDTO invoiceDTO){

        invoiceDTO.setProducts(productsPerInvoice);
        invoiceService.save(invoiceDTO);
        productsPerInvoice.clear();

        return "redirect:/sales-invoice/list";
    }

    @GetMapping("/update/{id}")
    public String updateInvoice(@PathVariable("id") Long id, Model model){

        InvoiceDTO invoiceDTO = invoiceService.getInvoiceById(id);
        model.addAttribute("invoice", invoiceDTO);
        model.addAttribute("product", new ProductDTO());
        model.addAttribute("products", productService.getAllProducts());
        model.addAttribute("clients", clientVendorService.getAllClientsVendors());
        productsPerInvoice = productService.getProductsByInvoiceId(invoiceDTO.getId());
        model.addAttribute("invoiceProducts", productsPerInvoice);

        return "invoice/sales-invoice-update";
    }

    @PostMapping("/update/{id}")
    public String updateInvoice(@PathVariable("id") Long id, InvoiceDTO invoiceDTO){

        InvoiceDTO invoice = invoiceService.getInvoiceById(id);
        invoice.setProducts(invoiceDTO.getProducts());
        invoiceService.save(invoice);

        return "redirect:/sales-invoice/list";

    }

}
