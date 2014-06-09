package irt.data.screws;

import irt.data.components.Component;
import irt.work.TextWork;

public class Nut extends Screws{

	private static final int NUT = TextWork.NUT;

	@Override
	public void setClassId(){
		setClassId(Component.CLASS_ID_NAME.get(NUT));
	}

	@Override
	protected String get() {
		return getClassId();
	}
}
