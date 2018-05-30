package irt.stock.data.jpa.beans;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="links")
public class Link {

	@Id @GeneratedValue
	private Long id;
	private String link;

	public Long getId() {
		return id;
	}

	public String getLink() {
		return link;
	}
}
