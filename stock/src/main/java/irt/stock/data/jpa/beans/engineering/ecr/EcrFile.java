package irt.stock.data.jpa.beans.engineering.ecr;

import java.io.Serializable;
import java.util.Optional;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import irt.stock.data.jpa.beans.engineering.ecr.EcrCategory.Category;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "ecr_files")
@Getter @Setter @NoArgsConstructor  @EqualsAndHashCode
public class EcrFile implements Serializable {
	private static final long serialVersionUID = -2339533054583331336L;

	public EcrFile(EcrStatus ecrStatus, Category fileCategory, String fileName) {
		this.ecrStatus = ecrStatus;
		this.fileCategory = fileCategory;
		this.fileName = fileName;
	}

	@Id @GeneratedValue
	private Long id;

	@JsonIgnore
	@OneToOne
	@JoinColumn(name = "ecrStatusId", referencedColumnName = "id",  nullable = true, updatable = false)
	private EcrStatus ecrStatus;

	private Category fileCategory;
	private String fileName;

	@Override
	public String toString() {
		return "EcrFile -  Ecr Status: " + Optional.ofNullable(ecrStatus).map(EcrStatus::getStatus).orElse(null) + "; id: " + id + "; fileCategory: " + fileCategory + "; file name: " + fileName;
	}
}
