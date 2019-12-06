package irt.stock.data.jpa.beans.production;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import irt.stock.data.jpa.beans.production.CustomerOrder.CustomerOrderStatus;

public interface CustomerOrderRepository extends CrudRepository<CustomerOrder, Long> {

	boolean existsByOrderNumber(String orderNumber);
	Optional<CustomerOrder> findByOrderNumber(String orderNumber);
	List<CustomerOrder> findByOrderNumberStartingWith(String orderNumber);
	List<CustomerOrder> findByCustomerOrderStatusIn(Collection<CustomerOrderStatus> orderStatus);
	List<CustomerOrder> findAllByOrderByCreatedDesc();
	List<CustomerOrder> findByCustomerOrderStatusInOrderByCreatedDesc(List<CustomerOrderStatus> value);
	List<CustomerOrder> findByOrderNumberContainingOrderByCreatedDesc(String orderNumber);
	List<CustomerOrder> findByOrderNumberContainingAndCustomerOrderStatusInOrderByCreatedDesc(String orderContains, List<CustomerOrderStatus> status);

	Optional<CustomerOrder> findByCreatedLikeOrderByCreatedDesc(String date);
}
