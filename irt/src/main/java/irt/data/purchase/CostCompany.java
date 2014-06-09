package irt.data.purchase;

import irt.work.ComboBoxField;

import java.util.ArrayList;
import java.util.List;

public class CostCompany implements ComboBoxField{

	private int id;//Company Id
	private String name;//Company Name
	private int selectedIndex;
	private List<ForPrice> forPrices;

	public CostCompany(int id, String name, ForPrice forPrice) {

		this.id = id;
		this.name = name;
		forPrices = new ArrayList<>();
		if(forPrice.isSet())
			forPrices.add(forPrice);
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	@Override
	public String getValue() {
		return Integer.toString(id);
	}

	@Override
	public String getText() {
		return name;
	}

	public int getSelectedForPriceIndex() {
		return selectedIndex;
	}

	public List<ForPrice> getForPrices() {
		return forPrices;
	}

	public ForPrice getForPrice() {
		return forPrices.isEmpty() ? null : forPrices.get(selectedIndex);
	}

	public boolean isSet() {
		return id>=0 && name!=null;
	}

	public void add(List<ForPrice> forPrices) {
		if(forPrices!=null)
			for(ForPrice fp:forPrices)
				if(this.forPrices.contains(fp))
					this.forPrices.set(this.forPrices.indexOf(fp),fp);
				else if(fp.isSet())
					this.forPrices.add(fp);
	}

	public Price getPrice() {
		return forPrices.isEmpty() ? null : forPrices.get(selectedIndex).getPrice();
	}

	public int getForUnits() {
		return forPrices.isEmpty() ? -1 : forPrices.get(selectedIndex).getForUnits();
	}

	public void setForUnits(int forUnit) {
		if(!forPrices.isEmpty())
			forPrices.get(selectedIndex).setForUnits(forUnit);
	}

	public boolean isChanged() {
		boolean isChanged = false;
		for(ForPrice fp:forPrices)
			if(fp.isChanged()){
				isChanged = true;
				break;
			}
		return isChanged;
	}

	public void setPrice(Price price) {
		if(!forPrices.isEmpty())
			forPrices.get(selectedIndex).setPrice(price);
	}

	@Override
	public boolean equals(Object obj) {
		return obj!=null ? obj.hashCode()==hashCode(): false;
	}

	@Override
	public int hashCode() {
		return id<0 ? super.hashCode() : id;
	}

	public boolean setSelectedForPriceIndex(int index) {
		boolean isSet = false;
		if(index>=0 && index<forPrices.size()){
			isSet = true;
			selectedIndex = index;
		}
		return isSet;
	}

	@Override
	public String toString() {
		return "CostCompany [id=" + id + ", name=" + name + ", selectedIndex="
				+ selectedIndex + ", forPrices=" + forPrices + ", isSet()="
				+ isSet() + "]";
	}

	public void reset() {
		selectedIndex = 0;
	}
}
