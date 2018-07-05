package irt.stock.data.jpa.beans;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="components")
public class PartNumber extends PartNumberSuperclass{

	protected PartNumber() { }
	public PartNumber(String partNumber, String manufPartNumber, String description) {
		super(partNumber, manufPartNumber, description);
	}
}
