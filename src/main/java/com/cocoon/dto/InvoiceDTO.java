package com.cocoon.dto;

import com.cocoon.entity.ClientVendor;
import com.cocoon.entity.Company;
import com.cocoon.enums.InvoiceStatus;
import com.cocoon.enums.InvoiceType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class InvoiceDTO {

    private Long id;
    private String invoiceNo;
    private InvoiceStatus invoiceStatus;
    private int invoiceNumber;
    private InvoiceType invoiceType;
    private boolean enabled;
    private Company company;
    private ClientVendor clientVendor;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate invoiceDate;

}
