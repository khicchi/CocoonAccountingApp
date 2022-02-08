package com.cocoon.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "client_vendor_list")
@NoArgsConstructor
@Getter
@Setter
public class ClientVendor extends BaseEntity {

    private String companyName;
}
