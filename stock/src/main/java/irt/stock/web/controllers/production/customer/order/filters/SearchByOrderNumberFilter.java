package irt.stock.web.controllers.production.customer.order.filters;

import java.util.List;
import java.util.Optional;

import irt.stock.data.jpa.beans.production.CustomerOrder.CustomerOrderStatus;

public class SearchByOrderNumberFilter {

	private List<CustomerOrderStatus> orderStatus;
	private String orderContains;


	public SearchByOrderNumberFilter(String orderStatus, String orderContains) {
		this.orderStatus = CustomerOrderStatus.parse(orderStatus);
		this.orderContains = orderContains;
	}


	public List<CustomerOrderStatus> getOrderStatus() { return orderStatus; }
	public String getOrderContains() { return orderContains; }



	public boolean hasStatus() {
		return !orderStatus.isEmpty();
	}


	public boolean orderNumberContains() {
		return Optional.ofNullable(orderContains).map(c->!c.isEmpty()).orElse(false);
	}
}
