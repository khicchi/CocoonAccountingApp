package com.cocoon.entity;

import com.cocoon.enums.ProductStatus;
import com.cocoon.enums.Unit;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name="product")
public class Product extends BaseEntity implements Serializable {

    private String name;
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    private int qty;
    private int price;

    @Enumerated(EnumType.STRING)
    private Unit unit;

    private int lowLimitAlert;
    private int tax;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    private Company company;

    private Byte enabled;

    @Enumerated(EnumType.STRING)
    private ProductStatus productStatus;

    @ManyToMany
    @JoinTable(name = "invoice_product_rel",
               joinColumns = {@JoinColumn(name = "invoice_id")},
               inverseJoinColumns = {@JoinColumn(name = "product_id")})
    private Collection<Invoice> invoices;

}
