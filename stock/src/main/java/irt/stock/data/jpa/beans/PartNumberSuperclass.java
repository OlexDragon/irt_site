package irt.stock.data.jpa.beans;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public class PartNumberSuperclass{

	protected PartNumberSuperclass() { }
	public PartNumberSuperclass(String partNumber, String manufPartNumber) {
		this.partNumber 	 = partNumber;
		this.manufPartNumber = manufPartNumber;
	}

	@Id @GeneratedValue
	private Long id;
	private String partNumber;
	private String manufPartNumber;

	public Long getId() 				{ return id; }
	public String getPartNumber() 		{ return partNumber; }
	public String getManufPartNumber() 	{ return manufPartNumber; }

	public void setId(Long id) {
		this.id = id;
	}

	public void setPartNumber(String partNumber) {
		this.partNumber = partNumber;
	}

	public boolean setManufPartNumber(String manufPartNumber) {

		final String mpn = getManufPartNumber();

		if(mpn==null || mpn.equals("NULL") || mpn.trim().isEmpty()) {
			this.manufPartNumber = manufPartNumber;
			return true;
		}

		return false;
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
		if (!(obj instanceof PartNumberSuperclass))
			return false;
		PartNumberSuperclass other = (PartNumberSuperclass) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}
