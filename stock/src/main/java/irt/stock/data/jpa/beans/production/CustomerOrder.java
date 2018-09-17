package irt.stock.data.jpa.beans.production;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import irt.stock.rest.helpers.ShippedLogHelper;

@Entity
public class CustomerOrder {

	protected CustomerOrder(){}
	public CustomerOrder(String orderNumber) {
		this.orderNumber = orderNumber;

		Optional
		.of(orderNumber)
		.map(on->on.replaceAll("\\D", ""))
		.filter(on->!on.isEmpty())
		.filter(on->on.length()>4)
		.map(on->on.substring(0, 4))
		.flatMap(ShippedLogHelper::stringToDate)
		.ifPresent(d->created=d);
	}

	@Id @GeneratedValue @Column(name="id_customer_order") private Long id;
	private String orderNumber;

	private String productionUnits;
	
	private Date created = new Date();
	private Date closed;
	@Enumerated(EnumType.STRING)
	private CustomerOrderStatus customerOrderStatus = CustomerOrderStatus.CREATED;

	public Long getId() { return id; }
	public String getOrderNumber() { return orderNumber; }
	public Date getCreated() { return created; }
	public CustomerOrderStatus getCustomerOrderStatus() { return customerOrderStatus; }
	public String getProductionUnits() { return productionUnits; }
	public Date getClosed() { return closed; }

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}

	public void setProductionUnit(String productionUnits) {
		this.productionUnits = productionUnits;
	}

	public boolean addProductionUnit(ProductionUnit productionUnit){
		Long productionUnitId = productionUnit.getId();

		String pus = Optional
							.ofNullable(productionUnits)
							.map(str->str.split(","))
							.map(
									array->
									Arrays
									.stream(array)
									.filter(i->i.equals(productionUnitId.toString()))
									.findAny()
									.map(s->productionUnits)
									.orElse(productionUnits + ',' + productionUnitId))
							.orElse(productionUnitId.toString());

		if(productionUnits==null || !productionUnits.equals(pus)){

			setProductionUnit(pus);

			if(customerOrderStatus!=CustomerOrderStatus.CANCELED && customerOrderStatus!=CustomerOrderStatus.CLOSED)
				customerOrderStatus = CustomerOrderStatus.IN_PROCESS;

			return true;
		}

		return false;
	}

	public void setCustomerOrderStatus(CustomerOrderStatus customerOrderStatus) {
		this.customerOrderStatus = customerOrderStatus;
	}

	public CustomerOrder setClosed(Date closed) {

		if(created==null || created.compareTo(closed)>0)
			created = closed;

		this.closed = closed;
		customerOrderStatus = CustomerOrderStatus.CLOSED;

		return this;
	}

	@Override
	public String toString() {
		return "CustomerOrder [id=" + id + ", orderNumber=" + orderNumber + ", created=" + created + ", shipped="
				+ closed + ", customerOrderStatus=" + customerOrderStatus + "]";
	}

	public enum CustomerOrderStatus{
		CREATED,
		IN_PROCESS,
		CLOSED,
		CANCELED,
		WAITING;

		public boolean inList(List<CustomerOrderStatus> list){
			return list.contains(this);
		}
	}
}
