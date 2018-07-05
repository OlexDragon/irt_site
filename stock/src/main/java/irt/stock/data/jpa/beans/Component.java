package irt.stock.data.jpa.beans;

import java.util.List;
import java.util.Optional;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

@Entity
@Table(name="components")
public class Component extends PartNumberSuperclass{

	protected Component() {}
	public Component(String partNumber, Manufacture manufacture, String description) {
		super(partNumber, null, description);
		this.manufacture = manufacture;
	}

	private String value;
	private String schematicLetter;
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

	@OneToMany(fetch=FetchType.LAZY, cascade=CascadeType.ALL)
	@LazyCollection(LazyCollectionOption.FALSE)
	@NotFound(action = NotFoundAction.IGNORE)
	@JoinColumn(name = "idComponents", nullable=true)
	private List<Cost> costs;

	@ManyToOne
	@JoinColumn(name="link", nullable=true)
	private Link link;

	public Long getQty() { return qty; }
	public List<CompanyQty> getCompanyQties() { return companyQties; }
	public Manufacture getManufacture() { return manufacture; }
	public List<ComponentAlternative> getAlternatives() { return alternativeComponents; }
	public List<Cost> getCosts() { return costs; }
	public String getSchematicLetter() { return schematicLetter; }
	public String getValue() { return value; }
	public Link getLink() { return link; }

	public void setAlternatives(List<ComponentAlternative> alternativeComponents) { this.alternativeComponents = alternativeComponents; }
	public void setQty(Long qty) { this.qty = qty; }

	public void addQty(long add) {
		qty = Optional.ofNullable(qty).map(q->q + add).filter(q->q>=0).orElse(add>0 ? add : 0);
	}

	@Override
	public String toString() {
		return "Component [schematicLetter=" + schematicLetter + ", qty=" + qty + ", manufacture=" + manufacture
				+ ", companyQties=" + companyQties + ", alternativeComponents=" + alternativeComponents + ", costs="
				+ costs + ", getId()=" + getId() + ", getPartNumber()=" + getPartNumber() + ", getManufPartNumber()="
				+ getManufPartNumber() + ", getDescription()=" + getDescription() + "]";
	}
}
