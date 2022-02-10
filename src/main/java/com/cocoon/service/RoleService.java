package com.cocoon.service;

import com.cocoon.entity.Role;

import java.util.List;

public interface RoleService {

    Role findById(Long id);
    static List<Role> findAllRoles();
}
