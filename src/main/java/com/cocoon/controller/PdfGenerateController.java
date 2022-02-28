package com.cocoon.controller;

import com.cocoon.dto.InvoiceDTO;
import com.cocoon.dto.InvoiceProductDTO;
import com.cocoon.entity.common.UserPrincipal;
import com.cocoon.service.*;
import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.sun.istack.ByteArrayDataSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import javax.activation.DataHandler;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.ByteArrayOutputStream;
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

    public PdfGenerateController(CompanyService companyService, ServletContext servletContext, TemplateEngine templateEngine, UserService userService, InvoiceService invoiceService, InvoiceProductService invoiceProductService, EmailSender emailSender) {
        this.companyService = companyService;
        this.servletContext = servletContext;
        this.templateEngine = templateEngine;
        this.userService = userService;
        this.invoiceService = invoiceService;
        this.invoiceProductService = invoiceProductService;
        this.emailSender = emailSender;
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
        System.out.println("hi");
    }
}
