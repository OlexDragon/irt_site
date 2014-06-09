package irt.data.screws;

import irt.data.components.Component;
import irt.work.TextWork;

public class Screw extends Screws {

	private static final int SCREW = TextWork.SCREW;

	@Override
	public void setClassId(){
		setClassId(Component.CLASS_ID_NAME.get(SCREW));
	}

	@Override
	protected String get() {
		return getClassId();
	}
}
