package irt.stock.data.jpa.beans.engineering.ecr;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

import irt.stock.data.jpa.beans.PartNumber;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@Getter @Setter @EqualsAndHashCode @NoArgsConstructor
public class EcrRelatedToId implements Serializable {
	private static final long serialVersionUID = -3486026222068047980L;

	@ManyToOne
	@JoinColumn(name = "componentId", referencedColumnName = "id")
	private PartNumber partNumber;

	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "ecrNumber", referencedColumnName = "number", nullable = false, updatable = false)
	private Ecr ecr;

}
