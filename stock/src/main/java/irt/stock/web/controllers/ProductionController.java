package irt.stock.web.controllers;

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
import irt.stock.web.controllers.filters.SearchByDateFilter;
import irt.stock.web.controllers.filters.SearchByDateFilter.SearchStyle;
import irt.stock.web.controllers.filters.SearchByOrderNumberFilter;
import irt.stock.web.service.CustomerOrderService;

@Controller
@RequestMapping("production")
public class ProductionController {
	private final static Logger logger = LogManager.getLogger();

	@Autowired private CustomerOrderService orderService;

	@ModelAttribute
	public void getCookies( HttpServletRequest request, Model model){

		final Map<String, Cookie> map = Arrays.stream(request.getCookies()).map(c->new AbstractMap.SimpleEntry<>(c.getName(), c)).distinct().collect(Collectors.toMap(entry->entry.getKey(), entry->entry.getValue()));

		Optional.ofNullable(map.get("order_status")).map(Cookie::getValue).ifPresent(os->model.addAttribute("orderStatus", os));
		Optional.ofNullable(map.get("order_contains")).map(Cookie::getValue).ifPresent(oc->model.addAttribute("orderContains", oc));

		Optional.ofNullable(map.get("created_type")).map(Cookie::getValue).filter(cookieValue->Arrays.stream(SearchStyle.values()).map(SearchStyle::name).filter(name->name.equals(cookieValue)).findAny().isPresent()).map(SearchStyle::valueOf).ifPresent(cs->model.addAttribute("createdStyle", cs));
		Optional.ofNullable(map.get("created_year")).map(Cookie::getValue).ifPresent(cy->model.addAttribute("createdYear", cy));
		Optional.ofNullable(map.get("created_month")).map(Cookie::getValue).ifPresent(cm->model.addAttribute("createdMonth", cm));
		Optional.ofNullable(map.get("created_day")).map(Cookie::getValue).ifPresent(cd->model.addAttribute("createdDay", cd));
	}

	@GetMapping("co")
	public String customerOrders(Model model){

		final Map<String, Object> asMap = model.asMap();

		final String orderStatus = (String)asMap.get("orderStatus");
		final String orderContains = (String)asMap.get("orderContains");

		final SearchByOrderNumberFilter orderNumberFilter = new SearchByOrderNumberFilter(orderStatus, orderContains);

		final SearchByDateFilter createdFilter = new SearchByDateFilter((SearchStyle)asMap.get("createdStyle"), (String)asMap.get("createdYear"), (String)asMap.get("createdMonth"), (String)asMap.get("createdDay"));

		final List<CustomerOrderWeb> customerOrders = orderService.getCustomerOrders(orderNumberFilter, createdFilter);

		model.addAttribute("customerOrders", customerOrders);

		Optional.ofNullable(orderStatus).ifPresent(os->model.addAttribute("orderStatus", os));
		Optional.ofNullable(orderContains).ifPresent(oc->model.addAttribute("orderContains", oc));

		return "production/customer_orders";
	}

	//Modal popup window
	@PostMapping("filter/order")
	public String filterOrders(Model model){

		final Map<String, Object> asMap = model.asMap();

		final List<CustomerOrderStatus> status = SearchByOrderNumberFilter.parseOrderStatus((String)asMap.get("orderStatus"));
		model.addAttribute("selected", status);

		Optional.ofNullable((String)asMap.get("orderContains")).ifPresent(oc->model.addAttribute("coContains", oc));

		return "production/filter_orders :: filter";
	}

	//Modal popup window
	@PostMapping("filter/clear")
	public String clearSearchFields(){

		return "production/filter_clear :: filter";
	}

	//Modal popup window
	@PostMapping("filter/created")
	public String created(Model model){

		final Map<String, Object> asMap = model.asMap();
		final SearchStyle createdStyle = (SearchStyle)asMap.get("createdStyle");

		final SearchByOrderNumberFilter orderNumberFilter = new SearchByOrderNumberFilter((String)asMap.get("orderStatus"), (String)asMap.get("orderContains"));
		final SearchByDateFilter createdFilter = new SearchByDateFilter(createdStyle, (String)asMap.get("createdYear"), (String)asMap.get("createdMonth"), (String)asMap.get("createdDay"));

		orderService.getCreated(orderNumberFilter, createdFilter).ifPresent(created->model.addAttribute("dates", created));
		Optional.ofNullable(createdStyle).ifPresent(ss->model.addAttribute("selectedStyle", ss));

		return "production/filter_date :: filter";
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
