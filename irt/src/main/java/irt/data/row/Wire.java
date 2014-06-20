package irt.data.row;

import irt.data.components.Component;
import irt.work.TextWorker;

public class Wire extends Component {


	private static final int WIRE = TextWorker.PLASTIC_PLARTS;

	@Override
	public void setClassId(){
		setClassId(CLASS_ID_NAME.get(WIRE));
	}
}
