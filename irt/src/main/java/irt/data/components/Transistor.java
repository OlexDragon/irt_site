package irt.data.components;

import irt.data.Menu;
import irt.data.dao.MenuDAO;
import irt.data.dao.SecondAndThirdDigitsDAO;
import irt.data.dao.MenuDAO.OrderBy;
import irt.work.TextWorker;

public class Transistor extends Diode {

	private static final int TRANSISTOR = TextWorker.TRANSISTOR;

	@Override
	public void setClassId(){
		setClassId(Component.CLASS_ID_NAME.get(TRANSISTOR));
	}

	@Override
	public Menu getTypes() {
		return new MenuDAO().getMenu("tr_type", OrderBy.DESCRIPTION);
	}

	@Override
	public Menu getPackages() {
		return new MenuDAO().getMenu("ic_package", OrderBy.DESCRIPTION);
	}

	@Override
	protected String getPartType(String partNumber) {
		String str = null;
		if(partNumber!=null && partNumber.length()>10)
			str = new SecondAndThirdDigitsDAO().getClassDescription(TRANSISTOR);
		return str;
	}

}
