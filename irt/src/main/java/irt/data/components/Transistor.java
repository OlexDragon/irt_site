package irt.data.components;

import irt.data.Menu;
import irt.data.dao.MenuDAO;
import irt.data.dao.SecondAndThirdDigitsDAO;
import irt.work.TextWork;

public class Transistor extends Diode {

	private static final int TRANSISTOR = TextWork.TRANSISTOR;

	@Override
	public void setClassId(){
		setClassId(Component.CLASS_ID_NAME.get(TRANSISTOR));
	}

	@Override
	public Menu getTypes() {
		return new MenuDAO().getMenu("tr_type","description");
	}

	@Override
	public Menu getPackages() {
		return new MenuDAO().getMenu("ic_package","description");
	}

	@Override
	protected String getPartType(String partNumber) {
		String str = null;
		if(partNumber!=null && partNumber.length()>10)
			str = new SecondAndThirdDigitsDAO().getClassDescription(TRANSISTOR);
		return str;
	}

}
