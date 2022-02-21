package com.cocoon.controller;

import com.cocoon.dto.ClientDTO;
import com.cocoon.dto.InvoiceDTO;
import com.cocoon.dto.InvoiceProductDTO;
import com.cocoon.enums.CompanyType;
import com.cocoon.enums.InvoiceStatus;
import com.cocoon.enums.InvoiceType;
import com.cocoon.exception.CocoonException;
import com.cocoon.repository.ClientVendorRepo;
import com.cocoon.service.ClientVendorService;
import com.cocoon.service.InvoiceProductService;
import com.cocoon.service.InvoiceService;
import com.cocoon.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/purchase-invoice")
public class PurchaseInvoiceController {

    private InvoiceDTO currentInvoiceDTO = new InvoiceDTO();
    private List<InvoiceProductDTO> addedInvoiceProducts = new ArrayList<>();
    private List<InvoiceProductDTO> deletedInvoiceProducts = new ArrayList<>();
    private boolean active = true;

    private final InvoiceService invoiceService;
    private final ProductService productService;
    private final InvoiceProductService invoiceProductService;
    private final ClientVendorService clientVendorService;
    private final ClientVendorRepo clientVendorRepo;

    public PurchaseInvoiceController(InvoiceService invoiceService, ProductService productService, InvoiceProductService invoiceProductService, ClientVendorService clientVendorService, ClientVendorRepo clientVendorRepo) {
        this.invoiceService = invoiceService;
        this.productService = productService;
        this.invoiceProductService = invoiceProductService;
        this.clientVendorService = clientVendorService;
        this.clientVendorRepo = clientVendorRepo;
    }

    @GetMapping({"/list", "/list/{cancel}"})
    public String invoiceList(@RequestParam(required = false) String cancel, Model model){

        if (cancel != null){
            this.active = true;
            this.addedInvoiceProducts.clear();
            this.deletedInvoiceProducts.clear();
        }
        currentInvoiceDTO = new InvoiceDTO();
        List<InvoiceDTO> invoices = invoiceService.getAllInvoicesByCompanyAndType(InvoiceType.PURCHASE);
        List<InvoiceDTO> updatedInvoices = invoices.stream().map(invoiceService::calculateInvoiceCost).collect(Collectors.toList());
        model.addAttribute("invoices", updatedInvoices);
        model.addAttribute("client", new ClientDTO());
        model.addAttribute("clients", clientVendorService.getAllClientVendorsByType(CompanyType.VENDOR));

        return "invoice/purchase-invoice-list";
    }

    @GetMapping("/create")
    public String purchaseInvoiceCreate(@RequestParam(required = false) Long id, Model model) throws CocoonException{

        if (id != null){
            currentInvoiceDTO.setClient(clientVendorRepo.getById(id));
        }
        currentInvoiceDTO.setInvoiceNumber(invoiceService.getInvoiceNumber(InvoiceType.PURCHASE));
        currentInvoiceDTO.setInvoiceDate(LocalDate.now());
        model.addAttribute("active", active);
        model.addAttribute("invoice", currentInvoiceDTO);
        model.addAttribute("product", new InvoiceProductDTO());
        model.addAttribute("products", productService.getAllProducts());
        model.addAttribute("clients", clientVendorService.getAllClientVendorsByType(CompanyType.VENDOR));
        model.addAttribute("selectedproducts", currentInvoiceDTO.getInvoiceProduct());

        return "invoice/purchase-invoice-create";
    }

    @PostMapping("/create-invoice-product")
    public String createInvoiceProduct(InvoiceProductDTO invoiceProductDTO){

        String name = invoiceProductDTO.getProductDTO().getName();
        invoiceProductDTO.setName(name); // TODO
        currentInvoiceDTO.getInvoiceProduct().add(invoiceProductDTO);
        this.active = false;
        return "redirect:/purchase-invoice/create";
    }


    @PostMapping("/create-invoice")
    public String createInvoice() throws CocoonException {

        currentInvoiceDTO.setInvoiceType(InvoiceType.PURCHASE);
        InvoiceDTO savedInvoice = invoiceService.save(currentInvoiceDTO);
        invoiceProductService.approveInvoiceProduct(savedInvoice.getId());
        this.active = true;

        return "redirect:/purchase-invoice/list";
    }

    // Update ----------------------------------------------------------------------------------------------------------
    @GetMapping("/update/{id}")
    public String updateInvoice(@PathVariable("id") Long id, Model model){

        InvoiceDTO invoiceDTO = invoiceService.getInvoiceById(id);
        Set<InvoiceProductDTO> databaseInvoiceProducts = invoiceProductService.getAllInvoiceProductsByInvoiceId(id);
        currentInvoiceDTO.setInvoiceProduct(databaseInvoiceProducts);

        if (this.addedInvoiceProducts.size() > 0 || this.deletedInvoiceProducts.size() > 0){
            addedInvoiceProducts.forEach(obj -> currentInvoiceDTO.getInvoiceProduct().add(obj));
            deletedInvoiceProducts.forEach(deleted -> currentInvoiceDTO.getInvoiceProduct().removeIf(obj -> obj.equals(deleted)));
        }
        model.addAttribute("active", active);
        model.addAttribute("invoice", invoiceDTO);
        model.addAttribute("product", new InvoiceProductDTO());
        model.addAttribute("products", productService.getAllProducts());
        model.addAttribute("clients", clientVendorService.getAllClientVendorsByType(CompanyType.VENDOR));
        model.addAttribute("invoiceProducts", currentInvoiceDTO.getInvoiceProduct());

        return "invoice/purchase-invoice-update";
    }

    @PostMapping("/create-product-update/{id}")
    public String updateProductForInvoice(@PathVariable("id") Long id, InvoiceProductDTO invoiceProductDTO) {

        String name = invoiceProductDTO.getProductDTO().getName();
        invoiceProductDTO.setName(name);
        this.addedInvoiceProducts.add(invoiceProductDTO);
        this.active = false;
        return "redirect:/purchase-invoice/update/"+id;
    }

    @PostMapping("/invoice-update/{id}")
    public String updateInvoice(@PathVariable("id") Long id, InvoiceDTO invoiceDTO){

        invoiceDTO.setInvoiceStatus(InvoiceStatus.APPROVED);
        InvoiceDTO updatedInvoice = invoiceService.update(invoiceDTO, id);
        currentInvoiceDTO.getInvoiceProduct().forEach(obj -> obj.setInvoiceDTO(updatedInvoice));
        invoiceProductService.updateInvoiceProducts(id,currentInvoiceDTO.getInvoiceProduct());
        this.active = true;
        this.addedInvoiceProducts.clear();
        this.deletedInvoiceProducts.clear();
        return "redirect:/purchase-invoice/list";
    }

    // Delete ----------------------------------------------------------------------------------------------------------
    @GetMapping("/delete/{id}")
    public String deleteInvoiceById(@PathVariable("id") Long id){

        invoiceProductService.deleteInvoiceProducts(id);
        invoiceService.deleteInvoiceById(id);
        return "redirect:/purchase-invoice/list";

    }

    @GetMapping("/delete-product/{name}")
    public String deleteInvoiceProduct(@PathVariable("name") String name){

        Set<InvoiceProductDTO> selectedInvoiceProducts = currentInvoiceDTO.getInvoiceProduct();
        Predicate<InvoiceProductDTO> selectedProductFinder = (obj) -> !(""+obj.getName() + obj.getPrice() + obj.getQty() + obj.getTax()).equals(name);
        Set<InvoiceProductDTO> filteredInvoiceProducts = selectedInvoiceProducts.stream().filter(selectedProductFinder).collect(Collectors.toSet());
        currentInvoiceDTO.setInvoiceProduct(filteredInvoiceProducts);
        if (currentInvoiceDTO.getInvoiceProduct().size()==0) this.active = true;
        return "redirect:/purchase-invoice/create";
    }

    @GetMapping("/delete-product-update/{id}/{name}")
    public String deleteInvoiceProductInUpdatePage(@PathVariable("id") Long id, @PathVariable("name") String name){
        if (currentInvoiceDTO.getInvoiceProduct().size()!=0) this.active = false;
        Set<InvoiceProductDTO> selectedInvoiceProducts = currentInvoiceDTO.getInvoiceProduct();
        Predicate<InvoiceProductDTO> selectedProductFinder = (obj) -> (""+obj.getName() + obj.getPrice() + obj.getQty() + obj.getTax()).equals(name);
        selectedInvoiceProducts.stream().filter(selectedProductFinder).forEach(obj -> deletedInvoiceProducts.add(obj));
        return "redirect:/purchase-invoice/update/"+id;
    }

}
