package irt.data.metal;

import irt.data.components.Component;
import irt.work.TextWorker;

public class SheetMetalBracket extends Enclosure {

	@Override
	public void setClassId(){
		setClassId(Component.CLASS_ID_NAME.get(TextWorker.SHEET_METAL_BACKET));
	}
}
