package com.cocoon.implementation;

import com.cocoon.dto.InvoiceDTO;
import com.cocoon.entity.Invoice;
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
        Invoice invoice = mapperUtil.convert(dto,new Invoice());
        invoiceRepository.save(invoice);
    }

    @Override
    public List<InvoiceDTO> getAllInvoices() {
        List<Invoice> invoices = invoiceRepository.findAll();
        return invoices.stream().map(invoice -> mapperUtil.convert(invoice, new InvoiceDTO())).collect(Collectors.toList());
    }

    @Override
    public List<InvoiceDTO> getInvoiceById(Long id) {
        List<Invoice> invoices = invoiceRepository.findAllById(Collections.singleton(id));
        return invoices.stream().map(invoice -> mapperUtil.convert(invoice,new InvoiceDTO())).collect(Collectors.toList());
    }
}
