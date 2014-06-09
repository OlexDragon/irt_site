package irt.data.metal;

import irt.data.components.Component;
import irt.work.TextWork;

public class Walls extends Enclosure {

	@Override
	public void setClassId(){
		setClassId(Component.CLASS_ID_NAME.get(TextWork.WALLS));
	}
}
