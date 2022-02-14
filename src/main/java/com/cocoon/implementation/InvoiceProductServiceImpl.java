package com.cocoon.implementation;

import com.cocoon.dto.InvoiceDTO;
import com.cocoon.dto.InvoiceProductDTO;
import com.cocoon.entity.InvoiceProduct;
import com.cocoon.repository.InvoiceProductRepo;
import com.cocoon.service.InvoiceProductService;
import com.cocoon.util.MapperUtil;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class InvoiceProductServiceImpl implements InvoiceProductService {

    private final InvoiceProductRepo invoiceProductRepo;
    private final MapperUtil mapperUtil;

    public InvoiceProductServiceImpl(InvoiceProductRepo invoiceProductRepo, MapperUtil mapperUtil) {
        this.invoiceProductRepo = invoiceProductRepo;
        this.mapperUtil = mapperUtil;
    }

    @Override
    public InvoiceProductDTO save(InvoiceProductDTO invoiceProductDTO) {
        InvoiceProduct invoiceProduct = mapperUtil.convert(invoiceProductDTO, new InvoiceProduct());
        InvoiceProduct savedInvoiceProduct = invoiceProductRepo.save(invoiceProduct);
        return mapperUtil.convert(savedInvoiceProduct, new InvoiceProductDTO());
    }

    @Override
    public Set<InvoiceProductDTO> save(Set<InvoiceProductDTO> invoiceProductDTOSet) {

        return invoiceProductDTOSet
                .stream()
                .map(dto -> mapperUtil.convert(dto, new InvoiceProduct()))
                .map(invoiceProductRepo::save)
                .map(entity -> mapperUtil.convert(entity,new InvoiceProductDTO()))
                .collect(Collectors.toSet());
    }
}
