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
import yapily.ApiException;

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
    private int selectedYear;
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

        this.selectedYear = (selectedYear == null || selectedYear.isEmpty()) ? LocalDate.now().getYear() : Integer.parseInt(selectedYear);
        paymentService.createPaymentsIfNotExist(this.selectedYear);
        model.addAttribute("payments",paymentService.getAllPaymentsByYear(this.selectedYear));
        model.addAttribute("year", this.selectedYear);
        return "payment/payment-list";
    }


    @GetMapping("/newpayment/{id}")
    public String selectInstitution(@PathVariable("id") Long id, Model model) throws ApiException {

        model.addAttribute("institutions", institutionService.getInstitutionsFromApi());
        model.addAttribute("payment", paymentService.getPaymentById(id));

        return "payment/payment-method";
    }

    @PostMapping("/newpayment/{id}")
    public String selectInstitutionPost(@PathVariable("id") Long id, PaymentDTO paymentDTO){

        PaymentDTO convertedPaymentDto = paymentService.getPaymentById(id);
        convertedPaymentDto.setInstitution(paymentDTO.getInstitution());
        paymentService.updatePayment(convertedPaymentDto);

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
