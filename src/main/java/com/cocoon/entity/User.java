package com.cocoon.entity;

import lombok.*;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "user")
@Where(clause = "is_deleted=false")
public class User extends BaseEntity {

    private String email;
    private String firstname;
    private String lastname;
    private String password;
    private boolean enabled;
    private String phone;

 //   @ManyToOne(fetch = FetchType.LAZY)
 //   @JoinColumn(name = "role_id")
 //   private Role role;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="company_id")
    private Company company;

}
