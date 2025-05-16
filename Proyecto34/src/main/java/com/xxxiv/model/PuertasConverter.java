package com.xxxiv.model;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class PuertasConverter implements AttributeConverter<Caracteristica.Puertas, String> {

    @Override
    public String convertToDatabaseColumn(Caracteristica.Puertas atributo) {
        return atributo != null ? atributo.getValor() : null;
    }

    @Override
    public Caracteristica.Puertas convertToEntityAttribute(String valor) {
        return valor != null ? Caracteristica.Puertas.fromValue(valor) : null;
    }
}
