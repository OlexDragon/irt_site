package irt.web.workers.beans.bomFields;

import irt.web.entities.bom.BomEntity;
import irt.web.workers.beans.interfaces.BOMEntityFieldToString;

public class FieldStockQty implements BOMEntityFieldToString{

	@Override
	public Class<?> returnType() {
		return Integer.class;
	}

	@Override
	public String toString(BomEntity bomEntity) {
		Integer qty = value(bomEntity);
		return qty!=null ? Integer.toString(qty) : null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Integer value(BomEntity bomEntity) {
		return bomEntity.getComponentEntity().getQty();
	}

	@Override
	public String getLink(BomEntity bomEntity) {
		return null;
	}

}
