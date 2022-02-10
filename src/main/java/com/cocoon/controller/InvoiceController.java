package com.cocoon.controller;

import com.cocoon.dto.InvoiceDTO;
import com.cocoon.dto.ProductDTO;
import com.cocoon.entity.Invoice;
import com.cocoon.entity.Product;
import com.cocoon.exception.CocoonException;
import com.cocoon.service.ClientVendorService;
import com.cocoon.service.InvoiceService;
import com.cocoon.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.*;

@Controller
@RequestMapping("/sales-invoice")
public class InvoiceController {

    private Set<ProductDTO> productsPerInvoice = new HashSet<>();

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
                Set<ProductDTO> products = productService.getProductsByInvoiceId(invoice.getId());
                int costWithoutTax = products.stream().mapToInt(ProductDTO::getPrice).sum();
                invoice.setInvoiceCostWithoutTax(costWithoutTax);
                int costWithTax = calculateTaxedCost(products);
                invoice.setTotalCost(costWithTax);
                invoice.setInvoiceCostWithTax(costWithTax - costWithoutTax);
            }

        return "invoice/sales-invoice-list";
    }

    private int calculateTaxedCost(Set<ProductDTO> products){
        int result = 0;
        for (ProductDTO product : products){
            result += product.getPrice() + (product.getPrice() * product.getTax() * 0.01);
        }
        return result;
    }

    @GetMapping("/create")
    public String invoiceCreateGet(Model model){

        InvoiceDTO invoiceDTO = new InvoiceDTO(invoiceService.getInvoiceNumber(),LocalDate.now());
        model.addAttribute("invoice", invoiceService.save(invoiceDTO));
        model.addAttribute("product", new ProductDTO());
        model.addAttribute("products", productService.getAllProducts());
        model.addAttribute("clients", clientVendorService.getAllClientsVendors());
        productsPerInvoice.clear();
        model.addAttribute("invoiceProducts", productsPerInvoice);

        return "invoice/sales-invoice-create";
    }

    @GetMapping("/addition/{id}")
    public String invoiceCreateMore(@PathVariable("id") Long id, Model model){

        InvoiceDTO invoiceDTO = invoiceService.getInvoiceById(id);
        model.addAttribute("invoice", invoiceDTO);
        model.addAttribute("product", new ProductDTO());
        model.addAttribute("products", productService.getAllProducts());
        model.addAttribute("clients", clientVendorService.getAllClientsVendors());
        model.addAttribute("invoiceProducts", productsPerInvoice);

        return "invoice/sales-invoice-create";
    }

    @PostMapping("/create-product/{id}")
    public String productCreateForInvoice(@PathVariable("id") Long id, Model model, ProductDTO productDTO) throws CocoonException {

        InvoiceDTO invoice = invoiceService.getInvoiceById(id);
        Set<InvoiceDTO> set = new HashSet<>();
        set.add(invoice);
        ProductDTO dto = productService.getProductById(productDTO.getId());
        dto.setInvoices(set);
        productsPerInvoice.add(dto);
        model.addAttribute("invoiceProducts", productsPerInvoice);

        return "redirect:/sales-invoice/addition/" + id;
    }

    @PostMapping("/create/{id}")
    public String createInvoice(@PathVariable("id") Long id, InvoiceDTO dto){

        dto.setProducts(productsPerInvoice);
        invoiceService.update(dto,id);
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

    @GetMapping("/addition-update/{id}")
    public String invoiceUpdateMore(@PathVariable("id") Long id, Model model){

        InvoiceDTO invoiceDTO = invoiceService.getInvoiceById(id);
        model.addAttribute("invoice", invoiceDTO);
        model.addAttribute("product", new ProductDTO());
        model.addAttribute("products", productService.getAllProducts());
        model.addAttribute("clients", clientVendorService.getAllClientsVendors());
        model.addAttribute("invoiceProducts", productsPerInvoice);

        return "invoice/sales-invoice-update";
    }

    @PostMapping("/create-product-update/{id}")
    public String updateProductForInvoice(@PathVariable("id") Long id, Model model, ProductDTO productDTO) throws CocoonException {

        ProductDTO dto = productService.getProductById(productDTO.getId());
        productsPerInvoice.add(dto);
        model.addAttribute("invoiceProducts", productsPerInvoice);

        return "redirect:/sales-invoice/addition-update/"+id;
    }

    @PostMapping("/update/{id}")
    public String updateInvoice(@PathVariable("id") Long id, InvoiceDTO invoiceDTO){

        invoiceService.update(invoiceDTO, id);

        return "redirect:/sales-invoice/list";

    }

}
