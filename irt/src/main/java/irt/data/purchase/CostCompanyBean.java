package irt.data.purchase;

import java.util.ArrayList;
import java.util.List;

public class CostCompanyBean {

	private int id;//Company Id
	private int selectedIndex;
	private List<ForPriceBean> forPriceBeans = new ArrayList<>();

	public int getId() {
		return id;
	}
	public CostCompanyBean setId(int id) {
		this.id = id;
		return this;
	}
	public int getSelectedIndex() {
		return selectedIndex;
	}
	public CostCompanyBean setSelectedIndex(int selectedIndex) {
		this.selectedIndex = selectedIndex;
		return this;
	}
	public List<ForPriceBean> getForPriceBeans() {
		return forPriceBeans;
	}
	public CostCompanyBean setForPriceBeans(List<ForPriceBean> forPriceBeans) {
		this.forPriceBeans = forPriceBeans;
		return this;
	}

	@Override
	public boolean equals(Object obj) {
		return obj!=null ? obj.hashCode()==hashCode(): false;
	}

	@Override
	public int hashCode() {
		return id<0 ? super.hashCode() : id;
	}
	@Override
	public String toString() {
		return "CostCompanyBean [id=" + id
				+ ", selectedIndex=" + selectedIndex + ", forPrices="
				+ forPriceBeans + "]";
	}
}
