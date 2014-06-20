package irt.data.top;

import irt.data.components.Component;
import irt.work.TextWorker;

public class RedundantSystem extends TopLevel {

	@Override
	public void setClassId(){
		setClassId(Component.CLASS_ID_NAME.get(TextWorker.REDUNDANT));
	}
}
