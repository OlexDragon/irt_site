package irt.data.metal;

import irt.data.components.Component;
import irt.work.TextWork;

public class Enclosure extends MetalParts {

	@Override
	public void setClassId(){
		setClassId(Component.CLASS_ID_NAME.get(TextWork.ENCLOSURE));
	}

	@Override
	protected String get() {
		return getClassId();
	}
}
