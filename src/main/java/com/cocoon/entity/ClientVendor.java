package com.cocoon.entity;

import com.cocoon.enums.CompanyType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "client_vendor_list")
@Where(clause = "is_deleted=false")
public class ClientVendor extends BaseEntity {

    private String companyName;
    private String phone;
    private String email;
    @Enumerated(EnumType.STRING)

    @ManyToOne(fetch = FetchType.LAZY)
    private Company company;

    private CompanyType type;
    private String zipCode;
    private String address;

    @ManyToOne(fetch = FetchType.LAZY)
    private State state;

    private boolean enabled;
}