package com.cocoon.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import java.time.Month;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name="payment")
public class Payment extends BaseEntity{

    private String clientName;

    @Enumerated(EnumType.STRING)
    private Month month;

    private int amount;
    private boolean isPaid;

}
