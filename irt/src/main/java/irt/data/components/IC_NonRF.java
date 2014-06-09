package irt.data.components;

import irt.data.dao.SecondAndThirdDigitsDAO;
import irt.work.TextWork;

public class IC_NonRF extends IC {

	private static final int IC_NON_RF = TextWork.IC_NON_RF;

	@Override
	public void setClassId() {
		setClassId(Component.CLASS_ID_NAME.get(IC_NON_RF));
	}

	@Override
	protected String getPartType(String partNumber) {
		String str = null;
		if(partNumber!=null && partNumber.length()>12){
			str = new SecondAndThirdDigitsDAO().getClassDescription(TextWork.IC_RF);
			str += "\\\\"+new SecondAndThirdDigitsDAO().getClassDescription(IC_NON_RF);
		}
		return str;
	}
}
