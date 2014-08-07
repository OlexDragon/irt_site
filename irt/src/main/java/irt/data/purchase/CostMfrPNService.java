package irt.data.purchase;

import irt.data.components.AlternativeComponent;
import irt.data.dao.ComponentDAO;
import irt.work.ComboBoxField;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CostMfrPNService implements ComboBoxField{

	private static final Logger LOGGER = LogManager.getLogger();

	private final ComponentDAO componentDAO = new ComponentDAO();

	private CostMfrPNBean costMfrPNBean = new CostMfrPNBean();
	private CostCompanyService costCompanyService;

	public CostMfrPNService(int id, String mfr, String mfrPN, CostCompanyBean costCompanyBean) {
		costMfrPNBean.setAlternativeComponentId(id);

		costCompanyService = new CostCompanyService(costCompanyBean);
		ArrayList<CostCompanyBean> costCompanyBeans = new ArrayList<>();
		if(costCompanyService.isSet())
			costCompanyBeans.add(costCompanyBean);
		costMfrPNBean.setCostCompanyBeans(costCompanyBeans);
	}

	public CostMfrPNService() {}

	public int getId() {
		return costMfrPNBean.getAlternativeComponentId();
	}

	public String getMfrPN() {
		AlternativeComponent alternativeComponent = componentDAO.getAlternativeComponent(costMfrPNBean.getAlternativeComponentId());
		return alternativeComponent!=null ? alternativeComponent.getMfrPN() : null;
	}

	public String getMfr() {
		AlternativeComponent alternativeComponent = componentDAO.getAlternativeComponent(costMfrPNBean.getAlternativeComponentId());
		return alternativeComponent!=null ? alternativeComponent.getMfrId() : null;
	}

	public int getSelectedCompany() {
		return costMfrPNBean.getSelectedIndex();
	}

	public void setSelectedIndex(int index) {
		if(index>=0)
			costMfrPNBean.setSelectedIndex(index);
	}

	public boolean isSet(){
		return costMfrPNBean.getAlternativeComponentId()>=0;
	}

	public static boolean isSet(CostMfrPNBean costMfrPNBean){
		return costMfrPNBean.getAlternativeComponentId()>=0;
	}

	public List<CostCompanyBean> getCostCompanies() {
		return costMfrPNBean.getCostCompanyBeans();
	}

	public void add(CostCompanyBean costCompanyBean){
		add(costMfrPNBean, costCompanyBean);
	}

	public static void add(CostMfrPNBean costMfrPNBean, CostCompanyBean costCompanyBean){
		if(costCompanyBean!=null) {
			List<CostCompanyBean> costCompanies = costMfrPNBean.getCostCompanyBeans();

			costCompanies.remove(new CostCompanyBean());

			if(costCompanies.contains(costCompanyBean)){
				costMfrPNBean.setSelectedIndex(costCompanies.indexOf(costCompanyBean));
				CostCompanyService.add(costCompanies.get(costMfrPNBean.getSelectedIndex()), costCompanyBean.getForPriceBeans());
			}else
				if(CostCompanyService.isSet(costCompanyBean)){
					costCompanies.add(costCompanyBean);
					costMfrPNBean.setSelectedIndex(costCompanies.indexOf(costCompanyBean));
				}
		}
	}

	public static void remove(CostMfrPNBean costMfrPNBean, CostCompanyBean costCompanyBean) {
		if(costCompanyBean!=null) {
			List<CostCompanyBean> costCompanies = costMfrPNBean.getCostCompanyBeans();

			costCompanies.remove(costCompanyBean);
		}
	}

	public static void add(CostMfrPNBean costMfrPNBean, List<CostCompanyBean> costCompanies) {
		if(costCompanies!=null)
			for(CostCompanyBean cc:costCompanies)
				add(costMfrPNBean, cc);
	}

	public static BigDecimal getPrice(CostMfrPNBean costMfrPNBean) {
		List<CostCompanyBean> costCompanies = costMfrPNBean.getCostCompanyBeans();
		return costCompanies.isEmpty() ? null : CostCompanyService.getPrice(costCompanies.get(costMfrPNBean.getSelectedIndex()));
	}

	public BigDecimal getPrice() {
		return getPrice(costMfrPNBean);
	}

	public static String getCompanyName(CostMfrPNBean costMfrPNBean) {
		List<CostCompanyBean> costCompanies = costMfrPNBean.getCostCompanyBeans();
		return costCompanies.isEmpty() ? null : CostCompanyService.getName(costCompanies.get(costMfrPNBean.getSelectedIndex()).getId());
	}

	public String getCompanyName() {
		return getCompanyName(costMfrPNBean);
	}

	public static int getCompanyId(CostMfrPNBean costMfrPNBean) {
		List<CostCompanyBean> costCompanies = costMfrPNBean.getCostCompanyBeans();
		return costCompanies.isEmpty() ? -1 : costCompanies.get(costMfrPNBean.getSelectedIndex()).getId();
	}

	public int getCompanyId() {
		return getCompanyId(costMfrPNBean);
	}

	public static List<ForPriceBean> getForPrices(CostMfrPNBean costMfrPNBean) {
		List<CostCompanyBean> costCompanies = costMfrPNBean.getCostCompanyBeans();
		return costCompanies.isEmpty() ? null : costCompanies.get(costMfrPNBean.getSelectedIndex()).getForPriceBeans();
	}

	public List<ForPriceBean> getForPrices() {
		return getForPrices(costMfrPNBean);
	}

	public static int getSelectedForPriceIndex(CostMfrPNBean costMfrPNBean) {
		List<CostCompanyBean> costCompanies = costMfrPNBean.getCostCompanyBeans();
		return costCompanies.isEmpty() ? -1 : costCompanies.get(costMfrPNBean.getSelectedIndex()).getSelectedIndex();
	}

	public int getSelectedForPriceIndex() {
		return getSelectedForPriceIndex(costMfrPNBean);
	}

	public static int getForUnits(CostMfrPNBean costMfrPNBean) {
		List<CostCompanyBean> costCompanies = costMfrPNBean.getCostCompanyBeans();
		return costCompanies.isEmpty() ? -1 : CostCompanyService.getForUnits(costCompanies.get(costMfrPNBean.getSelectedIndex()));
	}

	public int getForUnits() {
		return getForUnits(costMfrPNBean);
	}

	public static void setForUnits(CostMfrPNBean costMfrPNBean, int forUnit) {
		List<CostCompanyBean> costCompanies = costMfrPNBean.getCostCompanyBeans();
		if(!costCompanies.isEmpty())
			CostCompanyService.setForUnits(costCompanies.get(costMfrPNBean.getSelectedIndex()), forUnit);
	}

	public void setForUnits(int forUnit) {
		setForUnits(costMfrPNBean, forUnit);
	}

	public static boolean isChanged(CostMfrPNBean costMfrPNBean) {
		boolean isChanged = false;
		for(CostCompanyBean cc:costMfrPNBean.getCostCompanyBeans())
			if(CostCompanyService.isChanged(cc)){
				isChanged = true;
				break;
			}
		return isChanged;
	}

	public boolean isChanged() {
		return isChanged(costMfrPNBean);
	}

	public static void setPrice(CostMfrPNBean costMfrPNBean, BigDecimal price) {
		List<CostCompanyBean> costCompanies = costMfrPNBean.getCostCompanyBeans();
		if(!costCompanies.isEmpty())
			CostCompanyService.setPrice(costCompanies.get(costMfrPNBean.getSelectedIndex()), price);
	}

	public void setPrice(BigDecimal price) {
		setPrice(costMfrPNBean, price);
	}

	public static boolean setForPriceIndex(CostMfrPNBean costMfrPNBean, int index) {
		boolean isSet = false;
		List<CostCompanyBean> costCompanies = costMfrPNBean.getCostCompanyBeans();
		if(!costCompanies.isEmpty())
			isSet = CostCompanyService.setSelectedForPriceIndex(costCompanies.get(costMfrPNBean.getSelectedIndex()), index);
		return isSet;
	}

	public boolean setForPriceIndex(int index) {
		return setForPriceIndex(costMfrPNBean, index);
	}

	public void add(CostUnitBean alternativeUnit) {
//		if(CostUnitService.isSet(alternativeUnit))
//			costMfrPNBean.setAlternativeUnit(alternativeUnit);
		//TODO
	}

	public CostUnitBean getAlternativeUnit() {
		return null;//TODO costMfrPNBean.getAlternativeUnit();
	}

	@Override
	public boolean equals(Object obj) {
		return obj!=null ? obj.hashCode()==hashCode() : false;
	}

	@Override
	public int hashCode() {
		return costMfrPNBean.getAlternativeComponentId();
	}

	@Override
	public String getValue() {
		return ""+costMfrPNBean.getAlternativeComponentId();
	}

	@Override
	public String getText() {
		return getMfrPN();
	}

	public static CostCompanyBean getCostCompanyBean(CostMfrPNBean costMfrPNBean) {
		List<CostCompanyBean> costCompanies = costMfrPNBean.getCostCompanyBeans();
		return costCompanies.isEmpty() ? null : costCompanies.get(costMfrPNBean.getSelectedIndex());
	}

	public static List<CostCompanyService> getCostCompanyServices(CostMfrPNBean costMfrPNBean) {

		List<CostCompanyService> costCompanyServices = new ArrayList<>();
		List<CostCompanyBean> costCompanyBeans = costMfrPNBean.getCostCompanyBeans();

		for(CostCompanyBean ccb:costCompanyBeans)
			costCompanyServices.add(new CostCompanyService(ccb));

		return costCompanyServices;
	}

	public CostCompanyBean getCompany() {
		return getCostCompanyBean(costMfrPNBean);
	}

	public CostMfrPNBean getCostMfrPNBean() {
		return costMfrPNBean;
	}

	public CostMfrPNService setCostMfrPNBean(CostMfrPNBean costMfrPNBean) {
		this.costMfrPNBean = costMfrPNBean;
		return this;
	}

	public static void reset(CostMfrPNBean costMfrPNBean) {
		costMfrPNBean.setSelectedIndex(0);
		CostCompanyBean company = getCostCompanyBean(costMfrPNBean);
		if(company!=null)
			company.setSelectedIndex(0);
	}

	public void reset() {
		costMfrPNBean.setSelectedIndex(0);
		CostCompanyBean company = getCompany();
		if(company!=null)
			company.setSelectedIndex(0);
	}

	@Override
	public String toString() {
		return "CostMfrPN [costMfrPNBean=" + costMfrPNBean + "]";
	}

	public static ComboBoxField[] toArray( ArrayList<CostMfrPNBean> mfrPartNumbers) {
		ComboBoxField[] comboBoxFields;

		LOGGER.trace("{}", mfrPartNumbers);

		if(mfrPartNumbers!=null){
			int size = mfrPartNumbers.size();
			comboBoxFields = new CostMfrPNService[size];
			for(int fieldIndex=0; fieldIndex<size; fieldIndex++)
				comboBoxFields[fieldIndex] = new CostMfrPNService().setCostMfrPNBean(mfrPartNumbers.get(fieldIndex));
		}else
			comboBoxFields = null;

		return comboBoxFields;
	}
}
