package irt.stock.data.jpa.beans;

import java.io.Serializable;

import javax.persistence.Embeddable;

@Embeddable
public class CompanyQtyId implements Serializable{
	private static final long serialVersionUID = -4105600813907535417L;

	protected CompanyQtyId() { }
	public CompanyQtyId(Long idCompanies, long idComponents) {
		this.idCompanies = idCompanies;
		this.idComponents = idComponents;
	}

	private Long idCompanies;
	private Long idComponents;

	public Long getIdCompanies() { return idCompanies; }
	public Long getIdComponents() { return idComponents; }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = prime + ((idCompanies == null) ? 0 : idCompanies.hashCode());
		result = prime * result + ((idComponents == null) ? 0 : idComponents.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CompanyQtyId other = (CompanyQtyId) obj;
		if (idCompanies == null) {
			if (other.idCompanies != null)
				return false;
		} else if (!idCompanies.equals(other.idCompanies))
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
		return "CompanyQtyId [idCompanies=" + idCompanies + ", idComponents=" + idComponents + "]";
	}
}
