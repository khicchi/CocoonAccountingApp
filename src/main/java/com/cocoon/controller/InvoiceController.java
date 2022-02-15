package com.cocoon.controller;

import com.cocoon.dto.InvoiceDTO;
import com.cocoon.dto.InvoiceProductDTO;
import com.cocoon.dto.ProductDTO;
import com.cocoon.enums.InvoiceType;
import com.cocoon.exception.CocoonException;
import com.cocoon.service.ClientVendorService;
import com.cocoon.service.InvoiceProductService;
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

    private InvoiceDTO currentInvoiceDTO = new InvoiceDTO();
    private InvoiceProductDTO invoiceProductDTO = new InvoiceProductDTO();

    private final InvoiceService invoiceService;
    private final ProductService productService;
    private final InvoiceProductService invoiceProductService;
    private final ClientVendorService clientVendorService;

    public InvoiceController(InvoiceService invoiceService, ProductService productService, InvoiceProductService invoiceProductService, ClientVendorService clientVendorService) {
        this.invoiceService = invoiceService;
        this.productService = productService;
        this.invoiceProductService = invoiceProductService;
        this.clientVendorService = clientVendorService;
    }

    @GetMapping("/list")
    public String invoiceList(Model model){

        currentInvoiceDTO = new InvoiceDTO();
        List<InvoiceDTO> invoices = invoiceService.getAllInvoices();
        model.addAttribute("invoices", invoices);

        return "invoice/sales-invoice-list";
    }

    @GetMapping("/create")
    public String salesInvoiceCreate(Model model){

        currentInvoiceDTO.setInvoiceNo(invoiceService.getInvoiceNumber());
        currentInvoiceDTO.setInvoiceDate(LocalDate.now());
        model.addAttribute("invoice", currentInvoiceDTO);
        model.addAttribute("product", new InvoiceProductDTO());
        model.addAttribute("products", productService.getAllProducts());
        model.addAttribute("clients", clientVendorService.getAllClientsVendors());
        model.addAttribute("selectedproducts", currentInvoiceDTO.getInvoiceProduct());

        return "invoice/sales-invoice-create";
    }

    @PostMapping("/create-invoice-product")
    public String createInvoiceProduct(InvoiceProductDTO invoiceProductDTO){

        String name = invoiceProductDTO.getProductDTO().getName();
        invoiceProductDTO.setName(name);
        currentInvoiceDTO.getInvoiceProduct().add(invoiceProductDTO);
        return "redirect:/sales-invoice/create";
    }


    @PostMapping("/create-invoice")
    public String createInvoice(InvoiceDTO dto) throws CocoonException {

        currentInvoiceDTO.setInvoiceDate(dto.getInvoiceDate());
        currentInvoiceDTO.setInvoiceNo(dto.getInvoiceNo());
        currentInvoiceDTO.setClientVendor(dto.getClientVendor());
        currentInvoiceDTO.setInvoiceType(InvoiceType.SALE);
        InvoiceDTO savedInvoice = invoiceService.save(currentInvoiceDTO);
        currentInvoiceDTO.getInvoiceProduct().forEach(obj -> obj.setInvoiceDTO(savedInvoice));
        invoiceProductService.save(currentInvoiceDTO.getInvoiceProduct());

        return "redirect:/sales-invoice/list";
    }

    @GetMapping("/update/{id}")
    public String updateInvoice(@PathVariable("id") Long id, Model model){

        InvoiceDTO invoiceDTO = invoiceService.getInvoiceById(id);
        List<InvoiceProductDTO> databaseInvoiceProducts = invoiceProductService.getAllInvoiceProductsByInvoiceId(id);

        if (this.invoiceProductDTO.getName() != null){
            databaseInvoiceProducts.add(this.invoiceProductDTO);
        }
        model.addAttribute("invoice", invoiceDTO);
        model.addAttribute("product", new InvoiceProductDTO());
        model.addAttribute("products", productService.getAllProducts());
        model.addAttribute("clients", clientVendorService.getAllClientsVendors());
        model.addAttribute("invoiceProducts", databaseInvoiceProducts);

        return "invoice/sales-invoice-update";
    }

    @PostMapping("/create-product-update/{id}")
    public String updateProductForInvoice(@PathVariable("id") Long id, InvoiceProductDTO invoiceProductDTO) {

        this.invoiceProductDTO = invoiceProductDTO;
        return "redirect:/sales-invoice/update/"+id;
    }

    @PostMapping("/invoice-update/{id}")
    public String updateInvoice(@PathVariable("id") Long id, InvoiceDTO invoiceDTO){

        InvoiceDTO updatedInvoice = invoiceService.update(invoiceDTO, id);
        currentInvoiceDTO.getInvoiceProduct().forEach(obj -> obj.setInvoiceDTO(updatedInvoice));
        invoiceProductService.save(currentInvoiceDTO.getInvoiceProduct());
        return "redirect:/sales-invoice/list";

    }

}
