package irt.data.top;

import irt.data.components.Component;
import irt.data.dao.MenuDAO;
import irt.work.TextWork;

public class Sspb extends TopLevel {

	@Override
	public void setClassId(){
		setClassId(Component.CLASS_ID_NAME.get(TextWork.SSPB));
	}

	@Override
	protected void setFreqMenuDAO(MenuDAO menuDAO) {
		setFreqMenu(menuDAO.getMenu("band","description"));
	}
}
