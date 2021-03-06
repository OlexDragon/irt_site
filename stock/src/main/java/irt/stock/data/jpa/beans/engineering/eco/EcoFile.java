package irt.stock.data.jpa.beans.engineering.eco;

import java.io.Serializable;
import java.util.Optional;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import irt.stock.data.jpa.beans.engineering.ecr.EcrCategory.Category;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "eco_files")
@Getter @Setter @NoArgsConstructor  @EqualsAndHashCode
public class EcoFile implements Serializable {

	private static final long serialVersionUID = -2339533054583331336L;

	public EcoFile(EcoStatus ecoStatus, Category fileCategory, String fileName) {
		this.ecoStatus = ecoStatus;
		this.fileCategory = fileCategory;
		this.fileName = fileName;
		status = Status.ADDED;
	}

	@Id @GeneratedValue
	private Long id;

	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "ecoStatusId", referencedColumnName = "id",  nullable = true, updatable = false)
	private EcoStatus ecoStatus;

	private Category fileCategory;
	private String fileName;
	private Status status;

	@Override
	public String toString() {
		return "IdEcoRelatedTo -  EcoStatus id: " + Optional.ofNullable(ecoStatus).map(EcoStatus::getId).orElse(null) + "; id: " + id + "; fileRelation: " + fileCategory + "; file name: " + fileName;
	}

	public enum Status {
		DELETED,
		ADDED
	}
}
