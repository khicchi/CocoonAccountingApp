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
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class InvoiceDTO {

    private Long id;
    private String invoiceNo;
    private InvoiceStatus invoiceStatus;
    private InvoiceType invoiceType;
    private Byte enabled;
    private Company company;
    private ClientVendor clientVendor;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate invoiceDate;

    private List<ProductDTO> products;

    private int InvoiceCostWithoutTax;
    private int InvoiceCostWithTax;
    private int totalCost;

}
