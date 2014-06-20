package irt.data.pcb;

import irt.data.components.Component;
import irt.work.TextWorker;

public class Project extends Schematic {

	@Override
	public void setClassId(){
		setClassId(Component.CLASS_ID_NAME.get(TextWorker.PROJECT));
	}
}
