package com.cocoon.entity;

import com.cocoon.enums.InvoiceStatus;
import com.cocoon.enums.InvoiceType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "invoice")
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class Invoice extends BaseEntity implements Serializable {

    private String invoiceNo; //TODO - invoice number will be evaluated somewhere else...

    @Enumerated(EnumType.STRING)
    private InvoiceStatus invoiceStatus;

    @Enumerated(EnumType.STRING)
    private InvoiceType invoiceType;

    @Column(columnDefinition = "DATE")
    private LocalDate invoiceDate;

    private Byte enabled;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    private Company company;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sptable_id") //TODO foreign key will be replaced with "client_vendor_id"....
    private ClientVendor clientVendor;

    @ManyToMany(mappedBy = "invoices",cascade = CascadeType.MERGE)
    private Set<Product> products = new HashSet<>();



}
