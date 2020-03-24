package irt.stock.data.jpa.beans.engineering.eco;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import irt.stock.data.jpa.beans.engineering.EngineeringChange;
import irt.stock.data.jpa.beans.engineering.EngineeringChangeStatus;
import irt.stock.data.jpa.beans.engineering.ecr.Ecr;
import irt.stock.data.jpa.beans.engineering.ecr.EcrCategory;
import irt.stock.data.jpa.beans.engineering.ecr.EcrCategory.Category;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter @Setter @NoArgsConstructor @ToString
public class Eco implements Serializable, EngineeringChange {
	private static final long serialVersionUID = 7391018021981895292L;

	public Eco(Ecr ecr) {
		this.ecr = ecr;
	}

	@Id
	private Long number;

	@MapsId
	@OneToOne
	@JoinColumn(name = "number", referencedColumnName = "number", nullable = false, insertable = false, updatable = false)
	private Ecr ecr;

	@Column(nullable = true)
	private String description;

	@OneToMany(mappedBy = "id.eco", cascade = CascadeType.REMOVE)
	@LazyCollection(LazyCollectionOption.FALSE)
	private List<EcoRelatedTo> relatedTos;

	@OneToMany(mappedBy = "id.eco", cascade = CascadeType.REMOVE)
	@LazyCollection(LazyCollectionOption.FALSE)
	private List<EcoCategory> categories;

	@Getter(value = AccessLevel.NONE)
	@OneToMany(mappedBy = "eco", cascade = CascadeType.REMOVE)
	@LazyCollection(LazyCollectionOption.FALSE)
	@OrderBy("date DESC")
	private List<EcoStatus> status;

	@Override
	public List<EngineeringChangeStatus> getStatus() {
		return status.stream().map(EngineeringChangeStatus.class::cast).collect(Collectors.toList());
	}

	public boolean containsCategory(Category category) {
		Stream<Category> stream = Optional.ofNullable(categories).filter(c->!c.isEmpty()).map(List::stream).map(s->s.map(EcoCategory::getCategory)).orElse(ecr.getCategories().stream().map(EcrCategory::getCategory));
		return stream.filter(c->c.equals(category)).findAny().isPresent();
	}

	@Override
	public String getReason() {
		return ecr.getReason();
	}

	@Override
	public EcoStatus getLastStatus() {
		return getLastStatus(status);
	}

	public void addStatus(EcoStatus ecrStatus) {
		if(status==null) {
			status = new ArrayList<EcoStatus>();
			setStatus(status);
		}

		status.add(ecrStatus);
	}

	public static EcoStatus getLastStatus(List<EcoStatus> ecoStatus) {
		return ecoStatus.stream()
				.sorted(
						(st2, st1)
						->
						st1.getDate().compareTo(st2.getDate()))
				.findFirst()
				.get();
	}
}
