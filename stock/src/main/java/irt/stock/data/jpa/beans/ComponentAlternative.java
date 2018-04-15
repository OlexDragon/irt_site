package irt.stock.data.jpa.beans;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="components_alternative")
public class ComponentAlternative {

	protected ComponentAlternative() { }
	public ComponentAlternative(Long id, Long idComponents, String altMfrPartNumber, Manufacture manufacture, Boolean active) {
		this.id = id;
		this.idComponents = idComponents;
		this.altMfrPartNumber = altMfrPartNumber;
		this.manufacture = manufacture;
		this.active = active;
	}

	@Id @GeneratedValue
	private Long id;
	private Long idComponents;
	private String altMfrPartNumber;
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="manufId")
	private Manufacture manufacture;
	private Boolean active;

	public Long getId() { return id; }
	public Long getIdComponents() { return idComponents; }
	public String getAltMfrPartNumber() { return altMfrPartNumber; }
	public Manufacture getManufacture() { return manufacture; }
	public Boolean getActive() { return active; }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = prime + ((altMfrPartNumber == null) ? 0 : altMfrPartNumber.hashCode());
		return prime * result + ((idComponents == null) ? 0 : idComponents.hashCode());
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ComponentAlternative other = (ComponentAlternative) obj;
		if (altMfrPartNumber == null) {
			if (other.altMfrPartNumber != null)
				return false;
		} else if (!altMfrPartNumber.equals(other.altMfrPartNumber))
			return false;
		if (idComponents == null) {
			if (other.idComponents != null)
				return false;
		} else if (!idComponents.equals(other.idComponents))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ComponentAlternative [id=" + id + ", idComponents=" + idComponents + ", altMfrPartNumber="
				+ altMfrPartNumber + ", Manufacture=" + manufacture + ", active=" + active + "]";
	}
}
