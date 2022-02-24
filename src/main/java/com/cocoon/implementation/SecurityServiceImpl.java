package com.cocoon.implementation;

import com.cocoon.repository.UserRepo;

public class SecurityServiceImpl {

    UserRepo userRepo;

    public SecurityServiceImpl(UserRepo userRepo) {
        this.userRepo = userRepo;
    }


}
