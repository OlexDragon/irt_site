package irt.stock.web.controllers.production.customer.order;

import java.util.AbstractMap;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import irt.stock.data.jpa.beans.production.CustomerOrder.CustomerOrderStatus;
import irt.stock.web.beans.CustomerOrderWeb;
import irt.stock.web.controllers.production.customer.order.filters.SearchByDateFilter;
import irt.stock.web.controllers.production.customer.order.filters.SearchByOrderNumberFilter;
import irt.stock.web.controllers.production.customer.order.filters.SearchByDateFilter.SearchStyle;
import irt.stock.web.controllers.production.customer.order.service.CustomerOrderService;

@Controller
@RequestMapping("production")
public class CustomerOrderController {

	private final static Logger logger = LogManager.getLogger();

	private static final String CREATED_DAY = "created_day";
	private static final String CREATED_MONTH = "created_month";
	private static final String CREATED_YEAR = "created_year";
	private static final String CREATED_TYPE = "created_type";
	public static final String ORDER_CONTAINS = "order_contains";
	public static final String ORDER_STATUS = "order_status";

	@Autowired private CustomerOrderService orderService;

	@ModelAttribute
	public void getCookies( HttpServletRequest request, Model model){

		final Map<String, Cookie> map = Arrays.stream(request.getCookies()).map(c->new AbstractMap.SimpleEntry<>(c.getName(), c)).distinct().collect(Collectors.toMap(entry->entry.getKey(), entry->entry.getValue()));

		Optional.ofNullable(map.get(ORDER_STATUS)).map(Cookie::getValue).ifPresent(os->model.addAttribute(ORDER_STATUS, os));
		Optional.ofNullable(map.get(ORDER_CONTAINS)).map(Cookie::getValue).ifPresent(oc->model.addAttribute(ORDER_CONTAINS, oc));

		Optional.ofNullable(map.get(CREATED_TYPE)).map(Cookie::getValue).filter(cookieValue->Arrays.stream(SearchStyle.values()).map(SearchStyle::name).filter(name->name.equals(cookieValue)).findAny().isPresent()).map(SearchStyle::valueOf).ifPresent(cs->model.addAttribute(CREATED_TYPE, cs));
		Optional.ofNullable(map.get(CREATED_YEAR)).map(Cookie::getValue).ifPresent(cy->model.addAttribute(CREATED_YEAR, cy));
		Optional.ofNullable(map.get(CREATED_MONTH)).map(Cookie::getValue).ifPresent(cm->model.addAttribute(CREATED_MONTH, cm));
		Optional.ofNullable(map.get(CREATED_DAY)).map(Cookie::getValue).ifPresent(cd->model.addAttribute(CREATED_DAY, cd));
	}

	@GetMapping("co")
	public String customerOrders(Model model){

		final Map<String, Object> asMap = model.asMap();

		final String orderStatus = (String)asMap.get(ORDER_STATUS);
		final String orderContains = (String)asMap.get(ORDER_CONTAINS);

		final SearchByOrderNumberFilter orderNumberFilter = new SearchByOrderNumberFilter(orderStatus, orderContains);

		final SearchByDateFilter createdFilter = new SearchByDateFilter((SearchStyle)asMap.get(CREATED_TYPE), (String)asMap.get(CREATED_YEAR), (String)asMap.get(CREATED_MONTH), (String)asMap.get(CREATED_DAY));

		final List<CustomerOrderWeb> customerOrders = orderService.getCustomerOrders(orderNumberFilter, createdFilter);

		model.addAttribute("customerOrders", customerOrders);

		Optional.ofNullable(orderStatus).ifPresent(os->model.addAttribute(ORDER_STATUS, os));
		Optional.ofNullable(orderContains).ifPresent(oc->model.addAttribute(ORDER_CONTAINS, oc));

		return "production/customer/order/customer_orders";
	}

	//Modal popup window
	@PostMapping("filter/order")
	public String filterOrders(Model model){

		final Map<String, Object> asMap = model.asMap();

		final List<CustomerOrderStatus> status = CustomerOrderStatus.parse((String)asMap.get(ORDER_STATUS));
		model.addAttribute("selected", status);

		Optional.ofNullable((String)asMap.get(ORDER_CONTAINS)).ifPresent(oc->model.addAttribute("coContains", oc));

		return "production/customer/order/filter_orders :: filter";
	}

	//Modal popup window
	@PostMapping("filter/clear")
	public String clearSearchFields(){

		return "production/customer/order/filter_clear :: filter";
	}

	//Modal popup window
	@PostMapping("filter/created")
	public String created(Model model){

		final Map<String, Object> asMap = model.asMap();
		final SearchStyle createdStyle = (SearchStyle)asMap.get(CREATED_TYPE);

		final SearchByOrderNumberFilter orderNumberFilter = new SearchByOrderNumberFilter((String)asMap.get(ORDER_STATUS), (String)asMap.get(ORDER_CONTAINS));
		final SearchByDateFilter createdFilter = new SearchByDateFilter(createdStyle, (String)asMap.get(CREATED_YEAR), (String)asMap.get(CREATED_MONTH), (String)asMap.get(CREATED_DAY));

		orderService.getCreated(orderNumberFilter, createdFilter).ifPresent(created->model.addAttribute("dates", created));
		Optional.ofNullable(createdStyle).ifPresent(ss->model.addAttribute("selectedStyle", ss));
		model.addAttribute("searchStyles", SearchStyle.values());
		model.addAttribute("searchBetween", SearchStyle.BETWEEN.name());

		return "production/customer/order/filter_date :: filter";
	}

	@ExceptionHandler
	public String clearCookies(HttpServletRequest request, HttpServletResponse response, Exception ex, Model model){
		logger.catching(ex);

		Arrays.stream(request.getCookies())
		.filter(c->!c.getName().equals("JSESSIONID"))
		.filter(c->!c.getName().equals("remember-me"))
		.forEach(
				c->{
					logger.error("name: {}; Domain: {}; Path: {}; age: {}; value: {};", c.getName(), c.getDomain(), c.getPath(), c.getMaxAge(), c.getValue());
					c.setValue(null);
					c.setMaxAge(0);
					response.addCookie(c);
				});
		return customerOrders(model);
	}
}
