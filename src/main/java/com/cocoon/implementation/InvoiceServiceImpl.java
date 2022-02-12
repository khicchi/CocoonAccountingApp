package com.cocoon.implementation;

import com.cocoon.dto.InvoiceDTO;
import com.cocoon.entity.Company;
import com.cocoon.entity.Invoice;
import com.cocoon.entity.InvoiceNumber;
import com.cocoon.enums.InvoiceStatus;
import com.cocoon.enums.InvoiceType;
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

    private static final AtomicInteger count = new AtomicInteger(1);
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
        dto.setInvoiceStatus(InvoiceStatus.PENDING);
        dto.setEnabled((byte) 1);
        dto.setInvoiceType(InvoiceType.SALE);
        invoiceNumberRepo.save(new InvoiceNumber(count.getAndIncrement()));
        Invoice invoice = mapperUtil.convert(dto,new Invoice());
        return mapperUtil.convert(invoiceRepository.save(invoice), new InvoiceDTO());
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
    public void update(InvoiceDTO dto, Long id) {


        Invoice convertedInvoice = mapperUtil.convert(dto, new Invoice());
        Invoice invoice = invoiceRepository.getById(id);
        convertedInvoice.setInvoiceNo(invoice.getInvoiceNo());
        convertedInvoice.setInvoiceStatus(invoice.getInvoiceStatus());
        convertedInvoice.setProducts(invoice.getProducts());
        invoiceRepository.save(convertedInvoice);
    }

    @Override
    public String getInvoiceNumber(){
        return "INV-"+count;
    }


}
