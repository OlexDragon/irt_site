package irt.data.screws;

import irt.data.components.Component;
import irt.work.TextWorker;

public class Washer extends Screws {

	private static final int WASHER = TextWorker.WASHER;

	@Override
	public void setClassId(){
		setClassId(Component.CLASS_ID_NAME.get(WASHER));
	}

	@Override
	protected String get() {
		return getClassId();
	}
}