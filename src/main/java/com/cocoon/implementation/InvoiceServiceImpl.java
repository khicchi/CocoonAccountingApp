package com.cocoon.implementation;

import com.cocoon.dto.InvoiceDTO;
import com.cocoon.dto.InvoiceProductDTO;
import com.cocoon.dto.UserDTO;
import com.cocoon.entity.Company;
import com.cocoon.entity.Invoice;
import com.cocoon.entity.InvoiceProduct;
import com.cocoon.entity.User;
import com.cocoon.enums.InvoiceStatus;
import com.cocoon.enums.InvoiceType;
import com.cocoon.repository.InvoiceProductRepo;
import com.cocoon.repository.InvoiceRepository;
import com.cocoon.service.InvoiceProductService;
import com.cocoon.service.InvoiceService;
import com.cocoon.service.UserService;
import com.cocoon.util.MapperUtil;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class InvoiceServiceImpl implements InvoiceService {

    private final MapperUtil mapperUtil;
    private final InvoiceRepository invoiceRepository;
    private final InvoiceProductRepo invoiceProductRepo;
    private final InvoiceProductService invoiceProductService;
    private final UserService userService;

    public InvoiceServiceImpl(MapperUtil mapperUtil, InvoiceRepository invoiceRepository, InvoiceProductRepo invoiceProductRepo, InvoiceProductService invoiceProductService, UserService userService) {
        this.mapperUtil = mapperUtil;
        this.invoiceRepository = invoiceRepository;
        this.invoiceProductRepo = invoiceProductRepo;
        this.invoiceProductService = invoiceProductService;
        this.userService = userService;
    }

    @Override
    public InvoiceDTO save(InvoiceDTO dto) {

        Invoice invoice = mapperUtil.convert(dto, new Invoice());
        invoice.setInvoiceStatus(invoice.getInvoiceType().equals(InvoiceType.SALE) ? InvoiceStatus.PENDING : InvoiceStatus.APPROVED);
        invoice.setEnabled((byte) 1);
        invoice.setCompany(getCompanyByLoggedInUser());
        Invoice savedInvoice = invoiceRepository.save(invoice);
        InvoiceDTO savedInvoiceDTO = mapperUtil.convert(savedInvoice, new InvoiceDTO());
        dto.getInvoiceProduct().forEach(obj -> obj.setInvoiceDTO(savedInvoiceDTO));
        invoiceProductService.save(dto.getInvoiceProduct());
        return savedInvoiceDTO;
    }

    @Override
    public InvoiceDTO update(InvoiceDTO dto, Long id) {

        Invoice convertedInvoice = mapperUtil.convert(dto, new Invoice());
        Invoice invoice = invoiceRepository.getById(id);
        convertedInvoice.setInvoiceNumber(invoice.getInvoiceNumber());
        convertedInvoice.setCompany(invoice.getCompany());
        convertedInvoice.setInvoiceType(invoice.getInvoiceType());
        convertedInvoice.setEnabled(invoice.getEnabled());
        convertedInvoice.setInvoiceDate(invoice.getInvoiceDate());
        Invoice savedInvoice = invoiceRepository.save(convertedInvoice);
        return mapperUtil.convert(savedInvoice, new InvoiceDTO());
    }

    @Override
    public List<InvoiceDTO> getAllInvoices() {
        List<Invoice> invoices = invoiceRepository.findInvoiceByCompany(getCompanyByLoggedInUser());
        return invoices.stream().map(invoice -> mapperUtil.convert(invoice, new InvoiceDTO())).collect(Collectors.toList());
    }

    @Override
    public InvoiceDTO getInvoiceById(Long id) {
        Invoice invoice = invoiceRepository.getById(id);
        return mapperUtil.convert(invoice, new InvoiceDTO());
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
                .findInvoicesByCompanyAndInvoiceType(getCompanyByLoggedInUser(), invoiceType)
                .stream()
                .sorted(Comparator.comparing(Invoice::getInvoiceNumber).reversed())
                .collect(Collectors.toList());
        if (invoiceList.size() == 0) {
            if (invoiceType.name().equals("PURCHASE")) return "P-INV001";
            else return "S-INV001";
        }
        int number = Integer.parseInt(invoiceList.get(0).getInvoiceNumber().substring(5)) + 1;
        if (invoiceType.name().equals("PURCHASE")) return "P-INV" + String.format("%03d", number);
        else return "S-INV" + String.format("%03d", number);
    }

    @Override
    public List<InvoiceDTO> getAllInvoicesSorted() {

        List<Invoice> invoices = invoiceRepository.findInvoiceByCompany(getCompanyByLoggedInUser());
        invoices.sort((o2, o1) -> Integer.compare(o2.getInvoiceDate().compareTo(o1.getInvoiceDate()), 0));
        return invoices.stream().limit(3).map(invoice -> mapperUtil.convert(invoice, new InvoiceDTO())).collect(Collectors.toList());

    }

    @Override
    public List<InvoiceDTO> getAllInvoicesByCompanyAndType(InvoiceType type) {
        List<Invoice> invoices = invoiceRepository.findInvoicesByCompanyAndInvoiceType(getCompanyByLoggedInUser(), type);
        return invoices.stream().map(obj -> mapperUtil.convert(obj, new InvoiceDTO())).collect(Collectors.toList());
    }

    @Override
    public Map<String, Integer> calculateTotalProfitLoss() {

        Map<String, Integer> map = new HashMap<>();

        List<InvoiceDTO> saleInvoiceDTOS = getAllInvoicesByCompanyAndType(InvoiceType.SALE);
        List<InvoiceDTO> approvedSaleInvoiceDTOS = saleInvoiceDTOS.stream().filter(obj -> obj.getInvoiceStatus()==InvoiceStatus.APPROVED).collect(Collectors.toList());
        List<Set<InvoiceProductDTO>> allSoldInvoiceProducts = approvedSaleInvoiceDTOS.stream().map(obj -> invoiceProductService.getAllInvoiceProductsByInvoiceId(obj.getId())).collect(Collectors.toList());

        List<InvoiceDTO> purchaseInvoices = getAllInvoicesByCompanyAndType(InvoiceType.PURCHASE);
        List<Set<InvoiceProductDTO>> allPurchasedInvoiceProducts = purchaseInvoices.stream().map(obj -> invoiceProductService.getAllInvoiceProductsByInvoiceId(obj.getId())).collect(Collectors.toList());

        int totalIncomeFromSoldProductsWithoutTax = allSoldInvoiceProducts.stream()
                .mapToInt(this::calculateCostWithoutTax)
                .sum();

        int totalIncomeFromSoldProductsWithTax = allSoldInvoiceProducts.stream()
                .mapToInt(this::calculateCostWithTax)
                .sum();

        int totalSpendForPurchasedProductsWithoutTax = allPurchasedInvoiceProducts.stream()
                .mapToInt(this::calculateCostWithoutTax)
                .sum();

        int totalSpendForPurchasedProductsWithTax = allPurchasedInvoiceProducts.stream()
                .mapToInt(this::calculateCostWithTax)
                .sum();

        map.put("totalCost", totalSpendForPurchasedProductsWithoutTax);
        map.put("totalTax", totalSpendForPurchasedProductsWithTax - totalSpendForPurchasedProductsWithoutTax);
        map.put("totalSales", totalIncomeFromSoldProductsWithTax);
        map.put("totalEarning", totalIncomeFromSoldProductsWithTax - totalSpendForPurchasedProductsWithTax );

        return map;
    }

    @Override
    public InvoiceDTO calculateInvoiceCost(InvoiceDTO currentDTO) {

        Set<InvoiceProductDTO> invoiceProducts = invoiceProductService.getAllInvoiceProductsByInvoiceId(currentDTO.getId());
        int costWithoutTax = calculateCostWithoutTax(invoiceProducts);
        currentDTO.setInvoiceCostWithoutTax(costWithoutTax);
        int costWithTax = calculateCostWithTax(invoiceProducts);
        currentDTO.setTotalCost(costWithTax);
        currentDTO.setInvoiceCostWithTax(costWithTax - costWithoutTax);

        return currentDTO;
    }

    private int calculateCostWithoutTax(Set<InvoiceProductDTO> products) {
        int result = 0;
        for (InvoiceProductDTO product : products) {
            result += (product.getPrice() * product.getQty());
        }
        return result;
    }

    private int calculateCostWithTax(Set<InvoiceProductDTO> products) {
        int result = 0;
        for (InvoiceProductDTO product : products) {
            result += (product.getPrice() * product.getQty()) + (product.getPrice() * product.getQty() * product.getTax() * 0.01);
        }
        return result;
    }

    private Company getCompanyByLoggedInUser(){
        var currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        UserDTO userDTO = userService.findByEmail(currentUserEmail);
        User user = mapperUtil.convert(userDTO, new User());
        return user.getCompany();
    }


}
