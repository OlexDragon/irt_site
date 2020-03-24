package irt.stock.data.jpa.beans.engineering.eco;

import java.io.Serializable;
import java.util.Optional;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import irt.stock.data.jpa.beans.engineering.ecr.EcrCategory.Category;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "eco_categories")
@Getter @Setter @NoArgsConstructor @EqualsAndHashCode
public class EcoCategory implements Serializable{
	private static final long serialVersionUID = 2175417557423496456L;

	public EcoCategory(Category category, Eco eco) {
		id = new EcoCategoryId();
		id.setEco(eco);
		id.setCategory(category);
	}

	@EmbeddedId private EcoCategoryId id;

	public Category getCategory() {
		return id.getCategory();
	}

	@Override
	public String toString() {
		return "IdEcoRelatedTo -  Eco Number: " + Optional.ofNullable(id).map(EcoCategoryId::getEco).map(Eco::getNumber).orElse(null) + "; Eco Category: " + Optional.ofNullable(id).map(EcoCategoryId::getCategory).orElse(null);
	}
}
