package com.cocoon.controller;

import com.cocoon.dto.InvoiceDTO;
import com.cocoon.dto.InvoiceProductDTO;
import com.cocoon.dto.PaymentDTO;
import com.cocoon.entity.common.UserPrincipal;
import com.cocoon.service.*;
import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Set;

@Controller
@RequestMapping("/pdf")
public class PdfGenerateController {

    private final CompanyService companyService;
    private final ServletContext servletContext;
    private final TemplateEngine templateEngine;
    private final UserService userService;
    private final InvoiceService invoiceService;
    private final InvoiceProductService invoiceProductService;
    private final EmailSender emailSender;
    private final PaymentService paymentService;

    public PdfGenerateController(CompanyService companyService, ServletContext servletContext, TemplateEngine templateEngine, UserService userService, InvoiceService invoiceService, InvoiceProductService invoiceProductService, EmailSender emailSender, PaymentService paymentService) {
        this.companyService = companyService;
        this.servletContext = servletContext;
        this.templateEngine = templateEngine;
        this.userService = userService;
        this.invoiceService = invoiceService;
        this.invoiceProductService = invoiceProductService;
        this.emailSender = emailSender;
        this.paymentService = paymentService;
    }

    @GetMapping("/generate/{id}")
    public ResponseEntity<?> getPDF(@PathVariable("id") Long id, HttpServletRequest request, HttpServletResponse response) throws Exception {

        InvoiceDTO invoiceDTO = invoiceService.getInvoiceById(id);
        InvoiceDTO updatedInvoiceDTO = invoiceService.calculateInvoiceCost(invoiceDTO);
        Set<InvoiceProductDTO> invoiceProducts = invoiceProductService.getAllInvoiceProductsByInvoiceId(id);

        /*Create HTML using Thymeleaf template*/
        WebContext context = new WebContext(request, response, servletContext);

        context.setVariable("company", companyService.getCompanyByLoggedInUser());
        context.setVariable("invoice", updatedInvoiceDTO);
        context.setVariable("invoiceProducts", invoiceProducts);

        String companyListHTML = templateEngine.process("invoice/invoiceSuccess.html", context);

        /*Setup Source and target I/O streams*/
        ByteArrayOutputStream target = new ByteArrayOutputStream();
        ConverterProperties converterProperties = new ConverterProperties();
        converterProperties.setBaseUri("http://localhost:8080");
        /* Call convert method */
        HtmlConverter.convertToPdf(companyListHTML, target, converterProperties);

        /* extract output as bytes */
        byte[] bytes = target.toByteArray();

        /* Send the response as downloadable PDF */

        return ResponseEntity.ok()
//                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=company.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(bytes);


    }

    @GetMapping("/download/{id}")
    public ResponseEntity<?> downloadPDF(@PathVariable("id") Long id, HttpServletRequest request, HttpServletResponse response) throws Exception {

        InvoiceDTO invoiceDTO = invoiceService.getInvoiceById(id);
        InvoiceDTO updatedInvoiceDTO = invoiceService.calculateInvoiceCost(invoiceDTO);
        Set<InvoiceProductDTO> invoiceProducts = invoiceProductService.getAllInvoiceProductsByInvoiceId(id);


        /*Create HTML using Thymeleaf template*/
        WebContext context = new WebContext(request, response, servletContext);

        context.setVariable("company", companyService.getCompanyByLoggedInUser());
        context.setVariable("invoice", updatedInvoiceDTO);
        context.setVariable("invoiceProducts", invoiceProducts);

        String companyListHTML = templateEngine.process("invoice/invoiceSuccess.html", context);

        /*Setup Source and target I/O streams*/
        ByteArrayOutputStream target = new ByteArrayOutputStream();
        ConverterProperties converterProperties = new ConverterProperties();
        converterProperties.setBaseUri("http://localhost:8080");
        /* Call convert method */
        HtmlConverter.convertToPdf(companyListHTML, target, converterProperties);

        /* extract output as bytes */
        byte[] bytes = target.toByteArray();


        /* Send the response as downloadable PDF */

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=invoice.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(bytes);
    }

    @GetMapping("/sendEmail/{id}")
    public void sendMail(@PathVariable("id") Long id, HttpServletRequest request, HttpServletResponse response) throws Exception {

        UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        InvoiceDTO invoiceDTO = invoiceService.getInvoiceById(id);
        InvoiceDTO updatedInvoiceDTO = invoiceService.calculateInvoiceCost(invoiceDTO);
        Set<InvoiceProductDTO> invoiceProducts = invoiceProductService.getAllInvoiceProductsByInvoiceId(id);

        /*Create HTML using Thymeleaf template*/
        WebContext context = new WebContext(request, response, servletContext);

        context.setVariable("company", companyService.getCompanyByLoggedInUser());
        context.setVariable("invoice", updatedInvoiceDTO);
        context.setVariable("invoiceProducts", invoiceProducts);

        String companyListHTML = templateEngine.process("invoice/invoiceSuccess.html", context);

        /*Setup Source and target I/O streams*/
        ByteArrayOutputStream target = new ByteArrayOutputStream();
        ConverterProperties converterProperties = new ConverterProperties();
        converterProperties.setBaseUri("http://localhost:8080");
        /* Call convert method */
        HtmlConverter.convertToPdf(companyListHTML, target, converterProperties);

        /* extract output as bytes */
        byte[] bytes = target.toByteArray();

        // send with email
        emailSender.sendEmailWithAttachment(userPrincipal.getUsername(), "oltocoamazon@gmail.com", invoiceDTO.getInvoiceNumber(), "Your invoice ...", bytes);
    }

    // Payment Invoice PDF generator................................................................................

    @GetMapping("/generate-payment/{id}")
    public ResponseEntity<?> getPaymentPDF(@PathVariable("id") Long id, HttpServletRequest request, HttpServletResponse response) throws Exception {

        PaymentDTO paymentDTO = paymentService.getPaymentById(id);

        /*Create HTML using Thymeleaf template*/
        WebContext context = new WebContext(request, response, servletContext);

        context.setVariable("company", companyService.getCompanyByLoggedInUser());
        context.setVariable("payment", paymentDTO);

        String companyListHTML = templateEngine.process("payment/payment-success-print.html", context);

        /*Setup Source and target I/O streams*/
        ByteArrayOutputStream target = new ByteArrayOutputStream();
        ConverterProperties converterProperties = new ConverterProperties();
        converterProperties.setBaseUri("http://localhost:8080");
        /* Call convert method */
        HtmlConverter.convertToPdf(companyListHTML, target, converterProperties);

        /* extract output as bytes */
        byte[] bytes = target.toByteArray();

        /* Send the response as downloadable PDF */

        return ResponseEntity.ok()
//                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=company.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(bytes);


    }

    @GetMapping("/download-payment/{id}")
    public ResponseEntity<?> downloadPaymentPDF(@PathVariable("id") Long id, HttpServletRequest request, HttpServletResponse response) throws Exception {

        PaymentDTO paymentDTO = paymentService.getPaymentById(id);

        /*Create HTML using Thymeleaf template*/
        WebContext context = new WebContext(request, response, servletContext);

        context.setVariable("company", companyService.getCompanyByLoggedInUser());
        context.setVariable("payment", paymentDTO);

        String companyListHTML = templateEngine.process("payment/payment-success-print.html", context);

        /*Setup Source and target I/O streams*/
        ByteArrayOutputStream target = new ByteArrayOutputStream();
        ConverterProperties converterProperties = new ConverterProperties();
        converterProperties.setBaseUri("http://localhost:8080");
        /* Call convert method */
        HtmlConverter.convertToPdf(companyListHTML, target, converterProperties);

        /* extract output as bytes */
        byte[] bytes = target.toByteArray();


        /* Send the response as downloadable PDF */

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=invoice.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(bytes);
    }

    @GetMapping("/sendEmail-payment/{id}")
    public void sendMailPayment(@PathVariable("id") Long id, HttpServletRequest request, HttpServletResponse response) throws Exception {

        UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        PaymentDTO paymentDTO = paymentService.getPaymentById(id);

        /*Create HTML using Thymeleaf template*/
        WebContext context = new WebContext(request, response, servletContext);

        context.setVariable("company", companyService.getCompanyByLoggedInUser());
        context.setVariable("payment", paymentDTO);

        String companyListHTML = templateEngine.process("payment/payment-success-print.html", context);

        /*Setup Source and target I/O streams*/
        ByteArrayOutputStream target = new ByteArrayOutputStream();
        ConverterProperties converterProperties = new ConverterProperties();
        converterProperties.setBaseUri("http://localhost:8080");
        /* Call convert method */
        HtmlConverter.convertToPdf(companyListHTML, target, converterProperties);

        /* extract output as bytes */
        byte[] bytes = target.toByteArray();

        // send with email
        emailSender.sendEmailWithAttachment(userPrincipal.getUsername(), "oltocoamazon@gmail.com", ""+paymentDTO.getMonth()+" / "+paymentDTO.getYear().getYear()+" Invoice", "Your invoice ...", bytes);
    }

    @ModelAttribute
    public void addAttributes(Model model) {
        model.addAttribute("date", new Date());
        model.addAttribute("localDateTime", LocalDateTime.now());
        model.addAttribute("localDate", LocalDate.now());
        model.addAttribute("java8Instant", Instant.now());
    }
}
