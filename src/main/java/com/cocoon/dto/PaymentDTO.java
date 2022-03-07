package com.cocoon.dto;

import com.cocoon.entity.payment.Institution;
import com.cocoon.enums.Months;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.Month;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PaymentDTO {

    private Long id;
    private CompanyDTO company;
    private InstitutionDTO institution;
    private Months month;
    private int amount;
    private boolean isPaid;
    private LocalDate year;
}
