package irt.web.workers.beans.bomFields;

import irt.web.entities.all.ManufactureEntity;
import irt.web.entities.bom.BomEntity;
import irt.web.workers.beans.interfaces.BOMEntityFieldToString;

public class FieldMfr implements BOMEntityFieldToString {

	@Override
	public Class<?> returnType() {
		return String.class;
	}

	@Override
	public String toString(BomEntity bomEntity) {
		ManufactureEntity manufacture = bomEntity.getComponentEntity().getManufacture();
		return manufacture!=null ? manufacture.getName() : null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public String value(BomEntity bomEntity) {
		ManufactureEntity manufacture = bomEntity.getComponentEntity().getManufacture();
		return manufacture!=null ? manufacture.getName() : null;
	}

	@Override
	public String getLink(BomEntity bomEntity) {
		ManufactureEntity manufactureEntity = bomEntity.getComponentEntity().getManufacture();
		return manufactureEntity!=null ? manufactureEntity.getLink() : null;
	}
}
