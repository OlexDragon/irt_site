package irt.data;

import irt.data.components.Component;
import irt.data.dao.ComponentDAO;
import irt.data.dao.SecondAndThirdDigitsDAO;
import irt.table.OrderBy;
import irt.table.Table;
import irt.work.TextWorker;

public class Search {

	public Table components(String descriptionSecond, OrderBy orderBy) {
		Table table = null;
		String searchStr = new SecondAndThirdDigitsDAO().getId(descriptionSecond);
		
		if((table = componentBy(searchStr, orderBy))!=null)
			table.setClassName("border");
		
		return table;
	}

	public Table componentBy(String partNumberStr, OrderBy orderBy) {
		Table table = null;

		if((table = new ComponentDAO().getComponentsTable(TextWorker.pnValidation(partNumberStr), orderBy))!=null)
			table.setClassName("border");

		return table;
	}

	public Table componentBy(Component component) {
		Table table = null;

		if(component!=null && (table = new ComponentDAO().getComponentsTable(component))!=null)
			table.setClassName("border");

		return table;
	}
}
