package com.cocoon.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Where;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="institutions")
@NoArgsConstructor
@Getter
@Setter
@Where(clause = "is_deleted=false")
public class Institution {

    @Id
    private Long id;
    private String name;

    private Boolean isDeleted=false;

    public Institution(String name) {
        this.name = name;
    }
}
