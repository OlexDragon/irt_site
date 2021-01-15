package irt.stock.data.jpa.beans;

import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.NoArgsConstructor;

@Entity
@Table(name="components")
@NoArgsConstructor
public class PartNumber extends PartNumberSuperclass{

	public PartNumber(String partNumber, String manufPartNumber, String description) {
		super(partNumber, manufPartNumber, description);
	}
}
