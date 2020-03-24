package irt.stock.data.jpa.beans.engineering.eco;

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

@Entity(name="eco_comments")
@Getter @Setter @NoArgsConstructor
public class EcoComment implements EngineeringChangeComment{

	public EcoComment(EcoStatus ecoStatus, String comment) {
		this.ecoStatus = ecoStatus;
		this.comment = comment;
	}

	@Id
	private Long ecoStatusId;

	@JsonIgnore
	@MapsId
	@OneToOne
	@JoinColumn(name = "ecoStatusId", referencedColumnName = "id",  nullable = true, updatable = false)
	private EcoStatus ecoStatus;

	private String comment;

	@Override
	public String toString() {
		return "EcoComment [eco staus=" + Optional.ofNullable(ecoStatus).map(EcoStatus::getStatus).orElse(null) + ", comment=" + comment + "]";
	}
}
