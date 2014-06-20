package irt.data.metal;

import irt.data.components.Component;
import irt.work.TextWorker;

public class Heating extends Enclosure {

	@Override
	public void setClassId(){
		setClassId(Component.CLASS_ID_NAME.get(TextWorker.HEATING));
	}
}
