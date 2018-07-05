package irt.stock.data.jpa.beans;

import java.util.Optional;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public class PartNumberSuperclass{

	protected PartNumberSuperclass() { }
	public PartNumberSuperclass(String partNumber, String manufPartNumber, String description) {

		this.partNumber 	 = Optional.ofNullable(partNumber).map(pn->pn.replaceAll("[^A-Z0-9]", "")).filter(pn->!pn.isEmpty() && pn.length()>2).map(String::toUpperCase).orElseThrow(()->new NullPointerException("Part number can not be empty."));
		this.manufPartNumber = manufPartNumber;
		setDescription(description);
	}

	@Id @GeneratedValue
	private Long id;
	private String partNumber;
	private String manufPartNumber;
	private String description;

	public Long getId() 				{ return id; }
	public String getPartNumber() 		{ return partNumber; }
	public String getManufPartNumber() 	{ return manufPartNumber; }
	public String getDescription() 		{ return description; }

	public void setId(Long id) {
		this.id = id;
	}

	public void setPartNumber(String partNumber) {
		this.partNumber = partNumber;
	}

	public void setDescription(String description) {
		this.description = Optional.ofNullable(description).map(String::trim).map(d->d.replaceAll("\\s+", " ")).map(d->d.replaceAll("\\p{C}", "")).filter(d->!d.isEmpty()).map(d->(d.length()>50 ? d.substring(0,47) + "..." : d)).orElse(null);
	}

	public boolean setManufPartNumber(String manufPartNumber) {

		final String mpn = getManufPartNumber();

		if(mpn==null || mpn.equals("NULL") || mpn.trim().isEmpty()) {
			this.manufPartNumber = manufPartNumber;
			return true;
		}

		return false;
	}

	public void clearManufPartNumber() {
		manufPartNumber = null;
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

	@Override
	public String toString() {
		return getClass().getSimpleName() + " [id=" + id + ", partNumber=" + partNumber + ", manufPartNumber=" + manufPartNumber
				+ ", description=" + description + "]";
	}

	public static String addDashes(String partNumber){

			int length = partNumber.length();

			if(length>15){
				int position = length-3;
				partNumber = partNumber.substring(0, position) + "-" + partNumber.substring(position);
			}

			if(length>10){
				String sub = partNumber.substring(0,3);

				if(sub.equals("00I") || sub.equals("TPB") || sub.equals("TRS"))
					partNumber = partNumber.substring(0, 10) + "-" + partNumber.substring(10);

				else if(sub.equals("0IS") || sub.equals("0RF"))
					partNumber = partNumber.substring(0, 8) + "-" + partNumber.substring(8);

				else
					partNumber = partNumber.substring(0, 9) + "-" + partNumber.substring(9);
			}

			if(length>3)
				partNumber = partNumber.substring(0, 3) + "-" + partNumber.substring(3);

		return partNumber;
	}
}
