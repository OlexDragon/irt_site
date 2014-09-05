package irt.data.purchase;

import java.util.ArrayList;
import java.util.List;

public class AlternativeComponentBean {

	private int alternativeComponentId;
	private int selectedIndex;//cost for ...
	private List<CostCompanyBean> costCompanyBeans = new ArrayList<>();

	public int getAlternativeComponentId() {
		return alternativeComponentId;
	}
	public AlternativeComponentBean setAlternativeComponentId(int alternativeComponentId) {
		this.alternativeComponentId = alternativeComponentId;
		return this;
	}
	public int getSelectedIndex() {
		return selectedIndex;
	}
	public AlternativeComponentBean setSelectedIndex(int selectedIndex) {
		this.selectedIndex = selectedIndex;
		return this;
	}
	public List<CostCompanyBean> getCostCompanyBeans() {
		return costCompanyBeans;
	}
	public AlternativeComponentBean setCostCompanyBeans(List<CostCompanyBean> costCompanyBeans) {
		this.costCompanyBeans = costCompanyBeans;
		return this;
	}

	@Override
	public boolean equals(Object obj) {
		return obj!=null ? obj.hashCode()==hashCode() : false;
	}
	@Override
	public int hashCode() {
		return alternativeComponentId;
	}
	@Override
	public String toString() {
		return "AlternativeComponentBean [alternativeComponentId="+ alternativeComponentId + ", selectedIndex=" + selectedIndex + ", costCompanyBeans=" + costCompanyBeans + "]";
	}
}
