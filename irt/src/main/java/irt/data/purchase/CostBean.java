package irt.data.purchase;

import java.util.ArrayList;
import java.util.List;

public class CostBean {

	private int id;
	private String partnamber;
	private String description;
	private List<CostUnitBean> costUnits = new ArrayList<>();
	private boolean isEdit;

	private String classId = "";
	private String componentId = "";
	private int setIndex;
	private boolean isSet;

	public int getId() {
		return id;
	}
	public CostBean setId(int id) {
		this.id = id;
		return this;
	}
	public String getPartnamber() {
		return partnamber;
	}
	public CostBean setPartnamber(String partnamber) {
		this.partnamber = partnamber;
		return this;
	}
	public String getDescription() {
		return description;
	}
	public CostBean setDescription(String description) {
		this.description = description;
		return this;
	}
	public List<CostUnitBean> getCostUnitBeans() {
		return costUnits;
	}
	public CostBean setCostUnitBeans(List<CostUnitBean> costUnits) {
		this.costUnits = costUnits;
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
	public String getComponentId() {
		return componentId;
	}
	public CostBean setComponentId(String componentId) {
		this.componentId = componentId;
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
		return "CostBean [id=" + id + ", partnamber=" + partnamber
				+ ", description=" + description + ", costUnits=" + costUnits
				+ ", isEdit=" + isEdit + ", classId=" + classId
				+ ", componentId=" + componentId
				+ ", setIndex=" + setIndex + ", isSet=" + isSet + "]";
	}
}
