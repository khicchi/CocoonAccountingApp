package com.cocoon.service;

import com.cocoon.dto.PaymentDTO;

import java.time.LocalDate;
import java.util.List;

public interface PaymentService {

    List<PaymentDTO> getAllPaymentsByYear(int year);
    void createPaymentsIfNotExist(int year);
    PaymentDTO getPaymentById(Long id);
    PaymentDTO updatePayment(PaymentDTO paymentDTO);



}
