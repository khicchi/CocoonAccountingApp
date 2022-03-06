package com.cocoon.implementation;

import com.cocoon.dto.CompanyDTO;
import com.cocoon.dto.PaymentDTO;
import com.cocoon.entity.Company;
import com.cocoon.entity.Payment;
import com.cocoon.enums.Months;
import com.cocoon.repository.PaymentRepository;
import com.cocoon.service.CompanyService;
import com.cocoon.service.PaymentService;
import com.cocoon.util.MapperUtil;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import static java.time.temporal.TemporalAdjusters.firstDayOfYear;
import static java.time.temporal.TemporalAdjusters.lastDayOfYear;

@Service
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final MapperUtil mapperUtil;
    private final CompanyService companyService;

    public PaymentServiceImpl(PaymentRepository paymentRepository, MapperUtil mapperUtil, CompanyService companyService) {
        this.paymentRepository = paymentRepository;
        this.mapperUtil = mapperUtil;
        this.companyService = companyService;
    }

    @Override
    public List<PaymentDTO> getAllPaymentsByYear(int year) {

        LocalDate instance = LocalDate.now().withYear(year);
        LocalDate dateStart = instance.with(firstDayOfYear());
        LocalDate dateEnd = instance.with(lastDayOfYear());

        List<Payment> payments = paymentRepository.findAllByYearBetween(dateStart, dateEnd);
        return payments.stream().map(obj -> mapperUtil.convert(obj, new PaymentDTO())).collect(Collectors.toList());
    }

    @Override
    public void createPaymentsIfNotExist(int year) {

        LocalDate instance = LocalDate.now().withYear(year);
        LocalDate dateStart = instance.with(firstDayOfYear());
        LocalDate dateEnd = instance.with(lastDayOfYear());
        List<Payment> payments = paymentRepository.findAllByYearBetween(dateStart, dateEnd);

        CompanyDTO companyDto = companyService.getCompanyByLoggedInUser();

        if (payments.size() == 0) {
            for (Months month : Months.values()){
                Payment payment = new Payment();
                payment.setMonth(month);
                payment.setYear(LocalDate.now().withYear(year));
                payment.setPaid(false);
                payment.setAmount(250);
                payment.setCompany(mapperUtil.convert(companyDto, new Company()));
                paymentRepository.save(payment);
            }
        }

    }

    @Override
    public PaymentDTO getPaymentById(Long id) {
        Payment payment = paymentRepository.getById(id);
        return mapperUtil.convert(payment, new PaymentDTO());
    }

    @Override
    public PaymentDTO updatePayment(PaymentDTO paymentDTO) {

        Payment payment = paymentRepository.getById(paymentDTO.getId());
        payment.setInstitution(payment.getInstitution());
        return mapperUtil.convert(paymentRepository.save(payment), new PaymentDTO());
    }
}
