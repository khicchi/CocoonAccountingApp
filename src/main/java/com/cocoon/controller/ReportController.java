package com.cocoon.controller;

import com.cocoon.dto.InvoiceProductDTO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;

@Controller
@RequestMapping("/report")
public class ReportController {

    @GetMapping("/stock")
    public String getStock(Model model){
        model.addAttribute("stock", new ArrayList<InvoiceProductDTO>());
        return "report/stock-report.html";
    }
}
