package irt.stock.data.jpa.beans;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class CostId implements Serializable{
	private static final long serialVersionUID = -2135580000161870931L;

	protected CostId() { }
	public CostId(Long idComponents, Long idAlternative, Long companyId, Long forQty) {
		this.idComponents =idComponents;
		this.alternativeId = idAlternative;
		this.idCompanies = companyId;
		this.forQty = forQty;
	}

	@Column(name="idComponentsAlternative")
	private Long alternativeId;

	private Long idComponents;
	private Long idCompanies;

	@Column(name="`for`")
	private Long forQty;

	public Long getComponentId() { return idComponents; }
	public Long getComponentAlternativeId() { return alternativeId; }
	public Long getCompanyId() { return idCompanies; }
	public Long getForQty() { return forQty; }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((forQty == null) ? 0 : forQty.hashCode());
		result = prime * result + ((idComponents == null) ? 0 : idComponents.hashCode());
		result = prime * result + ((alternativeId == null) ? 0 : alternativeId.hashCode());
		result = prime * result + ((idCompanies == null) ? 0 : idCompanies.hashCode());
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
		CostId other = (CostId) obj;
		if (forQty == null) {
			if (other.forQty != null)
				return false;
		} else if (!forQty.equals(other.forQty))
			return false;
		if (idComponents == null) {
			if (other.idComponents != null)
				return false;
		} else if (!idComponents.equals(other.idComponents))
			return false;
		if (alternativeId == null) {
			if (other.alternativeId != null)
				return false;
		} else if (!alternativeId.equals(other.alternativeId))
			return false;
		if (idCompanies == null) {
			if (other.idCompanies != null)
				return false;
		} else if (!idCompanies.equals(other.idCompanies))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "CostId [idComponents=" + idComponents + ", componentsAlternative=" + alternativeId
				+ ", companyId=" + idCompanies + ", forQty=" + forQty + "]";
	}
}
