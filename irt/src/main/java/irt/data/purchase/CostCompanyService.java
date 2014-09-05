package irt.data.purchase;

import irt.data.companies.Company;
import irt.data.dao.CompanyDAO;
import irt.work.ComboBoxField;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CostCompanyService implements ComboBoxField{

	private static final Logger logger = LogManager.getLogger();

	private static final CompanyDAO COMPANY_DAO = new CompanyDAO();
	private CostCompanyBean costCompanyBean;
	private final ForPriceService forPriceService = new ForPriceService();

	public CostCompanyService(int id, ForPriceBean forPriceBean) {

		costCompanyBean = new CostCompanyBean();
		costCompanyBean.setId(id);

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
		String companyName = null;

		if(costCompanyBean!=null){
			Company company = COMPANY_DAO.getCompany(costCompanyBean.getId());
			if(company!=null)
				companyName = company.getName();
		}
		return companyName;
	}

	public static String getCompanyName(int companyId) {
		String companyName = null;

		Company company = COMPANY_DAO.getCompany(companyId);
		if(company!=null)
			companyName = company.getCompanyName();

		return companyName;
	}

	public static Company getCompany(CostCompanyBean costCompanyBean) {
		return costCompanyBean!=null ? COMPANY_DAO.getCompany(costCompanyBean.getId()) : null;
	}

	@Override
	public String getValue() {
		return Integer.toString(costCompanyBean.getId());
	}

	@Override
	public String getText() {
		return getName();
	}

	public int getSelectedForPriceIndex() {
		return costCompanyBean.getSelectedIndex();
	}

	public ForPriceBean getForPricBeane() {
		List<ForPriceBean> forPriceBeans = costCompanyBean.getForPriceBeans();
		return forPriceBeans.isEmpty() ? null : forPriceBeans.get(costCompanyBean.getSelectedIndex());
	}

	public static boolean isSet(CostCompanyBean costCompanyBean) {
		return costCompanyBean.getId()>=0;
	}

	public boolean isSet() {
		return isSet(costCompanyBean);
	}

	public static boolean add(List<ForPriceBean> newForPriceBeans, List<ForPriceBean> forPriceBeans) {
		logger.entry("\n\t", newForPriceBeans, "\n\t", forPriceBeans);

		boolean added = false;
		if(forPriceBeans!=null && newForPriceBeans!=null){

			for(ForPriceBean fpb:newForPriceBeans){

				int indexOf = forPriceBeans.indexOf(fpb);
				logger.trace("\n\tindexOf = {}", indexOf);

				if (indexOf>=0){
					ForPriceBean forPriceBean = forPriceBeans.get(indexOf);

					BigDecimal newPrice = fpb.getNewPrice();
					BigDecimal price = fpb.getPrice();
					added = price!=null ? price.compareTo(forPriceBean.getPrice())!=0 : forPriceBean.getPrice()!=null ||
							newPrice!=null
								? forPriceBean.getNewPrice()==null || newPrice.compareTo(forPriceBean.getNewPrice())!=0
								: forPriceBean.getNewPrice()!=null;

					if(added){
						forPriceBean.setNewPrice(newPrice);
						forPriceBean.setPrice(price);
					}
				}else if (ForPriceService.isSet(fpb))
					added = forPriceBeans.add(fpb);
			}
		}
		return logger.exit(added);
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

		if(costCompanyBean!=null)
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
		logger.entry(costCompanyBean, price);
		List<ForPriceBean> forPrices = costCompanyBean.getForPriceBeans();
		if(!forPrices.isEmpty())
			ForPriceService.setPrice(forPrices.get(costCompanyBean.getSelectedIndex()), price);
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
