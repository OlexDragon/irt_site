package irt.stock.data.jpa.beans.production;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class CustomerOrderComment {

	protected CustomerOrderComment(){}
	public CustomerOrderComment(String comment) {
		this.comment = comment;
	}

	@Id @GeneratedValue @Column(name="id_customer_order_comment") private Long id;
	private String comment;

	public Long getId() { return id; }
	public String getComment() { return comment; }
}
