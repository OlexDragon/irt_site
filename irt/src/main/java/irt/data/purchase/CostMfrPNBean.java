package irt.data.purchase;

import java.util.ArrayList;
import java.util.List;

public class CostMfrPNBean {

	private int id;
	private String mfrPN;
	private String mfr;
	private int selectedIndex;//cost for ...
	private List<CostCompanyBean> costCompanyBeans = new ArrayList<>();
	private CostUnitBean alternativeUnit;

	public int getId() {
		return id;
	}
	public CostMfrPNBean setId(int id) {
		this.id = id;
		return this;
	}
	public String getMfrPN() {
		return mfrPN;
	}
	public CostMfrPNBean setMfrPN(String mfrPN) {
		this.mfrPN = mfrPN;
		return this;
	}
	public String getMfr() {
		return mfr;
	}
	public CostMfrPNBean setMfr(String mfr) {
		this.mfr = mfr;
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
	public CostUnitBean getAlternativeUnit() {
		return alternativeUnit;
	}
	public CostMfrPNBean setAlternativeUnit(CostUnitBean alternativeUnit) {
		this.alternativeUnit = alternativeUnit;
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
		return "CostMfrPNBean [id=" + id + ", mfrPN=" + mfrPN + ", mfr=" + mfr
				+ ", selectedIndex=" + selectedIndex + ", costCompanies="
				+ costCompanyBeans + ", alternativeUnit=" + alternativeUnit + "]";
	}
}
