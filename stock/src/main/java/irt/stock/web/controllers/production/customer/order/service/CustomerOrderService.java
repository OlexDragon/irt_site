package irt.stock.web.controllers.production.customer.order.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.BiConsumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import irt.stock.data.jpa.beans.production.CustomerOrder;
import irt.stock.data.jpa.repositories.production.CustomerOrderRepository;
import irt.stock.data.jpa.repositories.production.ProductionUnitRepository;
import irt.stock.web.beans.CustomerOrderWeb;
import irt.stock.web.controllers.production.customer.order.filters.SearchByDateFilter;
import irt.stock.web.controllers.production.customer.order.filters.SearchByOrderNumberFilter;

@Service
public class CustomerOrderService {
//	private final static Logger logger = LogManager.getLogger();

	private static final int ALL 		= 0;
	private static final int STATUS 	= 1;
	private static final int ORDER 		= 2;
	private static final int CREATED	= 4;

	@Autowired private CustomerOrderRepository customerOrderRepository;
	@Autowired private ProductionUnitRepository productionUnitRepository;

	public List<CustomerOrderWeb> getCustomerOrders(SearchByOrderNumberFilter orderNumberFilter, SearchByDateFilter createdFilter){

		Iterable<CustomerOrder> customerOrders = getOrders(orderNumberFilter, createdFilter);

		return StreamSupport.stream(customerOrders.spliterator(), false)
				.map(CustomerOrderWeb::new)
				.map(co->co.setProductionUnits(productionUnitRepository))
				.collect(Collectors.toList());
	}

	private Iterable<CustomerOrder> getOrders(SearchByOrderNumberFilter orderNumberFilter, SearchByDateFilter createdFilter) {
		int caseValue = ALL;

		if(orderNumberFilter.hasStatus())
			caseValue += STATUS;

		if(orderNumberFilter.orderNumberContains())
			caseValue += ORDER;

		if(createdFilter.isUseful())
			caseValue += CREATED;

		Iterable<CustomerOrder> customerOrders;
		switch(caseValue){
		case STATUS:
			customerOrders = customerOrderRepository.findByCustomerOrderStatusInOrderByCreatedDesc(orderNumberFilter.getOrderStatus());
			break;

		case ORDER:
			customerOrders = customerOrderRepository.findByOrderNumberContainingOrderByCreatedDesc(orderNumberFilter.getOrderContains());
			break;

		case CREATED:
			customerOrders = customerOrderRepository.findByOrderNumberContainingOrderByCreatedDesc(orderNumberFilter.getOrderContains());
			break;

		case ORDER+STATUS:
			customerOrders = customerOrderRepository.findByOrderNumberContainingAndCustomerOrderStatusInOrderByCreatedDesc(orderNumberFilter.getOrderContains(), orderNumberFilter.getOrderStatus());
			break;

		case ORDER+CREATED:
			customerOrders = customerOrderRepository.findByOrderNumberContainingAndCustomerOrderStatusInOrderByCreatedDesc(orderNumberFilter.getOrderContains(), orderNumberFilter.getOrderStatus());
			break;

		case CREATED+STATUS:
			customerOrders = customerOrderRepository.findByOrderNumberContainingAndCustomerOrderStatusInOrderByCreatedDesc(orderNumberFilter.getOrderContains(), orderNumberFilter.getOrderStatus());
			break;

		case ORDER+STATUS + CREATED:
			customerOrders = customerOrderRepository.findByOrderNumberContainingAndCustomerOrderStatusInOrderByCreatedDesc(orderNumberFilter.getOrderContains(), orderNumberFilter.getOrderStatus());
			break;

		default:
			customerOrders = customerOrderRepository.findAllByOrderByCreatedDesc();
		}
		return customerOrders;
	}

	//Return all dates 
	public Optional<DatesHolder> getCreated(SearchByOrderNumberFilter orderNumberFilter, SearchByDateFilter createdFilter) {

		final Iterable<CustomerOrder> orders = getOrders(orderNumberFilter, createdFilter);
		return Optional.of(StreamSupport.stream(orders.spliterator(), true).distinct().map(CustomerOrder::getCreated).collect(DatesHolder::new, accumulator(), combiner()));
	}

	public BiConsumer<DatesHolder, Date> accumulator() {
		return (datesHolder, date)->{
			datesHolder.add(date);
		};
	}

	private BiConsumer<DatesHolder, DatesHolder> combiner() {
		return (a, b)->{
			a.append(b);
		};
	}

	public class DatesHolder implements Supplier<DatesHolder>{

		DateFormat dateFormat = new SimpleDateFormat("MMMM");

		private Set<Integer> years = new TreeSet<>();
		private Set<String> months = new TreeSet<>();
		private Set<Integer> days = new TreeSet<>();

		public DatesHolder add(Date date){

			final Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);

			years.add(calendar.get(Calendar.YEAR));
			months.add(dateFormat.format(date));
			days.add(calendar.get(Calendar.DATE));

			return this;
		}

		public void append(DatesHolder other) {
			years.addAll(other.years);
			months.addAll(other.months);
			days.addAll(other.days);
		}

		public Set<Integer> getYears() { return years; }
		public Set<String> getMonths() { return months; }
		public Set<Integer> getDays() { return days; }

		@Override
		public DatesHolder get() {
			return this;
		}

		@Override
		public String toString() {
			return "DatesHolder [years=" + years + ", months=" + months + ", days=" + days + "]";
		}
	}
}
