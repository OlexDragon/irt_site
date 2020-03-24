package irt.stock.data.jpa.beans.engineering.ecr;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "ecr_categories")
@Getter @Setter @NoArgsConstructor @EqualsAndHashCode
public class EcrCategory implements Serializable{
	private static final long serialVersionUID = 2175417557423496456L;

	public EcrCategory(Category category, Ecr ecr) {
		id = new EcrCategoryId();
		id.setEcr(ecr);
		id.setCategory(category);
	}

	@EmbeddedId private EcrCategoryId id;

	public Category getCategory() {
		return id.getCategory();
	}

	@Override
	public String toString() {
		return "IdEcrRelatedTo -  Ecr Number: " + Optional.ofNullable(id).map(EcrCategoryId::getEcr).map(Ecr::getNumber).orElse(null) + "; Ecr Category: " + Optional.ofNullable(id).map(EcrCategoryId::getCategory).orElse(null);
	}

	// ***** enum Category *****
	public enum Category {

		MECHANICAL_DESIGN	("Mechanical Design"),
		ELECTRICAL_DESIGN	("Electrical Design"),
		PCB_DESIGN			("PCB Design"),
		SOFTWARE_DESIGN		("Software Design"),
		BOM					("BOM"),
		REWORK				("Rework"),
		BATABASE_CHANGE		("Database Change");

		private final String description;

		private Category(String description) {
			this.description = description;
		}

		public String getDescription() {
			return description;
		}

		public static List<Category> valuesOf(int... array) {
			return IntStream.of(array)
					.mapToObj(
							id->
							Stream.of(values())
							.filter(category->category.ordinal()==id)
							.findAny().orElse(null))
					.collect(Collectors.toList());
		}
	}
}
