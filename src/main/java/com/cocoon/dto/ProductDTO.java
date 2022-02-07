package com.cocoon.dto;

import com.cocoon.entity.Category;
import com.cocoon.entity.Company;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

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
    private Category categoryId;
    private String unit;
    private int lowLimitAlert;
    private int tax;
    private Company companyId;
    private boolean enabled;
}
