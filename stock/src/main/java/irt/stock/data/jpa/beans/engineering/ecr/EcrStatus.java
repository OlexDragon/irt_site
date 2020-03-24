package irt.stock.data.jpa.beans.engineering.ecr;

import static irt.stock.IrtStockApp.dateFormat;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;

import irt.stock.data.jpa.beans.User;
import irt.stock.data.jpa.beans.engineering.EngineeringChangeStatus;
import irt.stock.data.jpa.beans.engineering.StatusOf;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter @NoArgsConstructor
public class EcrStatus implements EngineeringChangeStatus{

	public EcrStatus(Status status, Ecr ecr, User changedBy) {
		this.status = status;
		this.ecr = ecr;
		this.changedBy = changedBy;
	}

	@Id @GeneratedValue
	private long id;

	@Column(nullable = false, updatable = false)
	private Status status;

	@Column(nullable = false, insertable = false, updatable = false)
	@CreationTimestamp
	private Timestamp date;

	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "ecrNumber", referencedColumnName = "number", nullable = false, updatable = false)
	private Ecr ecr;

	@ManyToOne(optional = false)
	@JoinColumn(name = "changedBy", referencedColumnName = "id", nullable = false, updatable = false)
	private User changedBy;

	@OneToOne(mappedBy = "ecrStatus", cascade = CascadeType.REMOVE)
	private EcrComment comment;

	@OneToMany(fetch = FetchType.EAGER, mappedBy = "ecrStatus", cascade = CascadeType.REMOVE)
	private List<EcrFile> files;

	@OneToOne(mappedBy = "ecrStatus", cascade = CascadeType.REMOVE)
	private EcrForwardTo forwardTo;

	public void addFile(EcrFile ecrFile) {
		List<EcrFile> files = getFiles();
		if(files==null) {
			files = new ArrayList<EcrFile>();
			setFiles(files);
		}

		files.add(ecrFile);
	}

	@Override
	public String toString() {
		User changedBy = getChangedBy();
//		String forward = Optional.ofNullable(forwardTo).map(ft->" forvard to " + ft.getUser().getFirstname() + " " + ft.getUser().getLastname()).orElse("");
		String forward = Optional.ofNullable(forwardTo).map(ft->" to " + ft.getUser().getFirstname() + " " + ft.getUser().getLastname()).orElse("");
		return getStatus() + " by " + changedBy.getFirstname() + " " + changedBy.getLastname() + " on " + dateFormat.format(getDate()) + forward;
	}

	// ***** enum Status *****
	public enum Status implements StatusOf{

	CREATED,			// Created
	FORWARDED,			// Forwarded to some engineer to create ECO
	LINKED,				// Linked to new ECO
	REJECTED,			// Rejected by top engineer
//	DELAYED			// Delayed for a while
	;
}
}
