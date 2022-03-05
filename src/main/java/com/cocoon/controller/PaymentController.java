package com.cocoon.controller;

import com.cocoon.dto.PaymentDTO;
import com.cocoon.entity.Payment;
import com.cocoon.enums.Months;
import com.cocoon.service.PaymentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/payment")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }


    @GetMapping({"/list", "/list/{year}"})
    public String createPayment(@RequestParam(value = "year", required = false) String selectedYear, Model model) {

        int year = (selectedYear == null) ? LocalDate.now().getYear() : Integer.parseInt(selectedYear);
        paymentService.createPaymentsIfNotExist(year);
        model.addAttribute("payments",paymentService.getAllPaymentsByYear(year));
        model.addAttribute("year", selectedYear);
        return "payment/payment-list";
    }

    @ModelAttribute
    public void addAttributes(Model model) {
        model.addAttribute("date", new Date());
        model.addAttribute("localDateTime", LocalDateTime.now());
        model.addAttribute("localDate", LocalDate.now());
        model.addAttribute("java8Instant", Instant.now());
    }

//    @GetMapping("/newpayment/{id}")
//
//    @PostMapping("/newpayment/{consentId}")
//
//    @GetMapping("/newpayment/complete")*/




}
