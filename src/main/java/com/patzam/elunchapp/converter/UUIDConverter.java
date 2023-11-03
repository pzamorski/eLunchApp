package com.patzam.elunchapp.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.UUID;

@Converter(autoApply = true)
public class UUIDConverter implements AttributeConverter<UUID, String> {
	@Override
	public String convertToDatabaseColumn(UUID attribute) {
		return attribute != null ? attribute.toString() : null;
	}

	@Override
	public UUID convertToEntityAttribute(String dbData) {
		return dbData != null ? UUID.fromString(dbData) : null;
	}
}
