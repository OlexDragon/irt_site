package irt.web.workers.beans.interfaces;

import irt.web.entities.bom.BomEntity;

public interface BOMEntityFieldToString {

	public Class<?> returnType();
	public<T> T value(BomEntity bomEntity);
	public String toString(BomEntity bomEntity);
	public String getLink(BomEntity bomEntity);
}
