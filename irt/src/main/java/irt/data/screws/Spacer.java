package irt.data.screws;

import irt.data.components.Component;
import irt.work.TextWork;

public class Spacer extends Screws {

	private static final int SPACER = TextWork.SPACER;

	@Override
	public void setClassId(){
		setClassId(Component.CLASS_ID_NAME.get(SPACER));
	}

	@Override
	protected String get() {
		return getClassId();
	}
}
