package com.cocoon.implementation;

import com.cocoon.dto.InvoiceDTO;
import com.cocoon.entity.Invoice;
import com.cocoon.enums.InvoiceStatus;
import com.cocoon.repository.InvoiceNumberRepo;
import com.cocoon.repository.InvoiceRepository;
import com.cocoon.service.InvoiceService;
import com.cocoon.util.MapperUtil;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public class InvoiceServiceImpl implements InvoiceService {

    private int count = 0;
    private final MapperUtil mapperUtil;
    private final InvoiceRepository invoiceRepository;
    private final InvoiceNumberRepo invoiceNumberRepo;

    public InvoiceServiceImpl(MapperUtil mapperUtil, InvoiceRepository invoiceRepository, InvoiceNumberRepo invoiceNumberRepo) {
        this.mapperUtil = mapperUtil;
        this.invoiceRepository = invoiceRepository;
        this.invoiceNumberRepo = invoiceNumberRepo;
    }

    @Override
    public InvoiceDTO save(InvoiceDTO dto) {

        Invoice invoice = mapperUtil.convert(dto,new Invoice());
        invoice.setInvoiceStatus(InvoiceStatus.PENDING);
        invoice.setEnabled((byte) 1);
        Invoice savedInvoice = invoiceRepository.save(invoice);
        count++;
        return mapperUtil.convert(savedInvoice, new InvoiceDTO());
    }

    @Override
    public List<InvoiceDTO> getAllInvoices() {
        List<Invoice> invoices = invoiceRepository.findAll();
        return invoices.stream().map(invoice -> mapperUtil.convert(invoice, new InvoiceDTO())).collect(Collectors.toList());
    }

    @Override
    public InvoiceDTO getInvoiceById(Long id) {
        Invoice invoice = invoiceRepository.getById(id);
        return mapperUtil.convert(invoice, new InvoiceDTO());
    }

    @Override
    public InvoiceDTO update(InvoiceDTO dto, Long id) {

        Invoice convertedInvoice = mapperUtil.convert(dto, new Invoice());
        Invoice invoice = invoiceRepository.getById(id);
        convertedInvoice.setInvoiceNo(invoice.getInvoiceNo());
        convertedInvoice.setInvoiceStatus(invoice.getInvoiceStatus());
        Invoice savedInvoice = invoiceRepository.save(convertedInvoice);
        return mapperUtil.convert(savedInvoice, new InvoiceDTO());
    }

    @Override
    public String getInvoiceNumber(){
        return "INV-"+ count;
    }


    @Override
    public void deleteInvoiceById(Long id) {
        Invoice invoice = invoiceRepository.getById(id);
        invoice.setIsDeleted(true);
        invoiceRepository.save(invoice);
    }


}


//            for (InvoiceDTO invoice : invoices){
//                Set<ProductDTO> products = productService.getProductsByInvoiceId(invoice.getId());
//                int costWithoutTax = products.stream().mapToInt(ProductDTO::getPrice).sum();
//                invoice.setInvoiceCostWithoutTax(costWithoutTax);
//                int costWithTax = calculateTaxedCost(products);
//                invoice.setTotalCost(costWithTax);
//                invoice.setInvoiceCostWithTax(costWithTax - costWithoutTax);
//            }

//    private int calculateTaxedCost(Set<ProductDTO> products){
////        int result = 0;
////        for (ProductDTO product : products){
////            result += product.getPrice() + (product.getPrice() * product.getTax() * 0.01);
////        }
////        return result;
////    }
