package irt.stock.data.jpa.beans.engineering.eco;

import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "eco_categories")
@Setter @NoArgsConstructor @ToString
public class EcoRelatedCategory implements Serializable{
	private static final long serialVersionUID = 2175417557423496456L;

	@EmbeddedId private ecoRelatedCategoryID idEcoCategories;

	public ecoRelatedCategoryID getIdEcoRelatedCategory() {

		if(idEcoCategories==null)
			idEcoCategories = new ecoRelatedCategoryID();

		return idEcoCategories;
	}

	public EcoRelatedCategory setEco(Eco eco) {
		getIdEcoRelatedCategory().setEco(eco);
		return this;
	}

	public EcoRelatedCategory setCategory(EcoCategory ecoCategory) {
		getIdEcoRelatedCategory().setEcoCategory(ecoCategory);
		return this;
	}
}
