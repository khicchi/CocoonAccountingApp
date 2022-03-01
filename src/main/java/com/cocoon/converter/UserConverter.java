package com.cocoon.converter;

import com.cocoon.dto.UserDTO;
import com.cocoon.service.UserService;
import lombok.SneakyThrows;
import org.springframework.boot.context.properties.ConfigurationPropertiesBinding;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
@ConfigurationPropertiesBinding
public class UserConverter implements Converter<String, UserDTO> {

    UserService userService;

    public UserConverter(@Lazy UserService userService) {
        this.userService = userService;
    }

    @SneakyThrows
    @Override
    public UserDTO convert(String id) {
        if (id.equals("")) return null;
        return userService.findById(Long.parseLong(id));
    }
}
