package com.cocoon.entity;

import com.cocoon.enums.ProductStatus;
import com.cocoon.enums.Unit;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name="product")
public class Product extends BaseEntity{

    private String name;
    private String description;
    private int qty;
    private int price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    private Unit unit;
    private int lowLimitAlert;
    private int tax;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    private Company company;

    private boolean enabled;

    @Enumerated(EnumType.STRING)
    private ProductStatus productStatus;

}
