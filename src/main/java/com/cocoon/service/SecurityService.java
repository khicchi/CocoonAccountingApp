package com.cocoon.service;

import org.springframework.security.core.userdetails.*;


public interface SecurityService extends UserDetailsService {

    @Override
    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;



}
