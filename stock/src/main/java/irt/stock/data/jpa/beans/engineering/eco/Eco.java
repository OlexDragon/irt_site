package irt.stock.data.jpa.beans.engineering.eco;

import java.io.Serializable;
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

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import irt.stock.data.jpa.beans.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter @Setter @NoArgsConstructor @ToString
public class Eco implements Serializable{
	private static final long serialVersionUID = 7391018021981895292L;

	@Id @GeneratedValue
	private Long number;

	private String reason;
	private String description;
	@CreationTimestamp
	private Timestamp creationDate;
	private EcoStatus status;
	private Timestamp approvalDate;
	@Column(nullable = true)
	private Long finallyApprovedBy;
	@Column(nullable = true)
	private Timestamp finallyApprovalDate;

	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	@LazyCollection(LazyCollectionOption.FALSE)
	@JoinColumn(name = "created_by", referencedColumnName = "id", nullable = false)
	private User createdBy;

	@ManyToOne(optional = false)
	@JoinColumn(name = "approved_by", referencedColumnName = "id", nullable = false)
	@LazyCollection(LazyCollectionOption.FALSE)
	private User approvedBy;

	@OneToMany(cascade = CascadeType.REMOVE)
	@JoinColumn(name = "ecoNumber", referencedColumnName = "number",  nullable = false, insertable = false, updatable = false)
	@LazyCollection(LazyCollectionOption.FALSE)
	private List<EcoRelatedTo> ecoRelatedTo;

	@OneToMany(cascade = CascadeType.REMOVE)
	@JoinColumn(name = "ecoNumber", referencedColumnName = "number",  nullable = false, insertable = false, updatable = false)
	@LazyCollection(LazyCollectionOption.FALSE)
	private List<EcoRelatedCategory> ecoRelatedCategories;

	@OneToMany(cascade = CascadeType.REMOVE)
	@JoinColumn(name = "ecoNumber", referencedColumnName = "number",  nullable = true, insertable = false, updatable = false)
	@LazyCollection(LazyCollectionOption.FALSE)
	private List<EcoFile> ecoFiles;

	public String getShortReason(int maxLength) {
		return Optional.of(reason).filter(r->r.length()>maxLength).map(r->r.substring(0, maxLength-3) + "...").orElse(reason);
	}

	public void addEcoFile(EcoFile ecoFile) {

		if(ecoFiles==null)
			ecoFiles =  new ArrayList<EcoFile>();

		ecoFiles.add(ecoFile);
	}
}
