package com.airline.crewmanagement.entity;

import java.time.DayOfWeek;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.stream.Collectors;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class EnumSetConverter implements AttributeConverter<EnumSet<DayOfWeek>, String> {

    @Override
    public String convertToDatabaseColumn(EnumSet<DayOfWeek> attribute) {
        return attribute.stream()
                .map(Enum::name)
                .collect(Collectors.joining(","));
    }

    @Override
    public EnumSet<DayOfWeek> convertToEntityAttribute(String dbData) {
        String[] values = dbData.split(",");
        return EnumSet.copyOf(Arrays.stream(values)
                .map(DayOfWeek::valueOf)
                .collect(Collectors.toSet()));
    }
}
