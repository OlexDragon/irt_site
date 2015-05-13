package irt.web.workers.excel;

import irt.web.workers.beans.OrderNameVisibility;
import irt.web.workers.beans.bomFields.FieldDescription;
import irt.web.workers.beans.bomFields.FieldFootprint;
import irt.web.workers.beans.bomFields.FieldLocation;
import irt.web.workers.beans.bomFields.FieldMfr;
import irt.web.workers.beans.bomFields.FieldMfrPN;
import irt.web.workers.beans.bomFields.FieldPartNumber;
import irt.web.workers.beans.bomFields.FieldPartReference;
import irt.web.workers.beans.bomFields.FieldQty;
import irt.web.workers.beans.bomFields.FieldStockQty;
import irt.web.workers.beans.interfaces.BOMEntityFieldToString;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class OrderNameVisibilityWorker {

	private final static String[] names 			= new String[]{"Part Reference",	"Description",	"Part Number",	"QTY",	"SQty",		"Mfr P/N",	"Mfr",		"Location", "Footprint"};
	private final static String[] classNames 		= new String[]{"col-md-3",			"col-md-2",			"col-md-2",	null,	null,		"col-md-1", "col-md-1", "col-md-1",	"col-md-1"};
	private final static BOMEntityFieldToString[] BOMEF	= new BOMEntityFieldToString[]{
																				new FieldPartReference(),
																				new FieldDescription(),
																				new FieldPartNumber(),
																				new FieldQty(),
																				new FieldStockQty(),
																				new FieldMfrPN(),
																				new FieldMfr(),
																				new FieldLocation(),
																				new FieldFootprint()};
	private List<OrderNameVisibility> orderNameVisibilities;

	public OrderNameVisibilityWorker() {
		reset();
	}

	private void reset() {
		orderNameVisibilities = new ArrayList<>();
		for(int i=0; i<names.length; )
			orderNameVisibilities.add(new OrderNameVisibility("onv"+i, names[i], classNames[i], BOMEF[i], ++i, true));
	}

	public List<OrderNameVisibility> getOrderNameVisibilities(boolean reset) {
		if(reset) reset();
		return orderNameVisibilities;
	}
}
