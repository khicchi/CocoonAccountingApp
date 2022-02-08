package com.cocoon.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "category")
public class Category extends BaseEntity {

    private String description;

    @JoinColumn(name = "company_id",nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Company company;

    private boolean enabled;


}
