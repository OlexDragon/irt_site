package irt.stock.data.jpa.beans.engineering.eco;

import java.io.Serializable;
import java.util.Optional;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "eco_files")
@Getter @Setter @NoArgsConstructor  @EqualsAndHashCode
public class EcoFile implements Serializable {
	private static final long serialVersionUID = -2339533054583331336L;

	public EcoFile(Eco eco, EcoCategory ecoRelation, String name) {
		this.eco = eco;
		this.ecoRelation = ecoRelation;
		this.name = name;
	}

	@Id @GeneratedValue
	private Long id;

	private EcoCategory ecoRelation;

	private String name;

	@ManyToOne
	@JoinColumn(name = "ecoNumber", referencedColumnName = "number")
	private Eco eco;

	@Override
	public String toString() {
		return "IdEcoRelatedTo -  Eco Number: " + Optional.ofNullable(eco).map(Eco::getNumber).orElse(null) + "; id: " + id + "; ecoRelation: " + ecoRelation + "; path: " + name;
	}
}
