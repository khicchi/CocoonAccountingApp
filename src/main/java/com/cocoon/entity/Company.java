package com.cocoon.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "company")
public class Company extends BaseEntity {

    private String title;
    private String address1;
    private String address2;
    private String zip;
    private String representative;
    private String email;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate establishmentDate;
    private Byte enabled;
    private String phone;

    @OneToMany(mappedBy = "company", fetch = FetchType.LAZY)
    private List<User> user;

    @OneToMany(mappedBy = "company", fetch = FetchType.LAZY)
    private List<Category> categories;

    @ManyToOne
    @JoinColumn(name = "state_id")
    private State state;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "company")
    private List<Client> client;

}
