package irt.data.components;

import irt.data.dao.SecondAndThirdDigitsDAO;
import irt.work.TextWorker;

public class Varicap extends Capacitor {
	
	private static final int VARICAP = TextWorker.VARICAP;

	@Override
	public void setClassId() {
		setClassId(Component.CLASS_ID_NAME.get(VARICAP));
	}

	@Override
	public boolean setValue(int index, String valueStr) {
		
		boolean isSetted = super.setValue(index, valueStr);
		
		if(isSetted && index==MAN_PART_NUM)
			if(!(isSetted = !getMfrPN().isEmpty()))
				getError().setErrorMessage(" Type Manufacture P/N. ");
		
		return isSetted;
	}

	@Override
	public boolean isSet() {
		return super.isSet() && !getMfrPN().isEmpty();
	}

	@Override
	protected String getPartType(String partNumber) {
		String str = null;
		if(partNumber!=null && partNumber.length()>12)
			str = new SecondAndThirdDigitsDAO().getClassDescription(VARICAP);
		return str;
	}

}
