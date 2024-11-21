package com.exchange.trade.utils;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.util.Arrays;
import java.util.List;

@Converter
public class StringListConverter implements AttributeConverter<List<String>, String> {

    @Override
    public String convertToDatabaseColumn(List<String> roles) {
        if (roles == null) {
            return null;
        }
        return String.join(",", roles);
    }

    @Override
    public List<String> convertToEntityAttribute(String rolesString) {
        if (rolesString == null || rolesString.isEmpty()) {
            return List.of();
        }
        return Arrays.asList(rolesString.split(","));
    }
}
