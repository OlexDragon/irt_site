package irt.stock.data.jpa.beans.engineering.ecr;

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
public class EcrRelatedTo implements Serializable {
	private static final long serialVersionUID = 7838146142044063810L;

	@EmbeddedId private EcrRelatedToId id;

	public EcrRelatedTo(PartNumber partNumber, Ecr ecr) {
		id = new EcrRelatedToId();
		id.setPartNumber(partNumber);
		id.setEcr(ecr);
	}

	public PartNumber getPartNumber() {
		return Optional.ofNullable(id).map(EcrRelatedToId::getPartNumber).orElse(null);
	}

	@Override
	public String toString() {
		Optional<EcrRelatedToId> ofNullable = Optional.ofNullable(id);
		return "IdEcrRelatedTo -  ECR Number: " + ofNullable.map(EcrRelatedToId::getEcr).map(Ecr::getNumber).orElse(null) + "; PartNumber: " + ofNullable.map(EcrRelatedToId::getPartNumber).orElse(null);
	}
}
