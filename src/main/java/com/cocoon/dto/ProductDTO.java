package com.cocoon.dto;

import com.cocoon.entity.Category;
import com.cocoon.entity.Company;
import com.cocoon.enums.ProductStatus;
import com.cocoon.enums.Unit;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ProductDTO {

    private Long id;
    private String name;
    private String description;
    private Category category;
    private int qty;
    private int price;
    private Unit unit;
    private int lowLimitAlert;
    private int tax;
    private Company company;
    private Byte enabled;
    private ProductStatus productStatus;
    /*
    private InvoiceProductDTO invoiceProductDTO;
*/
}
