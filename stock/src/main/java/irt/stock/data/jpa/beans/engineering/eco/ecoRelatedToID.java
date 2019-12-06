package irt.stock.data.jpa.beans.engineering.eco;

import java.io.Serializable;
import java.util.Optional;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import irt.stock.data.jpa.beans.PartNumber;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@Getter @Setter @NoArgsConstructor  @EqualsAndHashCode
public class ecoRelatedToID implements Serializable{
	private static final long serialVersionUID = 1068574119677167878L;

	@ManyToOne
	@JoinColumn(name = "componentId", referencedColumnName = "id")
	private PartNumber partNumber;

	@ManyToOne
	@JoinColumn(name = "ecoNumber", referencedColumnName = "number")
	private Eco eco;

	@Override
	public String toString() {
		return "IdEcoRelatedTo -  ECO Number: " + Optional.ofNullable(eco).map(Eco::getNumber).orElse(null) + "; PartNumber: " + Optional.ofNullable(partNumber).orElse(null);
	}
}
