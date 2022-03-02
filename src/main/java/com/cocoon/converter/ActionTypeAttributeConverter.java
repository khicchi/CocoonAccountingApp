package com.cocoon.converter;

import com.cocoon.enums.ActionType;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.stream.Stream;

@Converter(autoApply = true)
public class ActionTypeAttributeConverter implements AttributeConverter<ActionType, Long> {
    @Override
    public Long convertToDatabaseColumn(ActionType actionType) {
        if (actionType == null) return null;
        return actionType.getId();
    }

    @Override
    public ActionType convertToEntityAttribute(Long id) {
        if (id==null) return null;
        return Stream.of(ActionType.values())
                .filter(c -> c.getId().equals(id))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
