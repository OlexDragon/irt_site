package irt.data.top;

import irt.data.components.Component;
import irt.data.pcb.Schematic;
import irt.work.TextWorker;

public class FrequencyConverter extends Schematic {

	@Override
	public void setClassId(){
		setClassId(Component.CLASS_ID_NAME.get(TextWorker.FREQUENCY_CONVERTER));
	}
}
