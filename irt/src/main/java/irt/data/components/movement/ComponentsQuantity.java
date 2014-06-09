package irt.data.components.movement;

import irt.data.components.Component;
import irt.data.components.movement.interfaces.ComponentQuantity;

import java.util.ArrayList;
import java.util.List;


public class ComponentsQuantity {

	List<ComponentQuantity> componentsQuantity = new ArrayList<>();

	public String getWhereClause(String string) {
		String whereClause = "";
		if(!componentsQuantity.isEmpty())
			whereClause = "WHERE";
		boolean isFirst = true;
		for(ComponentQuantity cq:componentsQuantity){
			if(isFirst)
				isFirst = false;
			else
				whereClause += " OR";
			whereClause += "`id`="+cq.getId();
		}
		return whereClause;
	}

	public List<ComponentQuantity> getComponentsQuantity() {
		return componentsQuantity;
	}

	public ComponentToMove[] getComponentsToMove() {
		ComponentToMove[] ctm = new ComponentToMove[componentsQuantity.size()];
		return componentsQuantity.toArray(ctm);
	}

	public void setComponentsQuantity(List<ComponentQuantity> componentsQuantity) {
		this.componentsQuantity = componentsQuantity;
	}

	public void add(ComponentQuantity componentQuantity) {
		componentsQuantity.add(componentQuantity);
	}

	public boolean add(ComponentToMove ctm) {
		boolean wasAdded = false;

		if(!componentsQuantity.contains(ctm)){
			componentsQuantity.add(ctm);
			wasAdded = true;
		}
		return wasAdded;
	}

	public boolean isEmpty() {
		return componentsQuantity.isEmpty();
	}

	public ComponentQuantity get(int index) {
		return componentsQuantity.size()>index ? componentsQuantity.get(index) : null;
	}

	public int size() {
		return componentsQuantity.size();
	}

	public void remove(int index) {
		componentsQuantity.remove(index);
	}

	@Override
	public String toString() {
		return "ComponentsQuantity=" + componentsQuantity;
	}

	public boolean contains(int componentId) {
		return componentsQuantity.contains(new ComponentToMove(componentId));
	}

	public ComponentToMove getComponentToMove(int componentId) {
		return (ComponentToMove) componentsQuantity.get(componentsQuantity.indexOf(new Component(componentId)));
	}

	public int indexOf(ComponentToMove[] alternativeComponents) {
		int index = -1;
		for(ComponentToMove ctm:alternativeComponents)
			if((index = componentsQuantity.indexOf(ctm))>=0)
				break;

		return index;
	}
}
