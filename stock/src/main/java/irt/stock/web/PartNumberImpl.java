package irt.stock.web;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="components")
public class PartNumberImpl implements PartNumber {

	@Id
	private Long id;
	private String partNumber;

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public String getPartNumber() {
		return partNumber;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setPartNumber(String partNumber) {
		this.partNumber = partNumber;
	}

}
