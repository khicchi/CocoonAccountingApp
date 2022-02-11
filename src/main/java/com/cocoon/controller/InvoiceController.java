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

    private InvoiceDTO currentInvoice = new InvoiceDTO();

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

        currentInvoice.setInvoiceNo(invoiceService.getInvoiceNumber());
        currentInvoice.setInvoiceDate(LocalDate.now());
        model.addAttribute("invoice", currentInvoice);
        model.addAttribute("product", new ProductDTO());
        model.addAttribute("products", productService.getAllProducts());
        model.addAttribute("clients", clientVendorService.getAllClientsVendors());
        model.addAttribute("invoiceProducts", currentInvoice.getProducts());

        return "invoice/sales-invoice-create";
    }

    @GetMapping("/addition")
    public String invoiceCreateMore(Model model){

        model.addAttribute("invoice", currentInvoice);
        model.addAttribute("product", new ProductDTO());
        model.addAttribute("products", productService.getAllProducts());
        model.addAttribute("clients", clientVendorService.getAllClientsVendors());
        model.addAttribute("invoiceProducts", currentInvoice.getProducts());

        return "invoice/sales-invoice-create";
    }

    @PostMapping("/create-product")
    public String productCreateForInvoice(Model model, ProductDTO productDTO) throws CocoonException {

        ProductDTO retrievedProduct = productService.getProductById(productDTO.getId());
        currentInvoice.getProducts().add(retrievedProduct);
        model.addAttribute("invoiceProducts", currentInvoice.getProducts());

        return "redirect:/sales-invoice/addition";
    }

    @PostMapping("/create")
    public String createInvoice(InvoiceDTO dto) throws CocoonException {

        InvoiceDTO savedInvoice = invoiceService.save(dto);

        for (ProductDTO productDTO : currentInvoice.getProducts()){
            var eachProduct = productService.getProductById(productDTO.getId());
            eachProduct.getInvoices().add(savedInvoice);
            var savedProduct = productService.save(eachProduct);
            savedInvoice.getProducts().add(savedProduct);
        }
        invoiceService.save(savedInvoice);
        currentInvoice = new InvoiceDTO();

        return "redirect:/sales-invoice/list";
    }

    @GetMapping("/update/{id}")
    public String updateInvoice(@PathVariable("id") Long id, Model model){

        InvoiceDTO invoiceDTO = invoiceService.getInvoiceById(id);
        model.addAttribute("invoice", invoiceDTO);
        model.addAttribute("product", new ProductDTO());
        model.addAttribute("products", productService.getAllProducts());
        model.addAttribute("clients", clientVendorService.getAllClientsVendors());// TODO get client
        model.addAttribute("invoiceProducts", productService.getProductsByInvoiceId(invoiceDTO.getId()));

        return "invoice/sales-invoice-update";
    }

    @GetMapping("/addition-update/{id}")
    public String invoiceUpdateMore(@PathVariable("id") Long id, Model model){

        InvoiceDTO invoiceDTO = invoiceService.getInvoiceById(id);
        model.addAttribute("invoice", invoiceDTO);
        model.addAttribute("product", new ProductDTO());
        model.addAttribute("products", productService.getAllProducts());
        model.addAttribute("clients", clientVendorService.getAllClientsVendors());
        model.addAttribute("invoiceProducts", productService.getProductsByInvoiceId(invoiceDTO.getId()));

        return "invoice/sales-invoice-update";
    }

    @PostMapping("/create-product-update/{id}")
    public String updateProductForInvoice(@PathVariable("id") Long id, Model model, ProductDTO productDTO) throws CocoonException {

        InvoiceDTO invoiceDTO = invoiceService.getInvoiceById(id);
        invoiceDTO.getProducts().add(productService.getProductById(productDTO.getId()));
        model.addAttribute("invoiceProducts", invoiceDTO.getProducts());

        return "redirect:/sales-invoice/addition-update/"+id;
    }

    @PostMapping("/update/{id}")
    public String updateInvoice(@PathVariable("id") Long id, InvoiceDTO invoiceDTO){

        invoiceService.update(invoiceDTO, id);

        return "redirect:/sales-invoice/list";

    }

}
