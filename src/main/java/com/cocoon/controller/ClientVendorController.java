package com.cocoon.controller;

import com.cocoon.dto.ClientVendorDTO;
import com.cocoon.exception.CocoonException;
import com.cocoon.repository.StateRepo;
import com.cocoon.service.ClientVendorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/client-vendor")
public class ClientVendorController {

    ClientVendorService clientVendorService;
    private StateRepo stateRepo;

    public ClientVendorController(ClientVendorService clientVendorService, StateRepo stateRepo) {
        this.clientVendorService = clientVendorService;
        this.stateRepo = stateRepo;
    }

    @GetMapping("/list")
    private String readAllClientVendor(Model model) {

        model.addAttribute("clientVendorList", clientVendorService.getAllClientsVendorsActivesFirst());

        return "clientvendor/client-vendor-list";
    }


    @GetMapping("/update/{id}")
    public String editCompany(@PathVariable("id") long id, Model model) throws CocoonException {
        model.addAttribute("client", clientVendorService.findById(id));
        model.addAttribute("states", stateRepo.findAll());
        return "client-vendor-edit";
    }


    @PostMapping("/update/{id}")
    public String updateCompany(@PathVariable("id") long id, ClientVendorDTO vendorClientDto) throws CocoonException {
        clientVendorService.update(vendorClientDto);
        return "redirect:/client-vendor-list";
    }

    @GetMapping("/delete/{email}")
    public String deleteUser(@PathVariable("email") String email,ClientVendorDTO vendorClientDto) throws CocoonException {
        clientVendorService.deleteClientVendor(email);
        return "redirect:/client-vendor-list";
    }

    @GetMapping("/create")
    public String getCreatePage(Model model){
        model.addAttribute("client", new ClientVendorDTO());
        model.addAttribute("states", stateRepo.findAll());

        return "clientvendor/client-vendor-add";
    }

    @PostMapping("/create")
    public String saveClient(ClientVendorDTO clientVendorDTO) throws CocoonException {
        clientVendorService.save(clientVendorDTO);
        return "redirect:/client-vendor/list";
    }
}
