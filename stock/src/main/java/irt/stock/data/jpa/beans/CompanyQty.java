package irt.stock.data.jpa.beans;

import java.util.Optional;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="companies_components")
public class CompanyQty {

	protected CompanyQty() {}
	public CompanyQty(Long companyId, Long idComponent, long qty) {
		this.id = new CompanyQtyId(companyId, idComponent);
		this.qty = qty;
	}

	@EmbeddedId private CompanyQtyId id;
	private long qty;

	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name = "idCompanies", insertable=false, updatable=false)
	private Company company;

	public CompanyQtyId getId() { return id; }
	public long getQty() 		{ return qty; }
	public Company getCompany() { return company; }

	public void addQty(long add) {
		qty = Optional.ofNullable(qty).map(q->q + add).filter(q->q>=0).orElse(add>0 ? add : 0);
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
		CompanyQty other = (CompanyQty) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "CompanyQty [id=" + id + ", qty=" + qty + ", company=" + company + "]";
	}
}
