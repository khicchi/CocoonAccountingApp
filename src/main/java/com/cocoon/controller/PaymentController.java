package com.cocoon.controller;

import com.cocoon.dto.InstitutionDTO;
import com.cocoon.dto.PaymentDTO;
import com.cocoon.service.InstitutionService;
import com.cocoon.service.PaymentService;
import com.cocoon.util.payment.GetInstitutions;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/payment")
public class PaymentController {

    private WebClient webClient = WebClient.builder().baseUrl("https://api.yapily.com").build();
    private final PaymentService paymentService;
    private final InstitutionService institutionService;

    public PaymentController(PaymentService paymentService, InstitutionService institutionService) {
        this.paymentService = paymentService;
        this.institutionService = institutionService;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void getInstitutionsAfterStartUp() {
        System.out.println(institutionService.getInstitutionsAtStartUp());
    }


    @GetMapping({"/list", "/list/{year}"})
    public String createPayment(@RequestParam(value = "year", required = false) String selectedYear, Model model) {

        int year = (selectedYear == null) ? LocalDate.now().getYear() : Integer.parseInt(selectedYear);
        paymentService.createPaymentsIfNotExist(year);
        model.addAttribute("payments",paymentService.getAllPaymentsByYear(year));
        model.addAttribute("year", selectedYear);
        return "payment/payment-list";
    }


    @GetMapping("/newpayment/{id}")
    public String selectInstitution(@PathVariable("id") Long id, Model model){

        //institutionService.saveIfNotExist(GetInstitutions.institutions);
        model.addAttribute("institutions", institutionService.getAllInstitutions());
        model.addAttribute("payment", paymentService.getPaymentById(id));

        return "payment/payment-method";
    }

    @PostMapping("/newpayment")
    public String selectInstitutionPost(PaymentDTO paymentDTO){
        paymentService.updatePayment(paymentDTO);

        return "redirect:/payment/list";
    }
//
//    @PostMapping("/newpayment/{consentId}")
//
//    @GetMapping("/newpayment/complete")*/

    @ModelAttribute
    public void addAttributes(Model model) {
        model.addAttribute("date", new Date());
        model.addAttribute("localDateTime", LocalDateTime.now());
        model.addAttribute("localDate", LocalDate.now());
        model.addAttribute("java8Instant", Instant.now());
    }


}
