package com.cocoon.entity;

import lombok.Getter;

import java.util.List;

@Getter
public class InstitutionResponse {

    private Meta meta;
    private List<Institution> data;
}
