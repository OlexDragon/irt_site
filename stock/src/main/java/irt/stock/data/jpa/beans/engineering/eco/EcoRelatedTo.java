package irt.stock.data.jpa.beans.engineering.eco;

import java.io.Serializable;
import java.util.Optional;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import irt.stock.data.jpa.beans.PartNumber;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table
@Getter @Setter @EqualsAndHashCode @NoArgsConstructor
public class EcoRelatedTo implements Serializable {
	private static final long serialVersionUID = 7838146142044063810L;

	@EmbeddedId private EcoRelatedToId id;

	public EcoRelatedTo(PartNumber partNumber, Eco eco) {
		id = new EcoRelatedToId();
		id.setPartNumber(partNumber);
		id.setEco(eco);
	}

	public PartNumber getPartNumber() {
		return Optional.ofNullable(id).map(EcoRelatedToId::getPartNumber).orElse(null);
	}

	@Override
	public String toString() {
		Optional<EcoRelatedToId> ofNullable = Optional.ofNullable(id);
		return "IdEcoRelatedTo -  ECO Number: " + ofNullable.map(EcoRelatedToId::getEco).map(Eco::getNumber).orElse(null) + "; PartNumber: " + ofNullable.map(EcoRelatedToId::getPartNumber).orElse(null);
	}
}
