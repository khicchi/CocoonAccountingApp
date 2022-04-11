package com.cocoon.controller;

import com.cocoon.dto.currency.CurrencyDto;
import com.cocoon.dto.currency.Rate;
import com.cocoon.entity.jpa_customization.IInvoiceForDashBoard;
import com.cocoon.service.CompanyService;
import com.cocoon.service.InvoiceService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.*;

@Controller
@RequestMapping("/dashboard")
public class DashboardController {

    private final CompanyService companyService;
    private final InvoiceService invoiceService;


    public DashboardController(CompanyService companyService, InvoiceService invoiceService) {
        this.companyService = companyService;
        this.invoiceService = invoiceService;
    }


    @GetMapping()
    public String invoiceTopThreeList(Model model){
        //added by kicchi to add the current currency rates
        model.addAttribute("rates", getExchangeRates());

        List<IInvoiceForDashBoard> updatedInvoices = invoiceService.getDashboardInvoiceTop3(companyService.getCompanyByLoggedInUser().getId());
        model.addAttribute("invoices", updatedInvoices);
        model.addAttribute("result", invoiceService.calculateTotalProfitLoss());
        return "dashboard";
    }

    //getting the current exchange rates
    //http://data.fixer.io/api/latest?access_key=0efa2bc3ea0bb4f378496f560590a648&symbols=USD,GBP,RUB,JPY&format=1
    private Rate getExchangeRates(){
        //normally this section will work, but free api usage restriction is a blocker to use this every time
//         CurrencyDto respo = webClient.get()
//                 .uri(uriBuilder ->
//                         uriBuilder
//                                 .path("/api/latest")
//                                 .queryParam("access_key","0efa2bc3ea0bb4f378496f560590a648")
//                                 .queryParam("symbols","USD,GBP,RUB,JPY")
//                                 .queryParam("format","1")
//                                 .build()
//                 )
//                .retrieve()
//                .bodyToMono(CurrencyDto.class).block();

        CurrencyDto respo = new CurrencyDto();
        respo.rates = new Rate();
        respo.rates.GBP = 0.82d;
        respo.rates.JPY = 127.7d;
        respo.rates.RUB = 118.02d;
        respo.rates.USD = 1.10d;
        return respo.rates;
    }

}