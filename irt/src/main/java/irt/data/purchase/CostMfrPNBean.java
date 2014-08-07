package irt.data.purchase;

import java.util.ArrayList;
import java.util.List;

public class CostMfrPNBean {

	private int id;
	private int selectedIndex;//cost for ...
	private List<CostCompanyBean> costCompanyBeans = new ArrayList<>();

	public int getAlternativeComponentId() {
		return id;
	}
	public CostMfrPNBean setAlternativeComponentId(int id) {
		this.id = id;
		return this;
	}
	public int getSelectedIndex() {
		return selectedIndex;
	}
	public CostMfrPNBean setSelectedIndex(int selectedIndex) {
		this.selectedIndex = selectedIndex;
		return this;
	}
	public List<CostCompanyBean> getCostCompanyBeans() {
		return costCompanyBeans;
	}
	public CostMfrPNBean setCostCompanyBeans(List<CostCompanyBean> costCompanyBeans) {
		this.costCompanyBeans = costCompanyBeans;
		return this;
	}

	@Override
	public boolean equals(Object obj) {
		return obj!=null ? obj.hashCode()==hashCode() : false;
	}

	@Override
	public int hashCode() {
		return id;
	}
	@Override
	public String toString() {
		return "CostMfrPNBean [id=" + id
				+ ", selectedIndex=" + selectedIndex + ", costCompanies="
				+ costCompanyBeans + "]";
	}
}
