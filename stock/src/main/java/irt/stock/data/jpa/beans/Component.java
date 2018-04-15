package irt.stock.data.jpa.beans;

import java.util.List;
import java.util.Optional;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import irt.stock.web.PartNumber;

@Entity
@Table(name="components")
public class Component implements PartNumber{

	protected Component() {}
	public Component(Long id, String partNumber, Manufacture manufacture) {
		this.id = id;
		this.partNumber = partNumber;
		this.manufacture = manufacture;
	}

	@Id @GeneratedValue
	private Long id;
	private String partNumber;
	private String description;
	private String manufPartNumber;
	private Long qty;

	@ManyToOne
	@JoinColumn(name="manuf_id", nullable=true)
	private Manufacture manufacture;

	@OneToMany(fetch=FetchType.EAGER, cascade=CascadeType.REMOVE)
	@JoinColumn(name = "idComponents", insertable=false, updatable=false, nullable=true)
	private List<CompanyQty> companyQties;

	@OneToMany(cascade=CascadeType.ALL)
	@LazyCollection(LazyCollectionOption.FALSE)
	@JoinColumn(name = "idComponents", nullable=true)
	private List<ComponentAlternative> alternativeComponents;

	@OneToMany(cascade=CascadeType.ALL)
	@LazyCollection(LazyCollectionOption.FALSE)
	@JoinColumn(name = "idComponents", nullable=true)
	private List<Cost> costs;

	@Override
	public Long getId() { return id; }
	@Override
	public String getPartNumber() { return partNumber; }
	public String getDescription() { return description; }
	public String getManufPartNumber() { return manufPartNumber; }
	public Long getQty() { return qty; }
	public List<CompanyQty> getCompanyQties() { return companyQties; }
	public Manufacture getManufacture() { return manufacture; }
	public List<ComponentAlternative> getAlternatives() { return alternativeComponents; }
	public List<Cost> getCosts() { return costs; }

	public void setAlternatives(List<ComponentAlternative> alternativeComponents) {
		this.alternativeComponents = alternativeComponents;
	}

	public void addQty(long add) {
		qty = Optional.ofNullable(qty).map(q->q + add).orElse(add);
	}

	@Override
	public int hashCode() {
		return 31 + ((id == null) ? 0 : id.hashCode());
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Component other = (Component) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "Component [id=" + id + ", partNumber=" + partNumber + ", description=" + description
				+ ", manufPartNumber=" + manufPartNumber + ", qty=" + qty + ", manufacture=" + manufacture
				+ ", companyQty=" + companyQties + "]";
	}
}
