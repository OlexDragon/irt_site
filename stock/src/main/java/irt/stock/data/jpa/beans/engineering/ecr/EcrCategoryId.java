package irt.stock.data.jpa.beans.engineering.ecr;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

import irt.stock.data.jpa.beans.engineering.ecr.EcrCategory.Category;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Embeddable
@Getter @Setter @EqualsAndHashCode
public class EcrCategoryId implements Serializable{
	private static final long serialVersionUID = 4676135407037026129L;

	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "ecrNumber", referencedColumnName = "number", nullable = false, updatable = false)
	private Ecr ecr;

	@Enumerated(EnumType.ORDINAL)
	private Category category;		
}
