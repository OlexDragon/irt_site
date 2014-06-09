package irt.data.purchase;

import java.util.ArrayList;
import java.util.List;

public class CostUnit {
	private int componentId;
	private String partNumberStr;
	private String description;
	private int selectedIndex;
	private int qty;
	private ArrayList<CostMfrPN> mfrPartNumbers = new ArrayList<>();

	public CostUnit(int componentId, int alternativeId, String partNumberStr, String description, CostMfrPN mfrPartNumber, int qty) {
		this.componentId = componentId;
		this.partNumberStr = partNumberStr;
		this.description = description;
		if (mfrPartNumber != null && mfrPartNumber.isSet()){
			mfrPartNumbers.add(mfrPartNumber);
			if(alternativeId>0)
				mfrPartNumber.add(new CostUnit(alternativeId, 0, partNumberStr, description, mfrPartNumber, qty));
		}
		this.qty = qty;
	}

	public int getQty() {
		CostUnit alternativeUnit = mfrPartNumbers.get(selectedIndex).getAlternativeUnit();
		return selectedIndex == 0 || alternativeUnit==null ? qty :alternativeUnit.getQty();
	}

	public int getComponentId() {
		return componentId;
	}

	public int getComponentId(CostMfrPN cmpn) {
		CostUnit alternativeUnit = cmpn.getAlternativeUnit();
		return alternativeUnit==null ? componentId : alternativeUnit.getComponentId();
	}

	public String getPartNumberStr() {
		CostUnit alternativeUnit = null;
		if(selectedIndex != 0)
			alternativeUnit = mfrPartNumbers.get(selectedIndex).getAlternativeUnit();
		return alternativeUnit==null ? partNumberStr : alternativeUnit.getPartNumberStr();
	}

	public String getDescription() {
		CostUnit alternativeUnit = null;
		if(selectedIndex != 0)
			alternativeUnit = mfrPartNumbers.get(selectedIndex).getAlternativeUnit();
		return alternativeUnit==null ? description : alternativeUnit.getDescription();
	}

	@Override
	public boolean equals(Object obj) {
		return obj!=null ? obj.hashCode()==hashCode() : false;
	}

	@Override
	public int hashCode() {
		return componentId;
	}

	public int getSelectedMfrPN() {
		return selectedIndex;
	}

	public ArrayList<CostMfrPN> getMfrPartNumbers() {
		return mfrPartNumbers;
	}

	public void add(ArrayList<CostMfrPN> mfrPartNumbers) {
		if(mfrPartNumbers!=null)
			for(CostMfrPN cmpn:mfrPartNumbers)
				if(this.mfrPartNumbers.contains(cmpn))
					this.mfrPartNumbers.get(this.mfrPartNumbers.indexOf(cmpn)).add(cmpn.getCostCompanies());
				else if(cmpn.isSet())
					this.mfrPartNumbers.add(cmpn);
	}

	public Price getPrice() {
		return mfrPartNumbers.isEmpty() ? null : mfrPartNumbers.get(selectedIndex).getPrice();
	}

	public CostMfrPN getMfrPartNumber() {
		return mfrPartNumbers.isEmpty() ? null : mfrPartNumbers.get(selectedIndex);
	}

	public String getMfrPN() {
		return mfrPartNumbers.isEmpty() ? null : mfrPartNumbers.get(selectedIndex).getMfrPN();
	}

	public String getMfr() {
		return mfrPartNumbers.isEmpty() ? null : mfrPartNumbers.get(selectedIndex).getMfr();
	}

	public boolean isSet() {
		return componentId>0 && partNumberStr!=null && !partNumberStr.isEmpty();
	}

	public List<CostCompany> getCompanies() {
		return mfrPartNumbers.isEmpty() ? null : mfrPartNumbers.get(selectedIndex).getCostCompanies();
	}

	public String getCompanyName() {
		return mfrPartNumbers.isEmpty() ? null : mfrPartNumbers.get(selectedIndex).getCompanyName();
	}

	public int getCompanyId() {
		return mfrPartNumbers.isEmpty() ? null : mfrPartNumbers.get(selectedIndex).getCompanyId();
	}

	public List<ForPrice> getForPrices() {
		return mfrPartNumbers.isEmpty() ? null : mfrPartNumbers.get(selectedIndex).getForPrices();
	}

	public int getSelectedForPrice() {
		return mfrPartNumbers.isEmpty() ? -1 : mfrPartNumbers.get(selectedIndex).getSelectedForPriceIndex();
	}

	public int getForUnits() {
		return mfrPartNumbers.isEmpty() ? -1 : mfrPartNumbers.get(selectedIndex).getForUnits();
	}

	public void setForUnits(int forUnit) {
		if(!mfrPartNumbers.isEmpty())
			mfrPartNumbers.get(selectedIndex).setForUnits(forUnit);
	}

	public boolean isChanged() {
		boolean isChanged = false;
		for(CostMfrPN cmpn:mfrPartNumbers)
			if(cmpn.isChanged()){
				isChanged = true;
				break;
			}
		return isChanged;
	}

	public void setPrice(Price price) {
		if(!mfrPartNumbers.isEmpty())
			mfrPartNumbers.get(selectedIndex).setPrice(price);
	}

	public void setSelectedMfrPN(int selectedIndex) {
		if(selectedIndex>=0)
			this.selectedIndex = selectedIndex;
	}

	public void setSelectedCompanyIndex(int index) {
		if(!mfrPartNumbers.isEmpty())
			mfrPartNumbers.get(selectedIndex).setSelectedIndex(index);
	}

	public boolean setForPriceIndex(int index) {
		boolean isSet = false;
		if(!mfrPartNumbers.isEmpty())
			isSet = mfrPartNumbers.get(selectedIndex).setForPriceIndex(index);
		return isSet;
	}

	public int getMfrPartNumberIndex() {
		return mfrPartNumbers.isEmpty() ? null : mfrPartNumbers.get(selectedIndex).getId();
	}

	@Override
	public String toString() {
		return "CostUnit [componentId=" + componentId + ", partNumberStr="
				+ partNumberStr + ", description=" + description
				+ ", selectedIndex=" + selectedIndex + ", qty=" + qty
				+ ", mfrPartNumbers=" + mfrPartNumbers + "]";
	}

	public int getSelectedCompany() {
		return getMfrPartNumber().getSelectedCompany();
	}

	public void reset() {
		selectedIndex = 0;
		getMfrPartNumber().reset();
	}

	public void setSelectedForPrice(int moqIndex) {
		CostMfrPN mfrPartNumber = getMfrPartNumber();
		if(mfrPartNumber!=null){
			CostCompany company = mfrPartNumber.getCompany();
			if(company!=null)
				company.setSelectedForPriceIndex(moqIndex);
		}
	}
}
