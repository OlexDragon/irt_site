package irt.stock.data.jpa.services.converters;

import java.util.Optional;

import org.springframework.core.convert.converter.Converter;

import irt.stock.data.jpa.beans.engineering.ecr.EcrCategory.Category;

public class EcrCategoryConverter implements Converter<String, Category> {

	@Override
	public Category convert(String source) {
		Category[] values = Category.values();
		return Optional.ofNullable(source).filter(s->s.replaceAll("\\D", "").length()==source.length()).map(Integer::parseInt).filter(index->index<values.length).map(index->values[index]).orElse(null);
	}

}
