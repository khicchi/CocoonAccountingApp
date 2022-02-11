package com.cocoon.implementation;

import com.cocoon.entity.Role;
import com.cocoon.repository.RoleRepo;
import com.cocoon.service.RoleService;
import com.cocoon.util.MapperUtil;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class RoleServiceImpl implements RoleService {

    private RoleRepo roleRepo;
    private MapperUtil mapperUtil;

    public RoleServiceImpl(RoleRepo roleRepo, MapperUtil mapperUtil) {
        this.roleRepo = roleRepo;
        this.mapperUtil = mapperUtil;
    }

    @Override
    public Role findById(Long id) {
        Optional<Role> role = roleRepo.findById(id);
        return mapperUtil.convert(role,new Role());
    }


    @Override
    public List<Role> findAllRoles() {
        List<Role> allRoles = roleRepo.findAll();
        return allRoles.stream().map(obj -> mapperUtil.convert(obj, new Role()))
                .collect(Collectors.toList());
    }

}