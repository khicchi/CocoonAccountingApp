package com.cocoon.controller;

import com.cocoon.repository.TestRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class TestController {

    @Autowired
    TestRepo testRepo;

    @RequestMapping(value = {"/test"})
    public String test(Model model){

        model.addAttribute("dbmessage", testRepo.getById(0).getTestval());
        return "test";
    }

    @RequestMapping(value = "/test/dashboard")
    public String getDashBoard(){
        return "dashboard";
    }
}
