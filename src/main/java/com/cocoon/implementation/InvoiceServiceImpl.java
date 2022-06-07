package com.cocoon.implementation;

import com.cocoon.dto.InvoiceDTO;
import com.cocoon.dto.InvoiceProductDTO;
import com.cocoon.dto.ProfitDTO;
import com.cocoon.dto.UserDTO;
import com.cocoon.entity.Company;
import com.cocoon.entity.Invoice;
import com.cocoon.entity.InvoiceProduct;
import com.cocoon.entity.User;
import com.cocoon.entity.jpa_customization.IInvoiceForDashBoard;
import com.cocoon.enums.InvoiceStatus;
import com.cocoon.enums.InvoiceType;
import com.cocoon.repository.InvoiceProductRepository;
import com.cocoon.repository.InvoiceRepository;
import com.cocoon.service.InvoiceProductService;
import com.cocoon.service.InvoiceService;
import com.cocoon.service.UserService;
import com.cocoon.util.MapperUtil;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class InvoiceServiceImpl implements InvoiceService {

    private final MapperUtil mapperUtil;
    private final InvoiceRepository invoiceRepository;
    private final InvoiceProductRepository invoiceProductRepository;
    private final InvoiceProductService invoiceProductService;
    private final UserService userService;

    public InvoiceServiceImpl(MapperUtil mapperUtil, InvoiceRepository invoiceRepository, InvoiceProductRepository invoiceProductRepository, InvoiceProductService invoiceProductService, UserService userService) {
        this.mapperUtil = mapperUtil;
        this.invoiceRepository = invoiceRepository;
        this.invoiceProductRepository = invoiceProductRepository;
        this.invoiceProductService = invoiceProductService;
        this.userService = userService;
    }

    @Override
    public InvoiceDTO save(InvoiceDTO dto) {

        Invoice invoice = mapperUtil.convert(dto, new Invoice());
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
        convertedInvoice.setInvoiceStatus(invoice.getInvoiceStatus());
        convertedInvoice.setCompany(invoice.getCompany());
        convertedInvoice.setInvoiceType(invoice.getInvoiceType());
        convertedInvoice.setEnabled(invoice.getEnabled());
        convertedInvoice.setInvoiceDate(invoice.getInvoiceDate());
        Invoice savedInvoice = invoiceRepository.save(convertedInvoice);
        return mapperUtil.convert(savedInvoice, new InvoiceDTO());
    }

    @Override
    public InvoiceDTO getInvoiceById(Long id) {
        Invoice invoice = invoiceRepository.getById(id);
        return mapperUtil.convert(invoice, new InvoiceDTO());
    }

    @Override
    public void deleteInvoiceById(Long id) {
        Invoice invoice = invoiceRepository.getById(id);
        Set<InvoiceProduct> invoiceProducts = invoiceProductRepository.findAllByInvoiceId(invoice.getId());
        invoiceProducts.stream().peek(obj -> obj.setIsDeleted(true)).forEach(invoiceProductRepository::save);
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
    public List<InvoiceDTO> getAllInvoicesByCompanyAndType(InvoiceType type) {
        List<Invoice> invoices = invoiceRepository.findInvoicesByCompanyAndInvoiceType(getCompanyByLoggedInUser(), type);
        List<InvoiceDTO> invoiceDTOS = invoices.stream().map(obj -> mapperUtil.convert(obj, new InvoiceDTO())).collect(Collectors.toList());
        return invoiceDTOS.stream().map(this::calculateInvoiceCost).collect(Collectors.toList());
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
    // Dashboard Items -----------------------------------------------------//
    @Override
    public Map<String, Integer> calculateTotalProfitLoss() {

        Map<String, Integer> map = new HashMap<>();

//        int totalSpendForPurchasedProductsWithoutTax= invoiceProductRepository.invoiceProductRepository.findAllByProfitEquals(0).stream().mapToInt(p-> p.getPrice()*p.getQty()).sum();
//        int totalTaxForPurchasedProducts=invoiceProductRepository.findAllByProfitEquals(0).stream().mapToInt(p-> p.getTax()*p.getQty()).sum();
//        int totalIncomeFromSoldProductsWithoutTax=invoiceProductRepository.findAllByProfitIsGreaterThanOrProfitIsLessThan(0).stream().mapToInt(p-> p.getPrice()*p.getQty()).sum();
//        int totalProfit=invoiceProductRepository.findAllByProfitIsGreaterThanOrProfitIsLessThan(0).stream().mapToInt(InvoiceProduct::getProfit).sum();
//
//        map.put("totalCost", totalSpendForPurchasedProductsWithoutTax+totalTaxForPurchasedProducts );
//        map.put("totalTax",  totalTaxForPurchasedProducts);
//        map.put("totalSales", totalIncomeFromSoldProductsWithoutTax);
//        map.put("totalEarning", totalProfit);
        map.put("p",0);

        return map;
    }

    // Profit Report  -----------------------------------------------------//
    @Override
    public List<ProfitDTO> getProfitReport() {

        List<ProfitDTO> list=new ArrayList<>();

        List<Invoice> invoices=invoiceRepository.findInvoicesByCompanyAndInvoiceType(getCompanyByLoggedInUser(),InvoiceType.SALE);
        List<InvoiceProduct> profitList= (List<InvoiceProduct>) invoices.stream().map(invoice -> {
            return invoiceProductRepository.findAllByInvoice(invoice);
        });


       profitList.stream().forEach(p->{
            int productProfit=profitList.stream()
                    .filter(product->product.getName().equals(p.getName()))
                    .mapToInt(InvoiceProduct::getProfit).sum();
            int productQuantity=profitList.stream()
                    .filter(product->product.getName().equals(p.getName()))
                    .mapToInt(InvoiceProduct::getQty).sum();
            list.add(new ProfitDTO(p.getName(),productQuantity,productProfit));

        });
        return list;
    }

    // Get top 3 invoices section----------------------------------------------------------------------------------------

    @Override
    public List<IInvoiceForDashBoard> getDashboardInvoiceTop3(Long companyId) {
        List<IInvoiceForDashBoard> invoiceForDashBoards = invoiceRepository.getDashboardInvoiceTop3Interface(companyId, companyId);
        return invoiceForDashBoards;
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
