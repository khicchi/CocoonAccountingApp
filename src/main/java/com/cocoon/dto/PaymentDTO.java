package com.cocoon.dto;

import com.cocoon.enums.Months;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import yapily.sdk.Institution;

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
