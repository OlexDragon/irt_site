package irt.data.metal;

import irt.data.components.Component;
import irt.work.TextWorker;

public class Enclosure extends MetalParts {

	@Override
	public void setClassId(){
		setClassId(Component.CLASS_ID_NAME.get(TextWorker.ENCLOSURE));
	}

	@Override
	protected String get() {
		return getClassId();
	}
}
