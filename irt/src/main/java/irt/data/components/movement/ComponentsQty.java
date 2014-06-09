package irt.data.components.movement;

import irt.data.components.movement.interfaces.ComponentMovement;
import irt.data.components.movement.interfaces.ComponentQuantity;

import java.util.ArrayList;
import java.util.List;

public class ComponentsQty implements ComponentMovement{

	List<ComponentQuantity> componentsQty = new ArrayList<>();

	public List<ComponentQuantity> getComponentsQty() {
		return componentsQty;
	}

	public void add(ComponentQty componentQty) {
		if(!this.componentsQty.contains(componentQty))
			this.componentsQty.add(componentQty);
		else
			this.componentsQty.get(this.componentsQty.indexOf(componentQty)).addQuantity(componentQty.getStockQuantity());
	}

	public String getWhereClause(String fieldName) {
		String whereClause = "";

		if (!componentsQty.isEmpty()) {
			whereClause = "WHERE`"+fieldName+"`="+componentsQty.get(0).getId();

			for (int i = 1; i < componentsQty.size(); i++)
				whereClause += " OR`" + fieldName + "`=" + componentsQty.get(i).getId();
		}

		return whereClause;
	}
}
