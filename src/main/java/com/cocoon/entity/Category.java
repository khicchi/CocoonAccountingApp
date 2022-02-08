package com.cocoon.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "category")
public class Category extends BaseEntity{

    private String description;
    private int companyId;
    private boolean enabled;
    private int createdBy;

    private int updatedBy;

    private LocalDateTime createdTime;
    private LocalDateTime updatedTime;
}
