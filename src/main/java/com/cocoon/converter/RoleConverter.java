package com.cocoon.converter;

import com.cocoon.entity.Role;
import com.cocoon.repository.RoleRepo;
import org.springframework.boot.context.properties.ConfigurationPropertiesBinding;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
@ConfigurationPropertiesBinding
public class RoleConverter implements Converter<String, Role> {

    private RoleRepo roleRepo;

    public RoleConverter(@Lazy RoleRepo roleRepo) {
        this.roleRepo = roleRepo;
    }

    @Override
    public Role convert(String id) {
       return roleRepo.findRoleById(Long.parseLong(id));
    }
}
