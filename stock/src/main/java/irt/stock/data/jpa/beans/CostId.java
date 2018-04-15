package irt.stock.data.jpa.beans;

import java.io.Serializable;
import java.util.Optional;

import javax.persistence.Column;
import javax.persistence.ConstraintMode;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

@Embeddable
public class CostId implements Serializable{
	private static final long serialVersionUID = -2135580000161870931L;

	protected CostId() { }
	public CostId(Component component, ComponentAlternative alternative, Company company, Long forQty) {
		this.component = component;
		this.alternative = alternative;
		this.company = company;
		this.forQty = forQty;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="idComponents")
	private Component component;

	@ManyToOne(fetch=FetchType.EAGER)
	@NotFound(action = NotFoundAction.IGNORE)
	@JoinColumn(name="idComponentsAlternative", insertable=false, updatable=false, foreignKey=@ForeignKey(value = ConstraintMode.NO_CONSTRAINT), nullable=true)
	@Column()
	private ComponentAlternative alternative;

	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="idCompanies", insertable=false, updatable=false)
	private Company company;

	@Column(name="`for`")
	private Long forQty;

	public Component getComponent() {
		return component;
	}
	public ComponentAlternative getComponentsAlternative() {
		return alternative;
	}
	public Company getCompany() {
		return company;
	}
	public Long getForQty() {
		return forQty;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((forQty == null) ? 0 : forQty.hashCode());
		result = prime * result + ((component == null) ? 0 : component.hashCode());
		result = prime * result + ((alternative == null) ? 0 : alternative.hashCode());
		result = prime * result + ((company == null) ? 0 : company.hashCode());
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
		if (component == null) {
			if (other.component != null)
				return false;
		} else if (!component.equals(other.component))
			return false;
		if (alternative == null) {
			if (other.alternative != null)
				return false;
		} else if (!alternative.equals(other.alternative))
			return false;
		if (company == null) {
			if (other.company != null)
				return false;
		} else if (!company.equals(other.company))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "CostId [idComponents=" + component + ", componentsAlternative=" + alternative
				+ ", company=" + company + ", forQty=" + forQty + "]";
	}
}
