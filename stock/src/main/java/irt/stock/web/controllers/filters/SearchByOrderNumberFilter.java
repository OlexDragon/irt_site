package irt.stock.web.controllers.filters;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import irt.stock.data.jpa.beans.production.CustomerOrder.CustomerOrderStatus;

public class SearchByOrderNumberFilter {
	private final static Logger logger = LogManager.getLogger();

	private List<CustomerOrderStatus> orderStatus;
	private String orderContains;


	public SearchByOrderNumberFilter(String orderStatus, String orderContains) {
		this.orderStatus = parseOrderStatus(orderStatus);
		this.orderContains = orderContains;
	}


	public List<CustomerOrderStatus> getOrderStatus() { return orderStatus; }
	public String getOrderContains() { return orderContains; }


	public static List<CustomerOrderStatus> parseOrderStatus(String orderStatus) {
		try{

			return Optional
														.ofNullable(orderStatus)
														.map(s->s.split(","))
														.map(Arrays::stream)
														.orElse(Stream.empty())
														.map(CustomerOrderStatus::valueOf)
														.collect(Collectors.toList());

		}catch(Exception e){
			logger.catching(e);
			return new ArrayList<>();
		}
	}


	public boolean hasStatus() {
		return !orderStatus.isEmpty();
	}


	public boolean orderNumberContains() {
		return Optional.ofNullable(orderContains).map(c->!c.isEmpty()).orElse(false);
	}
}
