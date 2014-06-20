package irt.data.components;

import irt.data.dao.SecondAndThirdDigitsDAO;
import irt.work.TextWorker;

public class Amplifier extends IC {

	private static final SecondAndThirdDigitsDAO SECOND_AND_THIRD_DIGITS_DAO = new SecondAndThirdDigitsDAO();
	private static final int AMPLIFIER = TextWorker.AMPLIFIER;

	@Override
	public void setClassId() {
		setClassId(Component.CLASS_ID_NAME.get(AMPLIFIER));
	}

	@Override
	protected String getPartType(String partNumber) {
		String str = null;
		if(partNumber!=null && partNumber.length()>12){
			str = SECOND_AND_THIRD_DIGITS_DAO.getClassDescription(AMPLIFIER);
		}
		return str;
	}
}
