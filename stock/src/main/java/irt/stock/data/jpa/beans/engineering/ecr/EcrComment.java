package irt.stock.data.jpa.beans.engineering.ecr;

import java.util.Optional;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

import irt.stock.data.jpa.beans.engineering.EngineeringChangeComment;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name="ecr_comments")
@Getter @Setter @NoArgsConstructor
public class EcrComment implements EngineeringChangeComment{

	public EcrComment(EcrStatus ecrStatus, String comment) {
		this.ecrStatus = ecrStatus;
		this.comment = comment;
	}

	@Id
	private Long ecrStatusId;

	@JsonIgnore
	@MapsId
	@OneToOne
	@JoinColumn(name = "ecrStatusId", referencedColumnName = "id",  nullable = true, updatable = false)
	private EcrStatus ecrStatus;

	private String comment;

	@Override
	public String toString() {
		return "EcrComment [ecr staus=" + Optional.ofNullable(ecrStatus).map(EcrStatus::getStatus).orElse(null) + ", comment=" + comment + "]";
	}
}
