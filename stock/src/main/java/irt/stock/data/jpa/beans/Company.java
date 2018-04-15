package irt.stock.data.jpa.beans;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Table;

@Entity
@Table(name="companies")
public class Company {

	protected Company() { }
	public Company(Long id, String companyName, CompanyType companyType) {
		this.id = id;
		this.companyName = companyName;
		this.type = companyType;
	}

	@Id @GeneratedValue
	@JoinColumn(name="id")
	private Long id;
	@Column(name="company")
	private String companyName;
	@Enumerated(EnumType.ORDINAL)
	private CompanyType type;

	public Long getId() { return id; }
	public String getCompanyName() { return companyName; }
	public CompanyType getType() { return type; }

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
		Company other = (Company) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "Company [id=" + id + ", companyName=" + companyName + "]";
	}

	public enum CompanyType{
		TYPE0,	//TODO
		STOCK,
		TYPE2,	//TODO
		TYPE3,	//TODO
		CO_MANUFACTURER,
		VENDOR
	}
}
