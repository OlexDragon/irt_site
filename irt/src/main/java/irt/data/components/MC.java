package irt.data.components;

import irt.data.dao.SecondAndThirdDigitsDAO;
import irt.work.TextWorker;

public class MC extends IC {

	private static final int MICROCONTROLLER = TextWorker.MICROCONTROLLER;

	@Override
	public void setClassId() {
		setClassId(Component.CLASS_ID_NAME.get(MICROCONTROLLER));
	}

	@Override
	protected String getPartType(String partNumber) {
		String str = null;
		if(partNumber!=null && partNumber.length()>12)
			str = super.getPartType(partNumber)+"\\\\"+new SecondAndThirdDigitsDAO().getClassDescription(MICROCONTROLLER);
		return str;
	}

}
