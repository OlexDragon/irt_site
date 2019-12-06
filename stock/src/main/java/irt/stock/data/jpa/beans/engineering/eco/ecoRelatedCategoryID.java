package irt.stock.data.jpa.beans.engineering.eco;

import java.io.Serializable;
import java.util.Optional;

import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@Getter @Setter @NoArgsConstructor  @EqualsAndHashCode
public class ecoRelatedCategoryID implements Serializable{
	private static final long serialVersionUID = 2484243680758167752L;

	@ManyToOne
	@JoinColumn(name = "ecoNumber", referencedColumnName = "number")
	private Eco eco;

	@Enumerated(EnumType.ORDINAL)
	private EcoCategory ecoCategory;

	@Override
	public String toString() {
		return "IdEcoRelatedTo -  Eco Number: " + Optional.ofNullable(eco).map(Eco::getNumber).orElse(null) + "; Eco Category: " + Optional.ofNullable(ecoCategory).orElse(null);
	}
}
