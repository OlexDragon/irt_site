package irt.data.row;

import irt.data.components.Component;
import irt.work.TextWorker.PartNumberFirstChar;

public class RawMaterial extends Component {


	@Override
	public void setClassId(){
		setClassId(""+PartNumberFirstChar.RAW_MATERIAL.getFirstDigit().getFirstChar());
	}
}
