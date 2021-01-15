package irt.stock.data.jpa.beans.production;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import irt.stock.data.jpa.beans.production.CustomerOrder.CustomerOrderStatus;

public class CustomerOrderTest {

	@Test
	public void customerOrderStatusTest() {

		List<CustomerOrderStatus> orderStatus = CustomerOrderStatus.parse(null);
		assertNotNull(orderStatus);
		assertTrue(orderStatus.isEmpty());

		orderStatus = CustomerOrderStatus.parse("");
		assertTrue(orderStatus.isEmpty());

		orderStatus = CustomerOrderStatus.parse("in_process");
		assertFalse(orderStatus.isEmpty());
		assertEquals(CustomerOrderStatus.IN_PROCESS, orderStatus.get(0));

		CustomerOrderStatus[] values = CustomerOrderStatus.values();
		orderStatus = CustomerOrderStatus.parse("IN_PROCESS,waiting, unknown, , CLOSED, CREATED, CANCeled, CREATED. CREATED, CREATED");
		assertEquals(values.length, orderStatus.size());
		assertTrue(orderStatus.containsAll(Arrays.asList(values)));
	}

}
