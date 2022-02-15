package com.cocoon.service;

import com.cocoon.dto.InvoiceProductDTO;

import java.util.List;
import java.util.Set;

public interface InvoiceProductService{

    InvoiceProductDTO save(InvoiceProductDTO invoiceProductDTO);
    Set<InvoiceProductDTO> save(Set<InvoiceProductDTO> invoiceProductDTOSet);
    List<InvoiceProductDTO> getAllInvoiceProductsByInvoiceId(Long id);
    List<InvoiceProductDTO> getAllInvoiceProductsByProductId(Long id);
}
