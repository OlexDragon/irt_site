package irt.data.purchase;

import java.util.ArrayList;

public class CostUnitBean {
	private int componentId;
	private int alternativeComponentId;
	private String partNumberStr;
	private String description;
	private int selectedIndex;
	private int qty;
	private ArrayList<CostMfrPNBean> mfrPartNumbers = new ArrayList<>();

	public int getComponentId() {
		return componentId;
	}
	public CostUnitBean setComponentId(int componentId) {
		this.componentId = componentId;
		return this;
	}
	public int getAlternativeComponentId() {
		return alternativeComponentId;
	}
	public CostUnitBean setAlternativeComponentId(int alternativeComponentId) {
		this.alternativeComponentId = alternativeComponentId;
		return this;
	}
	public String getPartNumberStr() {
		return partNumberStr;
	}
	public CostUnitBean setPartNumberStr(String partNumberStr) {
		this.partNumberStr = partNumberStr;
		return this;
	}
	public String getDescription() {
		return description;
	}
	public CostUnitBean setDescription(String description) {
		this.description = description;
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
	public ArrayList<CostMfrPNBean> getMfrPartNumbers() {
		return mfrPartNumbers;
	}
	public CostUnitBean setMfrPartNumbers(ArrayList<CostMfrPNBean> mfrPartNumbers) {
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
		return "CostUnitBean [componentId=" + componentId
				+ ", alternativeComponentId=" + alternativeComponentId
				+ ", partNumberStr=" + partNumberStr + ", mfrPartNumbers="
				+ mfrPartNumbers + ", description=" + description + ", qty="
				+ qty + ", selectedIndex=" + selectedIndex + "]";
	}
}
