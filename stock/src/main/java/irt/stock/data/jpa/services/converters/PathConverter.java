package irt.stock.data.jpa.services.converters;

import java.nio.file.Path;
import java.nio.file.Paths;

import javax.persistence.AttributeConverter;

public class PathConverter implements AttributeConverter<Path, String> {

	@Override
	public String convertToDatabaseColumn(Path attribute) {
		return attribute.toString();
	}

	@Override
	public Path convertToEntityAttribute(String dbData) {
		return Paths.get(dbData);
	}
}
