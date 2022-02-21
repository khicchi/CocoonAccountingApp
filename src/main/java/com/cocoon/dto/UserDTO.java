package com.cocoon.dto;

import com.cocoon.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserDTO {

    private Long Id;
    private String firstname;
    private String lastname;
    private String password;
    private String email;
    private String phone;
    private Role role;
    private CompanyDTO company;
    private Boolean enabled;


}
