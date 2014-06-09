package irt.data.components;

import irt.data.dao.SecondAndThirdDigitsDAO;
import irt.work.TextWork;

public class VoltageRegulator extends IC {

	private static final int VOLTAGE_REGULATOR = TextWork.VOLTAGE_REGULATOR;

	@Override
	public void setClassId() {
		setClassId(Component.CLASS_ID_NAME.get(VOLTAGE_REGULATOR));
	}

	@Override
	protected String getPartType(String partNumber) {
		String str = null;
		if(partNumber!=null && partNumber.length()>12)
			str = super.getPartType(partNumber) +"\\\\"+new SecondAndThirdDigitsDAO().getClassDescription(VOLTAGE_REGULATOR);
		return str;
	}

}
