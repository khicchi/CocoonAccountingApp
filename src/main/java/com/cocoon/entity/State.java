package com.cocoon.entity;

import lombok.Getter;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Getter
public class State {
    @Id
    private Long id;
    private String stateCode;
    private String stateName;
}
