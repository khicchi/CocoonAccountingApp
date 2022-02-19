package com.cocoon.controller;

import com.cocoon.annotation.ExecutionTimeLog;
import com.cocoon.dto.ClientDTO;
import com.cocoon.entity.Client;
import com.cocoon.exception.CocoonException;
import com.cocoon.repository.StateRepo;
import com.cocoon.service.ClientVendorService;
import com.cocoon.util.MapperUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/client-vendor")
public class ClientVendorController {

    ClientVendorService clientVendorService;
    private StateRepo stateRepo;
    private MapperUtil mapperUtil;

    public ClientVendorController(ClientVendorService clientVendorService, StateRepo stateRepo, MapperUtil mapperUtil) {
        this.clientVendorService = clientVendorService;
        this.stateRepo = stateRepo;
        this.mapperUtil = mapperUtil;
    }

    @ExecutionTimeLog()
    @GetMapping("/list")
    public String readAllClientVendor(Model model) {

        model.addAttribute("clientVendorList", clientVendorService.getAllClientsVendorsActivesFirst());

        return "clientvendor/client-vendor-list";
    }

    @ExecutionTimeLog()
    @GetMapping("/update/{id}")
    public String editCompany(@PathVariable("id") long id, Model model) throws CocoonException {
        model.addAttribute("client", clientVendorService.findById(id));
        model.addAttribute("states", stateRepo.findAll());
        return "clientvendor/client-vendor-edit";
    }

    @ExecutionTimeLog()
    @PostMapping("/update/{id}")
    public String updateCompany(ClientDTO vendorClientDto) throws CocoonException {
        clientVendorService.update(vendorClientDto);
        return "redirect:/client-vendor/list";
    }

    @ExecutionTimeLog()
    @GetMapping("/delete/{id}")
    public String deleteUser(ClientDTO vendorClientDto) throws CocoonException {
        clientVendorService.deleteClientVendor(vendorClientDto.getId());
        return "redirect:/client-vendor/list";
    }

    //@ExecutionTimeLog()
    @GetMapping("/create")
    public String getCreatePage(Model model){
        model.addAttribute("client", new Client());
        model.addAttribute("states", stateRepo.findAll());

        return "clientvendor/client-vendor-add";
    }

    //@ExecutionTimeLog()
    @PostMapping("/create")
    public String saveClient(Client client, BindingResult result, Model model) {
        try{
            clientVendorService.save(mapperUtil.convert(client, new ClientDTO()));
            return "redirect:/client-vendor/list";
        }catch (Exception e){
            String err = e.getMessage();

            if (!err.isEmpty()) {
                ObjectError error = new ObjectError("globalError", err);
                result.addError(error);
            }

            model.addAttribute("states", stateRepo.findAll());
            return "clientvendor/client-vendor-add";
        }
    }
}
