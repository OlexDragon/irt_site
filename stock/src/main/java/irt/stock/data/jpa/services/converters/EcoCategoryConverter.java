package irt.stock.data.jpa.services.converters;

import java.util.Optional;

import org.springframework.core.convert.converter.Converter;

import irt.stock.data.jpa.beans.engineering.eco.EcoCategory;

public class EcoCategoryConverter implements Converter<String, EcoCategory> {

	@Override
	public EcoCategory convert(String source) {
		EcoCategory[] values = EcoCategory.values();
		return Optional.ofNullable(source).filter(s->s.replaceAll("\\D", "").length()==source.length()).map(Integer::parseInt).filter(index->index<values.length).map(index->values[index]).orElse(null);
	}

}
