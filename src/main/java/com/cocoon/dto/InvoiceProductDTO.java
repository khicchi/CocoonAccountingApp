package com.cocoon.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class InvoiceProductDTO {

    private Long id;
    private String name;
    private int qty;
    private int price;
    private int tax;

    private ProductDTO productDTO;

    private InvoiceDTO invoiceDTO;

}
