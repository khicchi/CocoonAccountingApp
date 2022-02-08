package com.cocoon.dto;

import com.cocoon.entity.Category;
import com.cocoon.entity.Company;
import com.cocoon.enums.ProductStatus;
import com.cocoon.enums.Unit;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ProductDTO {

    private Long id;
    private String name;
    private String description;
    private int qty;
    private int price;
    private Category category;
    private Unit unit;
    private int lowLimitAlert;
    private int tax;
    private Company company;
    private boolean enabled;
    private ProductStatus productStatus;
}
