package irt.data.assemblies;

import irt.data.components.Component;
import irt.work.TextWorker;

public class KitAssy extends Assemblies {

	@Override
	public void setClassId(){
		setClassId(Component.CLASS_ID_NAME.get(TextWorker.KIT_ASSEMBLIES));
	}
}
