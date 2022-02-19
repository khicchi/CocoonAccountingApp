package com.cocoon.entity;

import com.cocoon.enums.CompanyType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Entity
@Table(name = "client_vendor")
@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
@Where(clause = "is_deleted=false")
public class Client extends BaseEntity {

    private String companyName;
    private String phone;
    private String email;

    @Enumerated(EnumType.STRING)
    private CompanyType type;
    private String zipCode;
    private String address;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "state_id")
    private State state;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    private Company company;

    private Boolean enabled;
}
