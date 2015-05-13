package irt.web.workers.beans.bomFields;

import irt.web.entities.bom.BomEntity;
import irt.web.view.workers.component.PartNumbers;
import irt.web.workers.beans.interfaces.BOMEntityFieldToString;

public class FieldTopComponentPN implements BOMEntityFieldToString {

	@Override
	public Class<?> returnType() {
		return String.class;
	}

	@Override
	public String toString(BomEntity bomEntity) {
		return PartNumbers.format(bomEntity.getTopComponentEntity().getPartNumber());
	}

	@SuppressWarnings("unchecked")
	@Override
	public String value(BomEntity bomEntity) {
		return PartNumbers.format(bomEntity.getTopComponentEntity().getPartNumber());
	}

	@Override
	public String getLink(BomEntity bomEntity) {
		return "/?partNumber="+bomEntity.getTopComponentEntity().getPartNumber();
	}

}
