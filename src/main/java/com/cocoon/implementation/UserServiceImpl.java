package com.cocoon.implementation;

import com.cocoon.dto.UserDTO;
import com.cocoon.exception.CocoonException;
import com.cocoon.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {


    @Override
    public List<UserDTO> findAllUsers() {
        return null;
    }

    @Override
    public UserDTO save(UserDTO userDTO) throws CocoonException {
        return null;
    }

    @Override
    public UserDTO findById(Long id) throws CocoonException {
        return null;
    }

    @Override
    public List<UserDTO> listAllUsersByCompanyId(Long id) {
        return null;
    }

    @Override
    public UserDTO findByEmail(String email) throws CocoonException {
        return null;
    }
}
