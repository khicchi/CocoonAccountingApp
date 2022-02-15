package com.cocoon.entity;

import com.cocoon.enums.ProductStatus;
import com.cocoon.enums.Unit;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Where;

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
@Where(clause = "is_deleted=false")
public class Product extends BaseEntity implements Serializable {

    private String name;
    private String description;
    private int qty;
    private int price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

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
/*
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "invoice_product_id")
    private InvoiceProduct invoiceProduct;
*/
}
