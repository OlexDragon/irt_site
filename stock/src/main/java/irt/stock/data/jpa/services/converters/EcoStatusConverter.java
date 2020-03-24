package irt.stock.data.jpa.services.converters;

import java.util.Optional;

import org.springframework.core.convert.converter.Converter;

import irt.stock.data.jpa.beans.engineering.eco.EcoStatus.Status;

public class EcoStatusConverter implements Converter<String, Status> {

	@Override
	public Status convert(String index) {
		Status[] values = Status.values();
		return Optional.ofNullable(index).map(Integer::parseInt).filter(i->i<values.length).map(i->values[i]).orElse(null);
	}

}
