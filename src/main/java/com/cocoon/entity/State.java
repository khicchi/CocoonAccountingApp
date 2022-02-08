package com.cocoon.entity;

import lombok.Getter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Getter
@Table(name = "state")
public class State {
    @Id
    private Long id;
    private String stateCode;
    private String stateName;
}
