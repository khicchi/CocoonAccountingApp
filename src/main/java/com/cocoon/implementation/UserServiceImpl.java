package com.cocoon.implementation;

import com.cocoon.dto.UserDTO;
import com.cocoon.entity.User;
import com.cocoon.exception.CocoonException;
import com.cocoon.repository.UserRepo;
import com.cocoon.service.UserService;
import com.cocoon.util.MapperUtil;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private UserRepo userRepo;
    private MapperUtil mapperUtil;
    private PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepo userRepo, MapperUtil mapperUtil, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.mapperUtil = mapperUtil;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<UserDTO> findAllUsers() {
        List<User> allUsers = userRepo.findAll();
        return allUsers.stream().map(obj -> mapperUtil.convert(obj, new UserDTO()))
                .collect(Collectors.toList());
    }

    @Override
    public UserDTO update(UserDTO userDTO) throws CocoonException {
        User updatedUser = mapperUtil.convert(userDTO, new User());
        User savedUser = userRepo.save(updatedUser);
        return mapperUtil.convert(savedUser, new UserDTO());
    }

    @Override
    public UserDTO save(UserDTO userDTO) throws CocoonException {
        User foundUser = userRepo.findByEmail(userDTO.getEmail());
        if (foundUser != null) throw new CocoonException("User already exists");
        User convertedUser = mapperUtil.convert(userDTO, new User());
        convertedUser.setPassword(passwordEncoder.encode(convertedUser.getPassword()));
        User savedUser = userRepo.save(convertedUser);
        return mapperUtil.convert(savedUser, new UserDTO());
    }

    @Override
    public UserDTO findById(Long id) throws CocoonException {
        User user = userRepo.findById(id).orElseThrow();
        if (user == null) {
            throw new CocoonException("User with " + id + " not exist");
        }
        return mapperUtil.convert(user, new UserDTO());
    }

    @Override
    public UserDTO findByEmail(String email) {
        User user = userRepo.findByEmail(email);
        return mapperUtil.convert(user, new UserDTO());
    }

    @Override
    public void delete(Long id) {
        User user = userRepo.findById(id).orElseThrow();
        user.setIsDeleted(true);
        userRepo.save(user);
    }

    @Override
    public List<UserDTO> findAllUsersForLogging() {
        List<User> allUsers = userRepo.findAll();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Set<String> roles = AuthorityUtils.authorityListToSet(authentication.getAuthorities());

        if (roles.contains("ROOT")) return allUsers.stream().map(obj -> mapperUtil.convert(obj, new UserDTO())).collect(Collectors.toList());
        else if (roles.contains("ADMIN")){
            User user = userRepo.findByEmail(authentication.getName());
            return allUsers.stream().filter(o -> o.getCompany().getId().equals(user.getCompany().getId()))
                    .map(obj -> mapperUtil.convert(obj, new UserDTO()))
                    .collect(Collectors.toList());
        }
        else return null;
    }

}
