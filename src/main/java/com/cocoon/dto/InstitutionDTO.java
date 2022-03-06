package com.cocoon.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class InstitutionDTO {

    public InstitutionDTO(String name) {
        this.name = name;
    }

    private Long id;
    private String name;
}
