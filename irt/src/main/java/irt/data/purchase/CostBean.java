package irt.data.purchase;

import java.util.ArrayList;
import java.util.List;

public class CostBean {

	private int topComponentId;
	private List<CostUnitBean> costUnits = new ArrayList<>();
	private boolean isEdit;

	private String classId = "";
	private int setIndex;
	private boolean isSet;

	public List<CostUnitBean> getCostUnitBeans() {
		return costUnits;
	}
	public CostBean setCostUnitBeans(List<CostUnitBean> costUnits) {
		this.costUnits = costUnits;
		return this;
	}
	public int getTopComponentId() {
		return topComponentId;
	}
	public CostBean setTopComponentId(int topComponentId) {
		this.topComponentId = topComponentId;
		return this;
	}
	public boolean isEdit() {
		return isEdit;
	}
	public CostBean setEdit(boolean isEdit) {
		this.isEdit = isEdit;
		return this;
	}
	public String getClassId() {
		return classId;
	}
	public CostBean setClassId(String classId) {
		this.classId = classId;
		return this;
	}
	public int getSetIndex() {
		return setIndex;
	}
	public CostBean setSetIndex(int setIndex) {
		this.setIndex = setIndex;
		return this;
	}
	public boolean isSet() {
		return isSet;
	}
	public CostBean setSet(boolean isSet) {
		this.isSet = isSet;
		return this;
	}
	@Override
	public String toString() {
		return "CostBean [topComponentId=" + topComponentId + ", costUnits="
				+ costUnits + ", isEdit=" + isEdit + ", classId=" + classId
				+ ", setIndex=" + setIndex + ", isSet=" + isSet + "]";
	}
}
