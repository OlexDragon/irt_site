package irt.stock.data.jpa.beans.engineering.eco;

import static irt.stock.IrtStockApp.dateFormat;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

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
import irt.stock.data.jpa.beans.engineering.ecr.EcrForwardTo;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter @NoArgsConstructor
public class EcoStatus implements EngineeringChangeStatus {

	public EcoStatus(Status status, Eco eco, User changedBy) {
		this.status = status;
		this.eco = eco;
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
	@JoinColumn(name = "ecoNumber", referencedColumnName = "number", nullable = false, updatable = false)
	private Eco eco;

	@ManyToOne(optional = false)
	@JoinColumn(name = "changedBy", referencedColumnName = "id", nullable = false, updatable = false)
	private User changedBy;

	@OneToOne(mappedBy = "ecoStatus", cascade = CascadeType.REMOVE)
	private EcoComment comment;

	@OneToMany(fetch = FetchType.EAGER, mappedBy = "ecoStatus", cascade = CascadeType.REMOVE)
	private List<EcoFile> files;

	public void addFile(EcoFile ecrFile) {
		List<EcoFile> files = getFiles();
		if(files==null) {
			files = new ArrayList<EcoFile>();
			setFiles(files);
		}

		files.add(ecrFile);
	}

	@Override
	public String toString() {
		User changedBy = getChangedBy();
		return getStatus() + " by " + changedBy.getFirstname() + " " + changedBy.getLastname() + " on " + dateFormat.format(getDate());
	}

	public enum Status implements StatusOf{
		CREATED,			//Created by engineer
		UPDATED,
		RELEASED,			//Released by executor
		APPROVED;			//Approved by top engineer
	}

	@Override
	public EcrForwardTo getForwardTo() {
		// TODO Auto-generated method stub
		return null;
	}
}