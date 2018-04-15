package irt.stock.data.jpa.beans;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Digits;

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
public class Cost {
	protected Cost() { }
	public Cost(Component component, ComponentAlternative alternative, Company company, Long forQty, BigDecimal cost, Currency currency, OrderType orderType, String orderNumber) {
		this.id = new CostId(component, alternative, company, forQty);
		this.cost = cost;
		this.currency = currency;
		this.orderType = orderType;
		this.orderNumber = orderNumber;
	}

	@EmbeddedId
	private CostId id;
	@Digits(integer=20, fraction=8)
	private BigDecimal cost;
	@Enumerated(EnumType.STRING)
	@Column(length=3)
	private Currency currency;
	private String orderNumber;
	@Enumerated(EnumType.STRING)
	@JoinColumn(name="manuf_id", nullable=true)
	private OrderType orderType;
	@Temporal(TemporalType.TIMESTAMP)
	@JsonFormat(pattern="dd.MMM.yy")
	public Date changeDate;

	public CostId 				getId() 			{ return id; }
	public BigDecimal 			getCost() 			{ return cost; }
	public Currency 			getCurrency() 		{ return currency; }
	public ComponentAlternative getAlternative() 	{ return id.getComponentsAlternative(); }
	public Company 				getCompany() 		{ return id.getCompany(); }
	public Date 				getChangeDate() 	{ return changeDate; }
	public String 				getOrderNumber() 	{ return orderNumber; }
	public OrderType 			getOrderType() 		{ return orderType; }

	@PrePersist
	protected void onCreate() {
		changeDate = new Date();
	}

	@PreUpdate
	protected void onUpdate() {
		changeDate = new Date();
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
		if (getClass() != obj.getClass())
			return false;
		Cost other = (Cost) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Cost [id=" + id + ", cost=" + cost + ", currency=" + currency + ", orderType=" + orderType + ", orderNumber=" + orderNumber + ", changeDate="
				+ changeDate + "]";
	}

	public enum Currency{
		CAD,
		USD
	}

	public enum OrderType{
		PO,
		INV
	}
}
