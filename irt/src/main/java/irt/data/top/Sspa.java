package irt.data.top;

import irt.data.components.Component;
import irt.data.dao.MenuDAO;
import irt.work.TextWork;

public class Sspa extends TopLevel {

	@Override
	public void setClassId(){
		setClassId(Component.CLASS_ID_NAME.get(TextWork.SSPA));
		getError().setErrorMessage(getClassId());
	}

	@Override
	protected void setFreqMenuDAO(MenuDAO menuDAO) {
		setFreqMenu(menuDAO.getMenu("band","description"));
	}
}
