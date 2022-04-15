package com.cocoon.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Table(name = "invoice_product")
@Getter
@Setter
@Where(clause = "is_deleted=false")
public class InvoiceProduct extends BaseEntity{

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne(cascade = {CascadeType.MERGE})
    @JoinColumn(name = "invoice_id")
    private Invoice invoice;

    private String name;

    private int qty;
    private int price;
    private int tax;
    private int profit;

    public String getProductQuantityUnitText(){
        return qty + " / " + product.getUnit().getValue();
    }
}
