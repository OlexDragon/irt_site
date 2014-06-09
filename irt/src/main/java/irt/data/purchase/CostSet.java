package irt.data.purchase;

import java.util.ArrayList;
import java.util.List;

public class CostSet {

	private int id;
	private int topComponentId;
	private List<CostSetUnit> set = new ArrayList<>();

	public CostSet(int id, int topComponentId) {
		this.id = id;
		this.topComponentId = topComponentId;
	}

	public void add(CostSetUnit costSetUnit){
		if(costSetUnit!=null && costSetUnit.getComponentId()>0 && !set.contains(costSetUnit))
			set.add(costSetUnit);
	}

	public int getId() {
		return id;
	}

	public int getTopComponentId() {
		return topComponentId;
	}

	public List<CostSetUnit> getSet() {
		return set;
	}
}
