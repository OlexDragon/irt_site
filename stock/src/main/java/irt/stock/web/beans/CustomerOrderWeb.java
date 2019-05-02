package irt.stock.web.beans;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.fasterxml.jackson.annotation.JsonFormat;

import irt.stock.data.jpa.beans.production.CustomerOrder;
import irt.stock.data.jpa.beans.production.CustomerOrder.CustomerOrderStatus;
import irt.stock.data.jpa.repositories.production.ProductionUnitRepository;
import irt.stock.data.jpa.beans.production.ProductionUnit;

public class CustomerOrderWeb {

	private Long id;
	private String orderNumber;
	@JsonFormat(pattern="dd.MMM.yyyy")
	private Date created;
	@JsonFormat(pattern="dd.MMM.yyyy")
	private Date shipped;
	private CustomerOrderStatus status;
	private String productionUnitsStr;
	private List<ProductionUnit> productionUnits;
	private final Map<Long, List<ProductionUnit>> subunits = new HashMap<>();

	public CustomerOrderWeb(CustomerOrder customerOrder) {

		id 			= customerOrder.getId();
		orderNumber = customerOrder.getOrderNumber();
		created 	= customerOrder.getCreated();
		shipped 	= customerOrder.getClosed();
		status 		= customerOrder.getCustomerOrderStatus();

		productionUnitsStr = customerOrder.getProductionUnits();
	}

	public Long getId() { return id; }
	public String getOrderNumber() { return orderNumber; }
	public Date getCreated() { return created; }
	public Date getShipped() { return shipped; }
	public CustomerOrderStatus getStatus() { return status; }
	public List<ProductionUnit> getProductionUnits() { return productionUnits; }

	public CustomerOrderWeb setProductionUnits(ProductionUnitRepository productionUnitRepository) {

		productionUnits = stringToProductionUnitsList(productionUnitsStr, productionUnitRepository);

		productionUnits
		.forEach(pu->{

			final String included = pu.getIncluded();

			if(included==null)
				return;

			subunits.put(pu.getId(), stringToProductionUnitsList(included, productionUnitRepository));
		});

		return this;
	}

	public String getInclidedSNs(Long id){
		return Optional.ofNullable(id).map(subunits::get).map(List::stream).map(s->s.map(pu->pu.getSerialNumber()).collect(Collectors.joining(", "))).orElse("");
	}
	private List<ProductionUnit> stringToProductionUnitsList(String strIDs, ProductionUnitRepository productionUnitRepository){
		return Optional
				.ofNullable(strIDs)
				.map(s->s.split(","))
				.map(Arrays::stream)
				.orElse(Stream.empty())
				.mapToLong(Long::parseLong)
				.mapToObj(productionUnitRepository::findById)
				.filter(Optional::isPresent)
				.map(Optional::get)
				.collect(Collectors.toList());
	}

	public String getStatusClass() {
		String htmlClass;
		switch(status){
		case CANCELED:
			htmlClass = "table-danger";
			break;
		case IN_PROCESS:
			htmlClass = "table-success";
			break;
		case CLOSED:
			htmlClass = "table-secondary";
			break;
		case CREATED:
			htmlClass = "table-primary";
			break;
		case WAITING:
			htmlClass = "table-info";
			break;
		default:
			htmlClass = null;
		}
		return htmlClass;
	}
}
