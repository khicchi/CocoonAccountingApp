package com.cocoon.controller;

import com.cocoon.dto.ClientVendorDTO;
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
import java.util.stream.Collectors;

@Controller
@RequestMapping("/sales-invoice")
public class InvoiceController {

    private InvoiceDTO currentInvoiceDTO = new InvoiceDTO();
    private List<InvoiceProductDTO> addedInvoiceProducts = new ArrayList<>();
    private List<InvoiceProductDTO> deletedInvoiceProducts = new ArrayList<>();
    private boolean active = true;

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

    @GetMapping({"/list", "/list/{cancel}"})
    public String invoiceList(@RequestParam(required = false) String cancel, Model model){

        if (cancel != null) this.active = true;
        currentInvoiceDTO = new InvoiceDTO();
        List<InvoiceDTO> invoices = invoiceService.getAllInvoicesByCompanyAndType(InvoiceType.SALE);
        model.addAttribute("invoices", invoices);
        model.addAttribute("client", new ClientVendorDTO());
        model.addAttribute("clients", clientVendorService.getAllClientsVendors());

        return "invoice/sales-invoice-list";
    }

    @GetMapping("/create")
    public String salesInvoiceCreate(@RequestParam(required = false) Long id, Model model) throws CocoonException {

        if (id != null){
            currentInvoiceDTO.setClientVendor(clientVendorService.findById(id));
        }
        currentInvoiceDTO.setInvoiceNumber(invoiceService.getInvoiceNumber(InvoiceType.SALE));
        currentInvoiceDTO.setInvoiceDate(LocalDate.now());
        model.addAttribute("active", active);
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
        this.active = false;
        return "redirect:/sales-invoice/create";
    }


    @PostMapping("/create-invoice")
    public String createInvoice() throws CocoonException {

        currentInvoiceDTO.setInvoiceType(InvoiceType.SALE);
        InvoiceDTO savedInvoice = invoiceService.save(currentInvoiceDTO);
        currentInvoiceDTO.getInvoiceProduct().forEach(obj -> obj.setInvoiceDTO(savedInvoice));
        invoiceProductService.save(currentInvoiceDTO.getInvoiceProduct());
        this.active = true;

        return "redirect:/sales-invoice/list";
    }

    // Update ----------------------------------------------------------------------------------------------------------
    @GetMapping("/update/{id}")
    public String updateInvoice(@PathVariable("id") Long id, Model model){

        InvoiceDTO invoiceDTO = invoiceService.getInvoiceById(id);
        Set<InvoiceProductDTO> databaseInvoiceProducts = invoiceProductService.getAllInvoiceProductsByInvoiceId(id);
        currentInvoiceDTO.setInvoiceProduct(databaseInvoiceProducts);

        if (this.addedInvoiceProducts.size() > 0 || this.deletedInvoiceProducts.size() > 0){
            addedInvoiceProducts.forEach(obj -> currentInvoiceDTO.getInvoiceProduct().add(obj));
            deletedInvoiceProducts.forEach(deleted -> currentInvoiceDTO.getInvoiceProduct().removeIf(obj -> obj.getName().equals(deleted.getName())));
        }
        model.addAttribute("active", active);
        model.addAttribute("invoice", invoiceDTO);
        model.addAttribute("product", new InvoiceProductDTO());
        model.addAttribute("products", productService.getAllProducts());
        model.addAttribute("clients", clientVendorService.getAllClientsVendors());
        model.addAttribute("invoiceProducts", currentInvoiceDTO.getInvoiceProduct());

        return "invoice/sales-invoice-update";
    }

    @PostMapping("/create-product-update/{id}")
    public String updateProductForInvoice(@PathVariable("id") Long id, InvoiceProductDTO invoiceProductDTO) {

        String name = invoiceProductDTO.getProductDTO().getName();
        invoiceProductDTO.setName(name);
        this.addedInvoiceProducts.add(invoiceProductDTO);
        this.active = false;
        return "redirect:/sales-invoice/update/"+id;
    }

    @PostMapping("/invoice-update/{id}")
    public String updateInvoice(@PathVariable("id") Long id, InvoiceDTO invoiceDTO){

        InvoiceDTO updatedInvoice = invoiceService.update(invoiceDTO, id);
        currentInvoiceDTO.getInvoiceProduct().forEach(obj -> obj.setInvoiceDTO(updatedInvoice));
        invoiceProductService.updateInvoiceProducts(id,currentInvoiceDTO.getInvoiceProduct());
        this.active = true;
        this.addedInvoiceProducts.clear();
        this.deletedInvoiceProducts.clear();
        return "redirect:/sales-invoice/list";
    }

    // Delete ----------------------------------------------------------------------------------------------------------
    @GetMapping("/delete/{id}")
    public String deleteInvoiceById(@PathVariable("id") Long id){

        invoiceService.deleteInvoiceById(id);
        return "redirect:/sales-invoice/list";
    }

    @GetMapping("/delete-product/{name}")
    public String deleteInvoiceProduct(@PathVariable("name") String name){

        Set<InvoiceProductDTO> selectedInvoiceProducts = currentInvoiceDTO.getInvoiceProduct();
        Set<InvoiceProductDTO> filteredInvoiceProducts = selectedInvoiceProducts.stream().filter(obj -> !obj.getName().equals(name)).collect(Collectors.toSet());
        currentInvoiceDTO.setInvoiceProduct(filteredInvoiceProducts);
        if (currentInvoiceDTO.getInvoiceProduct().size()==0) this.active = true;
        return "redirect:/sales-invoice/create";
    }

    @GetMapping("/delete-product-update/{id}/{name}")
    public String deleteInvoiceProductInUpdatePage(@PathVariable("id") Long id, @PathVariable("name") String name){
        this.active = false;
        Set<InvoiceProductDTO> selectedInvoiceProducts = currentInvoiceDTO.getInvoiceProduct();
        selectedInvoiceProducts.stream().filter(obj -> obj.getName().equals(name)).forEach(obj -> deletedInvoiceProducts.add(obj));
        return "redirect:/sales-invoice/update/"+id;
    }

}
