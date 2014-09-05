package irt.data.purchase;

import java.util.ArrayList;

public class CostUnitBean {
	private int componentId;
	private int selectedIndex;
	private int qty;
	private ArrayList<AlternativeComponentBean> mfrPartNumbers = new ArrayList<>();

	public int getComponentId() {
		return componentId;
	}
	public CostUnitBean setComponentId(int componentId) {
		this.componentId = componentId;
		return this;
	}
	public int getSelectedIndex() {
		return selectedIndex;
	}
	public CostUnitBean setSelectedIndex(int selectedIndex) {
		this.selectedIndex = selectedIndex;
		return this;
	}
	public int getQty() {
		return qty;
	}
	public CostUnitBean setQty(int qty) {
		this.qty = qty;
		return this;
	}
	public ArrayList<AlternativeComponentBean> getAlternativeComponentBeans() {
		return mfrPartNumbers;
	}
	public CostUnitBean setMfrPartNumbers(ArrayList<AlternativeComponentBean> mfrPartNumbers) {
		this.mfrPartNumbers = mfrPartNumbers;
		return this;
	}
	@Override
	public boolean equals(Object obj) {
		return obj!=null ? obj.hashCode()==hashCode() : false;
	}
	@Override
	public int hashCode() {
		return componentId;
	}
	@Override
	public String toString() {
		return "CostUnitBean [componentId=" + componentId + ", mfrPartNumbers=" + mfrPartNumbers + ", qty=" + qty + ", selectedIndex=" + selectedIndex + "]";
	}
}
