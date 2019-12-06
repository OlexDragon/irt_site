package irt.stock.data.jpa.services.converters;

import java.util.Optional;

import org.springframework.core.convert.converter.Converter;

import irt.stock.data.jpa.beans.engineering.eco.EcoOption;

public class EcoOptionConverter implements Converter<String, EcoOption>{

	@Override
	public EcoOption convert(String source) {
		EcoOption[] values = EcoOption.values();
		return Optional.ofNullable(source).filter(s->s.replaceAll("\\D", "").length()==source.length()).map(Integer::parseInt).filter(index->index<values.length).map(index->values[index]).orElse(null);
	}

}
