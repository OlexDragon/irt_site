package irt.stock.data.jpa.beans.engineering.eco;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public enum EcoCategory {

	MECHANICAL_DESIGN	("Mechanical Design"),
	ELECTRICAL_DESIGN	("Electrical Design"),
	PCB_DESIGN			("PCB Design"),
	SOFTWARE_DESIGN		("Software Design"),
	BOM					("BOM"),
	REWORK				("Rework"),
	BATABASE_CHANGE		("Database Change");

	private final String description;

	private EcoCategory(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

	public static List<EcoCategory> valuesOf(int... array) {
		return IntStream.of(array)
				.mapToObj(
						id->
						Stream.of(values())
						.filter(category->category.ordinal()==id)
						.findAny().orElse(null))
				.collect(Collectors.toList());
	}
}
