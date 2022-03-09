package com.cocoon.controller;

import com.cocoon.dto.PaymentDTO;
import com.cocoon.exception.CocoonException;
import com.cocoon.service.CompanyService;
import com.cocoon.service.InstitutionService;
import com.cocoon.service.PaymentService;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import yapily.ApiException;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Controller
@RequestMapping("/payment")
public class PaymentController {

    private WebClient webClient = WebClient.builder().baseUrl("https://api.yapily.com").build();
    private final PaymentService paymentService;
    private final InstitutionService institutionService;
    private final CompanyService companyService;

    public PaymentController(PaymentService paymentService, InstitutionService institutionService, CompanyService companyService) {
        this.paymentService = paymentService;
        this.institutionService = institutionService;
        this.companyService = companyService;
    }

//    @EventListener(ApplicationReadyEvent.class)
//    public void getInstitutionsAfterStartUp() {
//        System.out.println(institutionService.getInstitutionsAtStartUp());
//    }


    @GetMapping({"/list", "/list/{year}"})
    public String createPayment(@RequestParam(value = "year", required = false) String selectedYear, Model model) {

        int selectedYear1 = (selectedYear == null || selectedYear.isEmpty()) ? LocalDate.now().getYear() : Integer.parseInt(selectedYear);
        paymentService.createPaymentsIfNotExist(selectedYear1);
        model.addAttribute("payments",paymentService.getAllPaymentsByYear(selectedYear1));
        model.addAttribute("year", selectedYear1);
        return "payment/payment-list";
    }


    @GetMapping("/newpayment/{id}")
    public String selectInstitution(@PathVariable("id") Long id, Model model) throws ApiException {

        model.addAttribute("institutions", institutionService.getInstitutionsFromApi());
        model.addAttribute("payment", paymentService.getPaymentById(id));

        return "payment/payment-method";
    }

    @PostMapping("/newpayment/{id}")
    public String selectInstitutionPost(@PathVariable("id") Long id, PaymentDTO paymentDTO) throws ApiException, URISyntaxException, IOException {

        PaymentDTO convertedPaymentDto = paymentService.getPaymentById(id);
        convertedPaymentDto.setInstitution(paymentDTO.getInstitution());
        paymentService.makePaymentWithSelectedInstitution(paymentDTO.getInstitution().getId());
        paymentService.updatePayment(convertedPaymentDto);

        return "redirect:/payment/list";
    }

    // To invoice

    @GetMapping("/toInvoice/{id}")
    public String toInvoice(@PathVariable("id") Long id, Model model) throws CocoonException {

        model.addAttribute("payment", paymentService.getPaymentById(id));
        model.addAttribute("company", companyService.getCompanyByLoggedInUser());

        return "payment/payment-success";
    }


    @ModelAttribute
    public void addAttributes(Model model) {
        model.addAttribute("date", new Date());
        model.addAttribute("localDateTime", LocalDateTime.now());
        model.addAttribute("localDate", LocalDate.now());
        model.addAttribute("java8Instant", Instant.now());
    }


}
