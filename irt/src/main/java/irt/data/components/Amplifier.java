package irt.data.components;

import irt.data.dao.SecondAndThirdDigitsDAO;
import irt.work.TextWork;

public class Amplifier extends IC {

	private static final int AMPLIFIER = TextWork.AMPLIFIER;

	@Override
	public void setClassId() {
		setClassId(Component.CLASS_ID_NAME.get(AMPLIFIER));
	}

	@Override
	protected String getPartType(String partNumber) {
		String str = null;
		if(partNumber!=null && partNumber.length()>12){
			str = new SecondAndThirdDigitsDAO().getClassDescription(AMPLIFIER);
		}
		return str;
	}
}
