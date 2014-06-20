package irt.data.components;

import irt.data.dao.SecondAndThirdDigitsDAO;
import irt.work.TextWorker;

public class Other extends IC {

	private static final int OTHER = TextWorker.OTHER;

	@Override
	public void setClassId() {
		setClassId(Component.CLASS_ID_NAME.get(OTHER));
	}

	@Override
	protected String getPartType(String partNumber) {
		String str = null;
		if(partNumber!=null && partNumber.length()>12)
			str = new SecondAndThirdDigitsDAO().getClassDescription(OTHER);
		return str;
	}

	@Override
	protected boolean checkLeadsQuantity(String valueStr) {
		return getPackage().equals("OT") ? Integer.parseInt(valueStr) >= 0 : super.checkLeadsQuantity(valueStr);
	}

}
