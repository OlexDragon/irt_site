package irt.data.screws;

import irt.data.components.Component;
import irt.work.TextWorker;

public class ScrOther extends Screws{

	private static final int SCR_OTHER = TextWorker.SCR_OTHER;

	@Override
	public void setClassId(){
		setClassId(Component.CLASS_ID_NAME.get(SCR_OTHER));
	}

	@Override
	protected String get() {
		return getClassId();
	}
}
