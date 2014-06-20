package irt.data.top;

import irt.data.components.Component;
import irt.data.dao.MenuDAO;
import irt.data.dao.MenuDAO.OrderBy;
import irt.work.TextWorker;

public class Sspa extends TopLevel {

	@Override
	public void setClassId(){
		setClassId(Component.CLASS_ID_NAME.get(TextWorker.SSPA));
		getError().setErrorMessage(getClassId());
	}

	@Override
	protected void setFreqMenuDAO(MenuDAO menuDAO) {
		setFreqMenu(menuDAO.getMenu("band", OrderBy.DESCRIPTION));
	}
}
