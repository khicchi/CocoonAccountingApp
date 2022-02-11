package com.cocoon.implementation;

import com.cocoon.dto.InvoiceDTO;
import com.cocoon.entity.Invoice;
import com.cocoon.enums.InvoiceStatus;
import com.cocoon.enums.InvoiceType;
import com.cocoon.repository.InvoiceRepository;
import com.cocoon.service.InvoiceService;
import com.cocoon.util.MapperUtil;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class InvoiceServiceImpl implements InvoiceService {

    private final MapperUtil mapperUtil;
    private final InvoiceRepository invoiceRepository;

    public InvoiceServiceImpl(MapperUtil mapperUtil, InvoiceRepository invoiceRepository) {
        this.mapperUtil = mapperUtil;
        this.invoiceRepository = invoiceRepository;
    }

    @Override
    public void save(InvoiceDTO dto) {
        dto.setInvoiceStatus(InvoiceStatus.PENDING);
        dto.setEnabled((byte) 1);
        dto.setInvoiceType(InvoiceType.SALE);
        Invoice invoice = mapperUtil.convert(dto,new Invoice());
        invoiceRepository.save(invoice);
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
    public void update(InvoiceDTO dto) {
        Invoice invoice = invoiceRepository.getById(dto.getId());
        Invoice convertedInvoice = mapperUtil.convert(dto, new Invoice());
        convertedInvoice.setInvoiceNo(invoice.getInvoiceNo());
        convertedInvoice.setInvoiceStatus(invoice.getInvoiceStatus());
        convertedInvoice.setProducts(invoice.getProducts());
        invoiceRepository.save(convertedInvoice);
    }
}
