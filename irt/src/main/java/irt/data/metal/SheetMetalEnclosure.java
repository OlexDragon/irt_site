package irt.data.metal;

import irt.data.components.Component;
import irt.work.TextWork;

public class SheetMetalEnclosure extends Enclosure {

	@Override
	public void setClassId(){
		setClassId(Component.CLASS_ID_NAME.get(TextWork.SHEET_METAL_ENCLOSURE));
	}
}
