package irt.stock.data.jpa.beans;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.ConstraintMode;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Digits;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import irt.stock.data.jpa.beans.Cost.Currency;
import irt.stock.data.jpa.beans.Cost.OrderType;

@Entity
public class CostHistory {
	protected CostHistory() { }
	public CostHistory(Cost cost) {

		idComponents	= cost.getId().getComponentId();
		alternativeId 	= cost.getId().getComponentAlternativeId();
		company 		= cost.getCompany();
		forQty 			= cost.getForQty();
		price			= cost.getCost();
		currency		= cost.getCurrency();
		orderNumber		= cost.getOrderNumber();
		orderType		= cost.getOrderType();
		changeDate		= cost.getChangeDate();
		
	}

	@Id @GeneratedValue
	@JoinColumn(name="id")
	private Long id;

	private Long idComponents;

	@JsonIgnore
	@Column(name="idComponentsAlternative")
	private Long alternativeId;
	@ManyToOne(fetch=FetchType.EAGER)
	@NotFound(action = NotFoundAction.IGNORE)
	@JoinColumn(name="idComponentsAlternative", insertable=false, updatable=false, foreignKey=@ForeignKey(value = ConstraintMode.NO_CONSTRAINT), nullable=true)
	private ComponentAlternative alternative;

	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="idCompanies")
	private Company company;

	@Column(name="`for`")
	private Long forQty;

	@Digits(integer=20, fraction=8)
	@Column(name="cost")
	private BigDecimal price;

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

	public Long getId() 			{ return id; }
	public Long getComponentId()	{ return idComponents; }
	public Long getAlternativeId() 	{ return alternativeId; }
	public ComponentAlternative getAlternative() { return alternative; }
	public Company getCompany() 	{ return company; }
	public Long getForQty() 		{ return forQty; }
	public BigDecimal getPrice() 	{ return price; }
	public Currency getCurrency() 	{ return currency; }
	public String getOrderNumber() 	{ return orderNumber; }
	public OrderType getOrderType() { return orderType; }
	public Date getDate() 			{ return changeDate; }

	@Override
	public String toString() {
		return "CostHistory [id=" + id + ", idComponents=" + idComponents + ", alternativeId=" + alternativeId
				+ ", alternative=" + alternative + ", company=" + company + ", forQty=" + forQty + ", price=" + price
				+ ", currency=" + currency + ", orderNumber=" + orderNumber + ", orderType=" + orderType
				+ ", changeDate=" + changeDate + "]";
	}
}
