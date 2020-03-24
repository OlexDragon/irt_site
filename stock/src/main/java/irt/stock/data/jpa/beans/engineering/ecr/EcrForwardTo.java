package irt.stock.data.jpa.beans.engineering.ecr;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

import irt.stock.data.jpa.beans.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name="ecr_forward_to")
@Getter @Setter @NoArgsConstructor
public class EcrForwardTo{

	public EcrForwardTo(EcrStatus ecrStatus, User user) {
		this.ecrStatus = ecrStatus;
		this.user = user;
	}

	@Id
	@Column(name = "ecrStatusId")
	private Long ecrStatusId;

	@JsonIgnore
	@MapsId
	@OneToOne
	@JoinColumn(name = "ecrStatusId", referencedColumnName = "id",  nullable = true, updatable = false)
	private EcrStatus ecrStatus;

	@ManyToOne(optional = false)
	@JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false, updatable = false)
	private User user;

	@Override
	public String toString() {
		return "forward to " + user.getFirstname() + " " + user.getLastname();
	}
}
