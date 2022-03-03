package com.cocoon.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/payment")
public class PaymentController {

    List<String> payments = new ArrayList<String>(Arrays.asList("hi","my"));

    @GetMapping("/list")
    public String createPayment(Model model) {

        model.addAttribute("payments",payments);
        return "payment/payment-list";
    }

//    @GetMapping("/newpayment/{id}")
//
//    @PostMapping("/newpayment/{consentId}")
//
//    @GetMapping("/newpayment/complete")*/




}
