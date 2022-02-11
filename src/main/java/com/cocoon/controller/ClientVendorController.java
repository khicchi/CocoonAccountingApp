package com.cocoon.controller;

import com.cocoon.service.ClientVendorService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/client-vendor")
public class ClientVendorController {

    ClientVendorService clientVendorService;

    public ClientVendorController(ClientVendorService clientVendorService) {
        this.clientVendorService = clientVendorService;
    }

    @GetMapping("/list")
    private String readAllClientVendor(Model model) {

        model.addAttribute("clientVendorList", clientVendorService.getAllClientsVendorsActivesFirst());

        return "clientvendor/client-vendor-list";
    }
}
