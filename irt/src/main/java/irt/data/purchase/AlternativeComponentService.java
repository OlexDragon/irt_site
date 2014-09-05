package irt.data.purchase;

import irt.data.components.AlternativeComponent;
import irt.data.dao.ComponentDAO;
import irt.work.ComboBoxField;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AlternativeComponentService implements ComboBoxField{

	private static final Logger logger = LogManager.getLogger();

	private final static ComponentDAO componentDAO = new ComponentDAO();

	private AlternativeComponentBean alternativeComponentBean = new AlternativeComponentBean();
	private CostCompanyService costCompanyService;

	public AlternativeComponentService(int id, CostCompanyBean costCompanyBean) {
		alternativeComponentBean.setAlternativeComponentId(id);

		costCompanyService = new CostCompanyService(costCompanyBean);
		ArrayList<CostCompanyBean> costCompanyBeans = new ArrayList<>();
		if(costCompanyService.isSet())
			costCompanyBeans.add(costCompanyBean);
		alternativeComponentBean.setCostCompanyBeans(costCompanyBeans);

	}

	public AlternativeComponentService() {}

	public int getId() {
		return alternativeComponentBean.getAlternativeComponentId();
	}

	public String getMfrPN() {
		return getMfrPN(alternativeComponentBean.getAlternativeComponentId());
	}

	public static String getMfrPN(int alternativeComponentId) {
		AlternativeComponent alternativeComponent = componentDAO.getAlternativeComponent(alternativeComponentId);
		return alternativeComponent!=null ? alternativeComponent.getMfrPN() : null;
	}

	public String getMfr() {
		return getMfr(alternativeComponentBean.getAlternativeComponentId());
	}

	public static String getMfr(int alternativeComponentId) {
		AlternativeComponent alternativeComponent = componentDAO.getAlternativeComponent(alternativeComponentId);
		return alternativeComponent!=null ? alternativeComponent.getMfrId() : null;
	}

	public int getSelectedCompany() {
		return alternativeComponentBean.getSelectedIndex();
	}

	public void setSelectedIndex(int index) {
		if(index>=0)
			alternativeComponentBean.setSelectedIndex(index);
	}

	public boolean isSet(){
		return alternativeComponentBean.getAlternativeComponentId()>=0;
	}

	public static boolean isSet(AlternativeComponentBean costMfrPNBean){
		return costMfrPNBean.getAlternativeComponentId()>=0;
	}

	public List<CostCompanyBean> getCostCompanies() {
		return alternativeComponentBean.getCostCompanyBeans();
	}

	public static boolean add(List<CostCompanyBean> newCostCompanyBeans, List<CostCompanyBean> costCompanyBeans){
		logger.entry("\n\t", newCostCompanyBeans, "\n\t", costCompanyBeans);
		boolean added = false;
		if(costCompanyBeans!=null && newCostCompanyBeans!=null) {

			for(CostCompanyBean ccb:newCostCompanyBeans){

				int indexOf = costCompanyBeans.indexOf(ccb);
				logger.trace("\n\tindexOf = {}", indexOf);

				if(indexOf>=0){
					added = CostCompanyService.add(ccb.getForPriceBeans(), costCompanyBeans.get(indexOf).getForPriceBeans());
				}else
					added = costCompanyBeans.add(ccb);
			}
		}
		return logger.exit(added);
	}

	public static void remove(AlternativeComponentBean costMfrPNBean, CostCompanyBean costCompanyBean) {
		if(costCompanyBean!=null) {
			List<CostCompanyBean> costCompanies = costMfrPNBean.getCostCompanyBeans();

			costCompanies.remove(costCompanyBean);
		}
	}

	public static BigDecimal getPrice(AlternativeComponentBean costMfrPNBean) {
		List<CostCompanyBean> costCompanies = costMfrPNBean.getCostCompanyBeans();
		int selectedIndex = costMfrPNBean.getSelectedIndex();
		return costCompanies.isEmpty() ? null : CostCompanyService.getPrice(costCompanies.get(selectedIndex<costCompanies.size() ? selectedIndex : 0));
	}

	public BigDecimal getPrice() {
		return getPrice(alternativeComponentBean);
	}

	public static String getCompanyName(AlternativeComponentBean costMfrPNBean) {
		List<CostCompanyBean> costCompanies = costMfrPNBean.getCostCompanyBeans();
		int selectedIndex = costMfrPNBean.getSelectedIndex();
		return costCompanies.isEmpty() ? null : CostCompanyService.getCompanyName(costCompanies.get(selectedIndex<costCompanies.size() ? selectedIndex : 0).getId());
	}

	public String getCompanyName() {
		return getCompanyName(alternativeComponentBean);
	}

	public static int getCompanyId(AlternativeComponentBean costMfrPNBean) {
		List<CostCompanyBean> costCompanies = costMfrPNBean.getCostCompanyBeans();
		return costCompanies.isEmpty() ? -1 : costCompanies.get(costMfrPNBean.getSelectedIndex()).getId();
	}

	public int getCompanyId() {
		return getCompanyId(alternativeComponentBean);
	}

	public static List<ForPriceBean> getForPrices(AlternativeComponentBean costMfrPNBean) {
		List<CostCompanyBean> costCompanies = costMfrPNBean.getCostCompanyBeans();
		int selectedIndex = costMfrPNBean.getSelectedIndex();
		return costCompanies.isEmpty() ? null : costCompanies.get(selectedIndex<costCompanies.size() ? selectedIndex : 0).getForPriceBeans();
	}

	public List<ForPriceBean> getForPrices() {
		return getForPrices(alternativeComponentBean);
	}

	public static int getSelectedForPriceIndex(AlternativeComponentBean costMfrPNBean) {
		List<CostCompanyBean> costCompanies = costMfrPNBean.getCostCompanyBeans();
		return costCompanies.isEmpty() ? -1 : costCompanies.get(costMfrPNBean.getSelectedIndex()).getSelectedIndex();
	}

	public int getSelectedForPriceIndex() {
		return getSelectedForPriceIndex(alternativeComponentBean);
	}

	public static int getForUnits(AlternativeComponentBean costMfrPNBean) {
		List<CostCompanyBean> costCompanies = costMfrPNBean.getCostCompanyBeans();
		int selectedIndex = costMfrPNBean.getSelectedIndex();
		return costCompanies.isEmpty() ? -1 : CostCompanyService.getForUnits(costCompanies.get(selectedIndex<costCompanies.size() ? selectedIndex : 0));
	}

	public int getForUnits() {
		return getForUnits(alternativeComponentBean);
	}

	public static void setForUnits(AlternativeComponentBean costMfrPNBean, int forUnit) {
		List<CostCompanyBean> costCompanies = costMfrPNBean.getCostCompanyBeans();
		if(!costCompanies.isEmpty())
			CostCompanyService.setForUnits(costCompanies.get(costMfrPNBean.getSelectedIndex()), forUnit);
	}

	public void setForUnits(int forUnit) {
		setForUnits(alternativeComponentBean, forUnit);
	}

	public static boolean isChanged(AlternativeComponentBean costMfrPNBean) {
		boolean isChanged = false;
		for(CostCompanyBean cc:costMfrPNBean.getCostCompanyBeans())
			if(CostCompanyService.isChanged(cc)){
				isChanged = true;
				break;
			}
		return isChanged;
	}

	public boolean isChanged() {
		return isChanged(alternativeComponentBean);
	}

	public static void setPrice(AlternativeComponentBean costMfrPNBean, BigDecimal price) {
		logger.entry(costMfrPNBean, price);
		List<CostCompanyBean> costCompanies = costMfrPNBean.getCostCompanyBeans();
		if(!costCompanies.isEmpty())
			CostCompanyService.setPrice(costCompanies.get(costMfrPNBean.getSelectedIndex()), price);
	}

	public void setPrice(BigDecimal price) {
		setPrice(alternativeComponentBean, price);
	}

	public static boolean setForPriceIndex(AlternativeComponentBean costMfrPNBean, int index) {
		boolean isSet = false;
		List<CostCompanyBean> costCompanies = costMfrPNBean.getCostCompanyBeans();
		if(!costCompanies.isEmpty())
			isSet = CostCompanyService.setSelectedForPriceIndex(costCompanies.get(costMfrPNBean.getSelectedIndex()), index);
		return isSet;
	}

	public boolean setForPriceIndex(int index) {
		return setForPriceIndex(alternativeComponentBean, index);
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
		return alternativeComponentBean.getAlternativeComponentId();
	}

	@Override
	public String getValue() {
		return ""+alternativeComponentBean.getAlternativeComponentId();
	}

	@Override
	public String getText() {
		return getMfrPN();
	}

	public static CostCompanyBean getCostCompanyBean(AlternativeComponentBean costMfrPNBean) {
		List<CostCompanyBean> costCompanies = costMfrPNBean.getCostCompanyBeans();
		return costCompanies.isEmpty() ? null : costCompanies.get(costMfrPNBean.getSelectedIndex());
	}

	public static List<CostCompanyService> getCostCompanyServices(AlternativeComponentBean costMfrPNBean) {

		List<CostCompanyService> costCompanyServices = new ArrayList<>();
		List<CostCompanyBean> costCompanyBeans = costMfrPNBean.getCostCompanyBeans();

		for(CostCompanyBean ccb:costCompanyBeans)
			costCompanyServices.add(new CostCompanyService(ccb));

		return costCompanyServices;
	}

	public CostCompanyBean getCompany() {
		return getCostCompanyBean(alternativeComponentBean);
	}

	public AlternativeComponentBean getCostMfrPNBean() {
		return alternativeComponentBean;
	}

	public AlternativeComponentService setCostMfrPNBean(AlternativeComponentBean costMfrPNBean) {
		this.alternativeComponentBean = costMfrPNBean;
		return this;
	}

	public static void reset(AlternativeComponentBean costMfrPNBean) {
		costMfrPNBean.setSelectedIndex(0);
		CostCompanyBean company = getCostCompanyBean(costMfrPNBean);
		if(company!=null)
			company.setSelectedIndex(0);
	}

	public void reset() {
		alternativeComponentBean.setSelectedIndex(0);
		CostCompanyBean company = getCompany();
		if(company!=null)
			company.setSelectedIndex(0);
	}

	@Override
	public String toString() {
		return "CostMfrPN [costMfrPNBean=" + alternativeComponentBean + "]";
	}

	public static ComboBoxField[] toArray( ArrayList<AlternativeComponentBean> mfrPartNumbers) {
		ComboBoxField[] comboBoxFields;

		logger.trace("{}", mfrPartNumbers);

		if(mfrPartNumbers!=null){
			int size = mfrPartNumbers.size();
			comboBoxFields = new AlternativeComponentService[size];
			for(int fieldIndex=0; fieldIndex<size; fieldIndex++)
				comboBoxFields[fieldIndex] = new AlternativeComponentService().setCostMfrPNBean(mfrPartNumbers.get(fieldIndex));
		}else
			comboBoxFields = null;

		return comboBoxFields;
	}
}
