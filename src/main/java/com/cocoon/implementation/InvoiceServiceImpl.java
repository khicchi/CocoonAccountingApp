package com.cocoon.implementation;

import com.cocoon.dto.InvoiceDTO;
import com.cocoon.entity.Invoice;
import com.cocoon.entity.InvoiceProduct;
import com.cocoon.enums.InvoiceStatus;
import com.cocoon.enums.InvoiceType;
import com.cocoon.repository.CompanyRepo;
import com.cocoon.repository.InvoiceProductRepo;
import com.cocoon.repository.InvoiceRepository;
import com.cocoon.service.InvoiceService;
import com.cocoon.util.MapperUtil;

import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class InvoiceServiceImpl implements InvoiceService {

    private final MapperUtil mapperUtil;
    private final InvoiceRepository invoiceRepository;
    private final InvoiceProductRepo invoiceProductRepo;
    private final CompanyRepo companyRepo;

    public InvoiceServiceImpl(MapperUtil mapperUtil, InvoiceRepository invoiceRepository, InvoiceProductRepo invoiceProductRepo, CompanyRepo companyRepo) {
        this.mapperUtil = mapperUtil;
        this.invoiceRepository = invoiceRepository;
        this.invoiceProductRepo = invoiceProductRepo;
        this.companyRepo = companyRepo;
    }

    @Override
    public InvoiceDTO save(InvoiceDTO dto) {

        Invoice invoice = mapperUtil.convert(dto,new Invoice());
        invoice.setInvoiceStatus(InvoiceStatus.PENDING);
        invoice.setEnabled((byte) 1);
        invoice.setCompany(companyRepo.getById(9L));
        Invoice savedInvoice = invoiceRepository.save(invoice);
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

        convertedInvoice.setInvoiceNumber(invoice.getInvoiceNumber());
        convertedInvoice.setInvoiceStatus(invoice.getInvoiceStatus());
        Invoice savedInvoice = invoiceRepository.save(convertedInvoice);
        return mapperUtil.convert(savedInvoice, new InvoiceDTO());
    }

    @Override
    public void deleteInvoiceById(Long id) {
        Invoice invoice = invoiceRepository.getById(id);
        Set<InvoiceProduct> invoiceProducts = invoiceProductRepo.findAllByInvoiceId(invoice.getId());
        invoiceProducts.stream().peek(obj -> obj.setIsDeleted(true)).forEach(invoiceProductRepo::save);
        invoice.setIsDeleted(true);
        invoiceRepository.save(invoice);
    }

    @Override
    public String getInvoiceNumber(InvoiceType invoiceType) {
        List<Invoice> invoiceList = invoiceRepository
                .findInvoicesByCompanyAndInvoiceType(companyRepo.findById(9L).get(),invoiceType)
                .stream()
                .sorted(Comparator.comparing(Invoice::getInvoiceNumber).reversed())
                .collect(Collectors.toList());
        if (invoiceList.size() ==0) {
            if (invoiceType.name().equals("PURCHASE")) return "P-INV001";
            else return "S-INV001";
        }
        int number = Integer.parseInt(invoiceList.get(0).getInvoiceNumber().substring(5)) + 1;
        if (invoiceType.name().equals("PURCHASE")) return "P-INV" + String.format("%03d", number);
        else return "S-INV" + String.format("%03d", number);
    }

//    @Override
//    public List<InvoiceDTO> getAllInvoicesSorted() {
//        List<Invoice> invoices = invoiceRepository.findAll();
//
//        invoices.sort((o2, o1) -> o2.getInvoiceDate().compareTo(o1.getInvoiceDate()));
//
//        // get first 3
//        return invoices.stream().limit(3).map(invoice -> mapperUtil.convert(invoice, new InvoiceDTO())).collect(Collectors.toList());
//
//    }
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
