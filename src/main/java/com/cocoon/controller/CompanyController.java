package com.cocoon.controller;

import com.cocoon.dto.CompanyDTO;
import com.cocoon.exception.CocoonException;
import com.cocoon.repository.StateRepo;
import com.cocoon.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/company")
public class CompanyController {

    @Autowired
    private CompanyService companyService;
    @Autowired
    private StateRepo stateRepo;

    @GetMapping("/list")
    public String getCompanies(Model model){
        model.addAttribute("companies", companyService.getAllCompanies());

        return "company/company-list";
    }

    @GetMapping("/create")
    public String getCreatePage(Model model){
        model.addAttribute("company", new CompanyDTO());
        model.addAttribute("states", stateRepo.findAll());

        return "company/company-add";
    }

    @PostMapping("/create")
    public String saveCompany(CompanyDTO companyDTO) throws CocoonException {
        companyService.save(companyDTO);
        return "company/company-list";
    }

    @GetMapping("/update/{id}")
    public String getUpdatePage(@PathVariable("id") String id, Model model) throws CocoonException{
        model.addAttribute("company", companyService.getCompanyById(Long.valueOf(id)));
        model.addAttribute("states", stateRepo.findAll());

        return "company/company-edit";
    }

}
