package irt.data.assemblies;

import irt.data.components.Component;
import irt.work.TextWorker;

public class CarrierAssy extends Assemblies {

	@Override
	public void setClassId(){
		setClassId(Component.CLASS_ID_NAME.get(TextWorker.CARRIER_ASSEMBLIES));
	}
}
