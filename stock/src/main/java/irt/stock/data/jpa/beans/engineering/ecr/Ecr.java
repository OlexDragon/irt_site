package irt.stock.data.jpa.beans.engineering.ecr;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import irt.stock.data.jpa.beans.engineering.EngineeringChange;
import irt.stock.data.jpa.beans.engineering.EngineeringChangeStatus;
import irt.stock.data.jpa.beans.engineering.ecr.EcrCategory.Category;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter @Setter @NoArgsConstructor @ToString
public class Ecr implements Serializable, EngineeringChange {	// ECR - Engineering Change Request
	private static final long serialVersionUID = -8542007123854701633L;

	public Ecr(String ecrReason) {
		reason = ecrReason;
	}

	@Id @GeneratedValue
	private Long number;

	private String reason;

	@OneToMany(mappedBy = "id.ecr", cascade = CascadeType.REMOVE)
	@LazyCollection(LazyCollectionOption.FALSE)
	private List<EcrRelatedTo> relatedTos;

	@OneToMany(mappedBy = "id.ecr", cascade = CascadeType.REMOVE)
	@LazyCollection(LazyCollectionOption.FALSE)
	private List<EcrCategory> categories;

	@Getter(value = AccessLevel.NONE)
	@OneToMany(mappedBy = "ecr", cascade = CascadeType.REMOVE)
	@LazyCollection(LazyCollectionOption.FALSE)
	@OrderBy("date DESC")
	private List<EcrStatus> status;

	@Override
	public List<EngineeringChangeStatus> getStatus() {
		return status.stream().map(EngineeringChangeStatus.class::cast).collect(Collectors.toList());
	}

	@Override
	public EcrStatus getLastStatus() {
		return getLastStatus(status);
	}

	public boolean containsCategory(Category category) {
		return categories.parallelStream().map(EcrCategory::getCategory).filter(c->c.equals(category)).findAny().isPresent();
	}

	public void addStatus(EcrStatus ecrStatus) {
		if(status==null) {
			status = new ArrayList<EcrStatus>();
			setStatus(status);
		}

		status.add(ecrStatus);
	}

	public static EcrStatus getLastStatus(List<EcrStatus> ecrStatus) {
		return ecrStatus.stream()
				.sorted(
						(st2, st1)
						->
						st1.getDate().compareTo(st2.getDate()))
				.findFirst()
				.get();
	}
}
