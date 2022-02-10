package com.cocoon.dto;

import lombok.*;

@NoArgsConstructor
@Getter
@Setter
@Builder
@AllArgsConstructor
public class CategoryDTO {
    private Long id;
    private String description;
    private int companyId;
    private boolean enabled;
}
