package com.cocoon.controller;

import com.cocoon.dto.CompanyDTO;
import com.cocoon.dto.UserDTO;
import com.cocoon.exception.CocoonException;
import com.cocoon.repository.RoleRepo;
import com.cocoon.repository.UserRepo;
import com.cocoon.service.CompanyService;
import com.cocoon.service.RoleService;
import com.cocoon.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user")
public class UserController {

    private UserService userService;
    private RoleService roleService;
    private CompanyService companyService;

    public UserController(UserService userService, RoleService roleService, CompanyService companyService) {
        this.userService = userService;
        this.roleService = roleService;
        this.companyService = companyService;
    }

    @GetMapping("/list")
    public String findUsers(Model model) {
        model.addAttribute("users", userService.findAllUsers());
        return "user/user-list";
    }

    @PostMapping("/create")
    public String createUser(UserDTO userDTO) throws CocoonException {
        userService.save(userDTO);
        return "redirect:/user/list";
    }

    @GetMapping("/create")
    public String getCreatePage(Model model) {
        model.addAttribute("user", new UserDTO());
        model.addAttribute("roles", roleService.findAllRoles());
        model.addAttribute("companies", companyService.getAllCompanies());
        return "user/user-add";
    }


}
