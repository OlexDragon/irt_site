package irt.stock.data.jpa.beans.production;

import java.util.Optional;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class CustomerOrderDescription {

	protected CustomerOrderDescription(){}
	public CustomerOrderDescription(String description) {
		setDescription(description);
	}

	@Id @GeneratedValue @Column(name="id_customer_order_description") private Long id;
	private String description;

	public Long getId() { return id; }
	public String getDescription() { return description; }

	public void setDescription(String description) {
		this.description = Optional.ofNullable(description).map(String::trim).map(d->d.replaceAll("\\s+", " ")).filter(d->!d.isEmpty()).map(d->(d.length()>250 ? d.substring(0,247) + "..." : d)).orElse(null);
	}
}
