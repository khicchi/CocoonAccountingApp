package com.cocoon.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@MappedSuperclass
@EntityListeners(BaseEntityListener.class)
@Setter
@Getter
public class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "created_by", nullable = false,updatable = false)
    private Long createdBy;
    @Column(name = "created_time", nullable = false,updatable = false)
    private LocalDateTime createdTime;
    @Column(name = "updated_by", nullable = false)
    private Long updatedBy;
    @Column(name = "updated_time", nullable = false)
    private LocalDateTime updatedTime;

    private Boolean isDeleted=false;
}
