package irt.stock.data.jpa.beans;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import irt.stock.web.PartNumber;

@Entity
@Table(name="components")
public class PartNumberImpl implements PartNumber {

	protected PartNumberImpl() { }
	public PartNumberImpl(PartNumber component) {
		id 				= component.getId();
		partNumber 		= component.getPartNumber();
		manufPartNumber = component.getManufPartNumber();
	}

	@Id
	private Long id;
	private String partNumber;
	private String manufPartNumber;

	@Override public Long getId() 					{ return id; }
	@Override public String getPartNumber() 		{ return partNumber; }
	@Override public String getManufPartNumber() 	{ return manufPartNumber; }

	public void setId(Long id) {
		this.id = id;
	}

	public void setPartNumber(String partNumber) {
		this.partNumber = partNumber;
	}

}
