package irt.data.purchase;

import irt.work.ComboBoxField;

import java.util.ArrayList;
import java.util.List;

public class CostMfrPN implements ComboBoxField{

	private int id;
	private String mfrPN;
	private String mfr;
	private int selectedIndex;//cost for ...
	private List<CostCompany> costCompanies;
	private CostUnit alternativeUnit;

	public CostMfrPN(int id, String mfr, String mfrPN, CostCompany costCompany) {
		this.id = id;
		this.mfr = mfr;
		this.mfrPN = mfrPN;
		costCompanies = new ArrayList<>();
		if(costCompany.isSet())
			costCompanies.add(costCompany);
	}

	public int getId() {
		return id;
	}

	public String getMfrPN() {
		return mfrPN;
	}

	public String getMfr() {
		return mfr;
	}

	public int getSelectedCompany() {
		return selectedIndex;
	}

	public void setSelectedIndex(int index) {
		if(index>=0)
			this.selectedIndex = index;
	}

	public boolean isSet(){
		return id>=0;
	}

	public List<CostCompany> getCostCompanies() {
		return costCompanies;
	}

	public void add(CostCompany costCompany){
		if(costCompany!=null && costCompany.getName()!=null)
			if(costCompanies.contains(costCompany)){
				selectedIndex = costCompanies.indexOf(costCompany);
				costCompanies.get(selectedIndex).add(costCompany.getForPrices());
			}else
				if(costCompany.isSet()){
					costCompanies.add(costCompany);
					selectedIndex = costCompanies.indexOf(costCompany);
				}
	}

	public void add(List<CostCompany> costCompanies) {
		if(costCompanies!=null)
			for(CostCompany cc:costCompanies)
				add(cc);
	}

	public Price getPrice() {
		return costCompanies.isEmpty() ? null : costCompanies.get(selectedIndex).getPrice();
	}

	public String getCompanyName() {
		return costCompanies.isEmpty() ? null : costCompanies.get(selectedIndex).getName();
	}

	public int getCompanyId() {
		return costCompanies.isEmpty() ? -1 : costCompanies.get(selectedIndex).getId();
	}

	public List<ForPrice> getForPrices() {
		return costCompanies.isEmpty() ? null : costCompanies.get(selectedIndex).getForPrices();
	}

	public int getSelectedForPriceIndex() {
		return costCompanies.isEmpty() ? -1 : costCompanies.get(selectedIndex).getSelectedForPriceIndex();
	}

	public int getForUnits() {
		return costCompanies.isEmpty() ? -1 : costCompanies.get(selectedIndex).getForUnits();
	}

	public void setForUnits(int forUnit) {
		if(!costCompanies.isEmpty())
			costCompanies.get(selectedIndex).setForUnits(forUnit);
	}

	public boolean isChanged() {
		boolean isChanged = false;
		for(CostCompany cc:costCompanies)
			if(cc.isChanged()){
				isChanged = true;
				break;
			}
		return isChanged;
	}

	public void setPrice(Price price) {
		if(!costCompanies.isEmpty())
			costCompanies.get(selectedIndex).setPrice(price);
	}

	public boolean setForPriceIndex(int index) {
		boolean isSet = false;
		if(!costCompanies.isEmpty())
			isSet = costCompanies.get(selectedIndex).setSelectedForPriceIndex(index);
		return isSet;
	}

	public void add(CostUnit alternativeUnit) {
		if(alternativeUnit.isSet())
			this.alternativeUnit = alternativeUnit;
	}

	public CostUnit getAlternativeUnit() {
		return alternativeUnit;
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
	public String getValue() {
		return ""+id;
	}

	@Override
	public String getText() {
		return mfrPN;
	}

	@Override
	public String toString() {
		return "CostMfrPN [id=" + id + ", mfrPN=" + mfrPN + ", mfr=" + mfr
				+ ", selectedIndex=" + selectedIndex + ", costCompanies="
				+ costCompanies + "]";
	}

	public CostCompany getCompany() {
		return costCompanies.isEmpty() ? null : costCompanies.get(selectedIndex);
	}

	public void reset() {
		selectedIndex = 0;
		CostCompany company = getCompany();
		if(company!=null)
			company.reset();
	}
}
