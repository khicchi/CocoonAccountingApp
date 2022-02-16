package com.cocoon.controller;

import com.cocoon.dto.InvoiceDTO;
import com.cocoon.dto.InvoiceProductDTO;
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
import java.util.List;

@Controller
@RequestMapping("/purchase-invoice")
public class PurchaseInvoiceController {

    private InvoiceDTO currentInvoiceDTO = new InvoiceDTO();
    private InvoiceProductDTO invoiceProductDTO = new InvoiceProductDTO();
    private boolean active = true;

    private final InvoiceService invoiceService;
    private final ProductService productService;
    private final InvoiceProductService invoiceProductService;
    private final ClientVendorService clientVendorService;

    public PurchaseInvoiceController(InvoiceService invoiceService, ProductService productService, InvoiceProductService invoiceProductService, ClientVendorService clientVendorService) {
        this.invoiceService = invoiceService;
        this.productService = productService;
        this.invoiceProductService = invoiceProductService;
        this.clientVendorService = clientVendorService;
    }

    @GetMapping({"/list", "/list/{cancel}"})
    public String invoiceList(@RequestParam(required = false) String cancel, Model model){

        if (cancel != null) this.active = true;
        currentInvoiceDTO = new InvoiceDTO();
        List<InvoiceDTO> invoices = invoiceService.getAllInvoices();
        model.addAttribute("invoices", invoices);

        return "invoice/purchase-invoice-list";
    }

    @GetMapping("/create")
    public String purchaseInvoiceCreate(Model model){

        currentInvoiceDTO.setInvoiceNumber(invoiceService.getInvoiceNumber(InvoiceType.PURCHASE));
        currentInvoiceDTO.setInvoiceDate(LocalDate.now());
        model.addAttribute("active", active);
        model.addAttribute("invoice", currentInvoiceDTO);
        model.addAttribute("product", new InvoiceProductDTO());
        model.addAttribute("products", productService.getAllProducts());
        model.addAttribute("clients", clientVendorService.getAllClientsVendors());
        model.addAttribute("selectedproducts", currentInvoiceDTO.getInvoiceProduct());

        return "invoice/purchase-invoice-create";
    }

    @PostMapping("/create-invoice-product")
    public String createInvoiceProduct(InvoiceProductDTO invoiceProductDTO){

        String name = invoiceProductDTO.getProductDTO().getName();
        invoiceProductDTO.setName(name);
        currentInvoiceDTO.getInvoiceProduct().add(invoiceProductDTO);
        this.active = false;
        return "redirect:/purchase-invoice/create";
    }


    @PostMapping("/create-invoice")
    public String createInvoice(InvoiceDTO dto) throws CocoonException {

        currentInvoiceDTO.setInvoiceDate(dto.getInvoiceDate());
        currentInvoiceDTO.setInvoiceNumber(dto.getInvoiceNumber());
        currentInvoiceDTO.setClientVendor(dto.getClientVendor());
        currentInvoiceDTO.setInvoiceType(InvoiceType.PURCHASE);
        InvoiceDTO savedInvoice = invoiceService.save(currentInvoiceDTO);
        currentInvoiceDTO.getInvoiceProduct().forEach(obj -> obj.setInvoiceDTO(savedInvoice));
        invoiceProductService.save(currentInvoiceDTO.getInvoiceProduct());
        this.active = true;

        return "redirect:/purchase-invoice/list";
    }

    // Update ----------------------------------------------------------------------------------------------------------
    @GetMapping("/update/{id}")
    public String updateInvoice(@PathVariable("id") Long id, Model model){

        InvoiceDTO invoiceDTO = invoiceService.getInvoiceById(id);
        List<InvoiceProductDTO> databaseInvoiceProducts = invoiceProductService.getAllInvoiceProductsByInvoiceId(id);

        if (this.invoiceProductDTO.getName() != null){
            databaseInvoiceProducts.add(this.invoiceProductDTO);
        }
        model.addAttribute("active", active);
        model.addAttribute("invoice", invoiceDTO);
        model.addAttribute("product", new InvoiceProductDTO());
        model.addAttribute("products", productService.getAllProducts());
        model.addAttribute("clients", clientVendorService.getAllClientsVendors());
        model.addAttribute("invoiceProducts", databaseInvoiceProducts);

        return "invoice/purchase-invoice-update";
    }

    @PostMapping("/create-product-update/{id}")
    public String updateProductForInvoice(@PathVariable("id") Long id, InvoiceProductDTO invoiceProductDTO) {

        String name = invoiceProductDTO.getProductDTO().getName();
        invoiceProductDTO.setName(name);
        this.invoiceProductDTO = invoiceProductDTO;
        this.active = false;
        return "redirect:/purchase-invoice/update/"+id;
    }

    @PostMapping("/invoice-update/{id}")
    public String updateInvoice(@PathVariable("id") Long id, InvoiceDTO invoiceDTO){

        InvoiceDTO updatedInvoice = invoiceService.update(invoiceDTO, id);
        currentInvoiceDTO.getInvoiceProduct().forEach(obj -> obj.setInvoiceDTO(updatedInvoice));
        invoiceProductService.save(currentInvoiceDTO.getInvoiceProduct());
        this.active = true;
        return "redirect:/purchase-invoice/list";
    }

    // Delete ----------------------------------------------------------------------------------------------------------
    @GetMapping("/delete/{id}")
    public String deleteInvoiceById(@PathVariable("id") Long id){

        invoiceService.deleteInvoiceById(id);
        return "redirect:/purchase-invoice/list";

    }

}
