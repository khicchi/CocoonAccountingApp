package com.cocoon.converter;

import com.cocoon.enums.ActionType;
import org.springframework.boot.context.properties.ConfigurationPropertiesBinding;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.stream.Stream;

@Component
@ConfigurationPropertiesBinding
public class ActionTypeEnumConverter implements Converter<String, ActionType> {
    @Override
    public ActionType convert(String id) {
        return Stream.of(ActionType.values())
                .filter(c -> c.getId().equals(Long.parseLong(id)))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
