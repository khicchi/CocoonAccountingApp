package com.cocoon.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.concurrent.atomic.AtomicInteger;

@Entity
@Table(name = "invoice_number")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceNumber {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "year", columnDefinition = "DATE")
    private LocalDate year;

    private int invoiceNumber;

    public InvoiceNumber(int invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }
}
