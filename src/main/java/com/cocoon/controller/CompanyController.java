package com.cocoon.controller;

import com.cocoon.dto.CompanyDTO;
import com.cocoon.entity.State;
import com.cocoon.enums.ProductStatus;
import com.cocoon.entity.Company;
import com.cocoon.exception.CocoonException;
import com.cocoon.repository.StateRepo;
import com.cocoon.service.CompanyService;
import com.cocoon.util.MapperUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;

@Controller
@RequestMapping("/company")
public class CompanyController {

    @Autowired
    private CompanyService companyService;
    @Autowired
    private StateRepo stateRepo;
    @Autowired
    private MapperUtil mapperUtil;

    @GetMapping("/list")
    public String getCompanies(Model model) {
        model.addAttribute("companies", companyService.getAllCompanies());

        return "company/company-list";
    }

    @GetMapping("/create")
    public String getCreatePage(Model model) {
        model.addAttribute("company", new Company());
        model.addAttribute("states", stateRepo.findAll());

        return "company/company-add";
    }

    @PostMapping("/create")
    public String saveCompany(Company company, BindingResult result, Model model) {
        try{
            companyService.save(mapperUtil.convert(company, new CompanyDTO()));
            return "redirect:/company/list";
        }catch (Exception e){
            String err = e.getMessage();

            if (!err.isEmpty()) {
                ObjectError error = new ObjectError("globalError", err);
                result.addError(error);
            }

            return "company/company-add";
        }
    }

    @GetMapping("/update/{id}")
    public String getUpdatePage(@PathVariable("id") String id, Model model) throws CocoonException {
        model.addAttribute("company", companyService.getCompanyById(Long.valueOf(id)));
        model.addAttribute("states", stateRepo.findAll());
        return "company/company-edit";
    }

    @PostMapping("/update/{id}")
    public String updateCompany(@PathVariable("id") Long id, CompanyDTO companyDTO, Model model) throws CocoonException {
        companyService.update(companyDTO);
        return "redirect:/company/list";
    }

    @GetMapping("/close/{id}")
    public String getClosePage(@PathVariable("id") String id, Model model) throws CocoonException {
        companyService.close(companyService.getCompanyById(Long.valueOf(id)));
        return "redirect:/company/list";
    }

    @GetMapping("/open/{id}")
    public String getOpenPage(@PathVariable("id") String id, Model model) throws CocoonException {
        companyService.open(companyService.getCompanyById(Long.valueOf(id)));
        return "redirect:/company/list";
    }

    @GetMapping("/delete/{id}")
    public String deleteCompany(@PathVariable("id") String id, Model model) throws CocoonException {
        companyService.delete(companyService.getCompanyById(Long.valueOf(id)));
        return "redirect:/company/list";
    }

    /*
    @GetMapping("/delete/{id}")
    public String deleteCompany(@PathVariable("id") String id, Model model) throws CocoonException{
        model.addAttribute("company", companyService.getCompanyById(Long.valueOf(id)));
        model.addAttribute("states", stateRepo.findAll());
        return "company/company-edit";
    }*/
/*
    @GetMapping("/delete/{username}")
    public String deleteUser(@PathVariable("username") String username) throws CocoonException {
        companyService.delete(username);

        return "company/company-add";
    }*/

}
