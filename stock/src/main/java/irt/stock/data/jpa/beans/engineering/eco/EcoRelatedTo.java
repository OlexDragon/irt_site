package irt.stock.data.jpa.beans.engineering.eco;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import irt.stock.data.jpa.beans.PartNumber;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "eco_related_to")
@Setter @NoArgsConstructor @EqualsAndHashCode @ToString
public class EcoRelatedTo {

	@EmbeddedId private ecoRelatedToID idEcoRelatedTo;

	public EcoRelatedTo setPartNumber(PartNumber partNumber) {
		getIdEcoRelatedTo().setPartNumber(partNumber);
		return this;
	}

	public EcoRelatedTo setEco(Eco eco) {
		getIdEcoRelatedTo().setEco(eco);
		return this;
	}

	public ecoRelatedToID getIdEcoRelatedTo() {
		if(idEcoRelatedTo==null)
			idEcoRelatedTo = new ecoRelatedToID();
		return idEcoRelatedTo;
	}
}
