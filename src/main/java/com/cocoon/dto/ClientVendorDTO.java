package com.cocoon.dto;

import com.cocoon.entity.State;
import com.cocoon.enums.CompanyType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ClientVendorDTO {

    private Long id;
    private String companyName;
    private String phone;
    private String email;
    private CompanyType type;
    private String zipCode;
    private String address;
    private State state;
    private boolean enabled;
}
