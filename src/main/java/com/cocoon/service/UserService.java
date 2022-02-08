package com.cocoon.service;

import com.cocoon.dto.UserDTO;
import com.cocoon.exception.CocoonException;

import java.util.List;

public interface UserService {

    List<UserDTO> findAllUsers();

    UserDTO save(UserDTO userDTO) throws CocoonException;
    UserDTO findById(Long id) throws CocoonException;

    List<UserDTO> listAllUsersByCompanyId(Long id);

    UserDTO findByEmail(String email) throws CocoonException;


}
