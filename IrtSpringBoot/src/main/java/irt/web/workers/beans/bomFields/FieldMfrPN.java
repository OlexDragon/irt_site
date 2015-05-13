package irt.web.workers.beans.bomFields;

import irt.web.entities.bom.BomEntity;
import irt.web.entities.component.LinkEntity;
import irt.web.workers.beans.interfaces.BOMEntityFieldToString;

public class FieldMfrPN implements BOMEntityFieldToString {

	@Override
	public Class<?> returnType() {
		return String.class;
	}

	@Override
	public String toString(BomEntity bomEntity) {
		return bomEntity.getComponentEntity().getManufPartNumber();
	}

	@SuppressWarnings("unchecked")
	@Override
	public String value(BomEntity bomEntity) {
		return bomEntity.getComponentEntity().getManufPartNumber();
	}

	@Override
	public String getLink(BomEntity bomEntity) {
		LinkEntity link = bomEntity.getComponentEntity().getLink();
		String linkStr;
		if(link!=null){
			linkStr = link.getLink();
			if(!linkStr.toLowerCase().startsWith("http:"))
				linkStr = "http://irttechnologies:8080" + linkStr;
		}else
			linkStr = null;
		return linkStr;
	}
}
