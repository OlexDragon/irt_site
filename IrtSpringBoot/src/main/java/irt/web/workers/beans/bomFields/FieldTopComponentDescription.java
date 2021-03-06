package irt.web.workers.beans.bomFields;

import irt.web.entities.bom.BomEntity;
import irt.web.workers.beans.interfaces.BOMEntityFieldToString;

public class FieldTopComponentDescription implements BOMEntityFieldToString {

	@Override
	public Class<?> returnType() {
		return String.class;
	}

	@Override
	public String toString(BomEntity bomEntity) {
		return bomEntity.getTopComponentEntity().getDescription();
	}

	@SuppressWarnings("unchecked")
	@Override
	public String value(BomEntity bomEntity) {
		return bomEntity.getTopComponentEntity().getDescription();
	}

	@Override
	public String getLink(BomEntity bomEntity) {
		return null;
	}

}
