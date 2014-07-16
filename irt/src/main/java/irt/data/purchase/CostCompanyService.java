package irt.data.purchase;

import irt.work.ComboBoxField;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class CostCompanyService implements ComboBoxField{

	private CostCompanyBean costCompanyBean;
	private final ForPriceService forPriceService = new ForPriceService();

	public CostCompanyService(int id, String name, ForPriceBean forPriceBean) {

		costCompanyBean = new CostCompanyBean();
		costCompanyBean.setId(id);
		costCompanyBean.setName(name);

		forPriceService.setForPriceBean(forPriceBean);
		List<ForPriceBean> forPriceBeans = new ArrayList<>();
		if(forPriceService.isSet())
			forPriceBeans.add(forPriceBean);
		costCompanyBean.setForPriceBeans(forPriceBeans);
	}

	public CostCompanyService(CostCompanyBean costCompanyBean) {
		this.costCompanyBean = costCompanyBean;
	}

	public int getId() {
		return costCompanyBean.getId();
	}

	public String getName() {
		return costCompanyBean.getName();
	}

	@Override
	public String getValue() {
		return Integer.toString(costCompanyBean.getId());
	}

	@Override
	public String getText() {
		return costCompanyBean.getName();
	}

	public int getSelectedForPriceIndex() {
		return costCompanyBean.getSelectedIndex();
	}

	public ForPriceBean getForPricBeane() {
		List<ForPriceBean> forPriceBeans = costCompanyBean.getForPriceBeans();
		return forPriceBeans.isEmpty() ? null : forPriceBeans.get(costCompanyBean.getSelectedIndex());
	}

	public static boolean isSet(CostCompanyBean costCompanyBean) {
		return costCompanyBean.getId()>=0 && costCompanyBean.getName()!=null;
	}

	public boolean isSet() {
		return isSet(costCompanyBean);
	}

	public static void add(CostCompanyBean costCompanyBean, List<ForPriceBean> forPriceBeans) {
		if(forPriceBeans!=null){
			for(ForPriceBean fp:forPriceBeans){
				List<ForPriceBean> f = costCompanyBean.getForPriceBeans();
				if (fp != null)
					if (f.contains(fp))
						f.set(f.indexOf(fp), fp);
					else if (ForPriceService.isSet(fp))
						f.add(fp);
			}
		}
	}

	public static void addForPriceBean(CostCompanyBean costCompanyBean, ForPriceBean forPriceBean) {
		if(forPriceBean!=null)
			costCompanyBean.getForPriceBeans().add(forPriceBean);
	}

	public static BigDecimal getPrice(CostCompanyBean costCompanyBean) {
		List<ForPriceBean> forPrices = costCompanyBean.getForPriceBeans();
		return forPrices.isEmpty() ? null : ForPriceService.getPrice(forPrices.get(costCompanyBean.getSelectedIndex()));
	}

	public static int getForUnits(CostCompanyBean costCompanyBean) {
		List<ForPriceBean> forPrices = costCompanyBean.getForPriceBeans();
		return forPrices.isEmpty() ? -1 : forPrices.get(costCompanyBean.getSelectedIndex()).getForUnits();
	}

	public static void setForUnits(CostCompanyBean costCompanyBean, int forUnit) {
		if(!costCompanyBean.getForPriceBeans().isEmpty())
			costCompanyBean.getForPriceBeans().get(costCompanyBean.getSelectedIndex()).setForUnits(forUnit);
	}

	public static boolean isChanged(CostCompanyBean costCompanyBean) {
		boolean isChanged = false;
		for(ForPriceBean fp:costCompanyBean.getForPriceBeans())
			if(ForPriceService.isChanged(fp)){
				isChanged = true;
				break;
			}
		return isChanged;
	}

	public boolean isChanged() {
		return isChanged(costCompanyBean);
	}

	public static void setPrice(CostCompanyBean costCompanyBean, BigDecimal price) {
		List<ForPriceBean> forPrices = costCompanyBean.getForPriceBeans();
		if(!forPrices.isEmpty())
			forPrices.get(costCompanyBean.getSelectedIndex()).setPrice(price);
	}

	@Override
	public boolean equals(Object obj) {
		return obj!=null ? obj.hashCode()==hashCode(): false;
	}

	@Override
	public int hashCode() {
		return costCompanyBean.hashCode();
	}

	public static boolean setSelectedForPriceIndex(CostCompanyBean costCompanyBean, int index) {
		boolean isSet = false;
		if(index>=0 && index<costCompanyBean.getForPriceBeans().size()){
			isSet = true;
			costCompanyBean.setSelectedIndex(index);
		}
		return isSet;
	}

	public boolean setSelectedForPriceIndex(int index) {
		return setSelectedForPriceIndex(costCompanyBean, index);
	}

	public void reset() {
		costCompanyBean.setSelectedIndex(0);
	}

	@Override
	public String toString() {
		return "CostCompany [costCompanyBean=" + costCompanyBean + "]";
	}

	public static void remove(CostCompanyBean costCompanyBean, ForPriceBean forPriceBean) {
		costCompanyBean.getForPriceBeans().remove(forPriceBean);
	}
}
