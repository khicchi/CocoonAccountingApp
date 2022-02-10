package com.cocoon.service;

import com.cocoon.dto.InvoiceDTO;

import java.util.List;

public interface InvoiceService {

    InvoiceDTO save(InvoiceDTO invoice);

    List<InvoiceDTO> getAllInvoices();

    InvoiceDTO getInvoiceById(Long id);

    void update(InvoiceDTO dto, Long id);

    String getInvoiceNumber();
}
