package irt.data.purchase;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CostUnitService {

	private static final Logger LOGGER = LogManager.getLogger();
	private CostUnitBean costUnitBean = new CostUnitBean();
	private CostMfrPNService costMfrPNService = new CostMfrPNService();

	public CostUnitService(){};

	public CostUnitService(int componentId, int alternativeId, String partNumberStr, String description, CostMfrPNBean costMfrPNBean, int qty) {
		costUnitBean.setComponentId(componentId);
		costUnitBean.setPartNumberStr(partNumberStr);
		costUnitBean.setDescription(description);

		costMfrPNService.setCostMfrPNBean(costMfrPNBean);

// ??		costUnitBean.setAlternativeComponentId(alternativeId);
//		if (costMfrPNService.isSet()){
//			ArrayList<CostMfrPNBean> mfrPartNumbers = costUnitBean.getMfrPartNumbers();
//			mfrPartNumbers.add(mfrPNBean);
//			if(alternativeId>0)
//				mfrPNBean.add(new CostUnit(alternativeId, 0, partNumberStr, description, mfrPNBean, qty));
//		}
		costUnitBean.setQty(qty);
	}

	public int getQty() {
//TODO		CostUnitBean alternativeUnit = costUnitBean.getMfrPartNumbers().get(costUnitBean.getSelectedIndex()).getAlternativeUnit();
		return 0;// costUnitBean.getSelectedIndex() == 0 || alternativeUnit==null ? costUnitBean.getQty() :alternativeUnit.getQty();
	}

	public int getComponentId() {
		return costUnitBean.getComponentId();
	}

	public int getComponentId(CostMfrPNService costMfrPNService) {
		CostUnitBean alternativeUnit = costMfrPNService.getAlternativeUnit();
		return alternativeUnit==null ? costUnitBean.getComponentId() : alternativeUnit.getComponentId();
	}

	public String getPartNumberStr() {
//TODO		CostUnitBean alternativeUnit = null;
//		int selectedIndex = costUnitBean.getSelectedIndex();
//		if(selectedIndex != 0)
//			alternativeUnit = costUnitBean.getMfrPartNumbers().get(selectedIndex).getAlternativeUnit();
		return null;// alternativeUnit==null ? costUnitBean.getPartNumberStr() : alternativeUnit.getPartNumberStr();
	}

	public String getDescription() {
//TODO		CostUnitBean alternativeUnit = null;
//		int selectedIndex = costUnitBean.getSelectedIndex();
//		if(selectedIndex != 0)
//			alternativeUnit = costUnitBean.getMfrPartNumbers().get(selectedIndex).getAlternativeUnit();
		return null;// alternativeUnit==null ? costUnitBean.getDescription() : alternativeUnit.getDescription();
	}

	@Override
	public boolean equals(Object obj) {
		return obj!=null ? obj.hashCode()==hashCode() : false;
	}

	@Override
	public int hashCode() {
		return costUnitBean.hashCode();
	}

	public static int getSelectedMfrPN(CostUnitBean costUnitBean) {
		return costUnitBean.getSelectedIndex();
	}

	public int getSelectedMfrPN() {
		return getSelectedMfrPN(costUnitBean);
	}

	public ArrayList<CostMfrPNBean> getMfrPartNumbers() {
		return costUnitBean.getMfrPartNumbers();
	}

	public static void add(CostUnitBean costUnitBean, ArrayList<CostMfrPNBean> mfrPartNumbers) {
		if(mfrPartNumbers!=null)
			for(CostMfrPNBean cmpn:mfrPartNumbers) {
				ArrayList<CostMfrPNBean> mPNs = costUnitBean.getMfrPartNumbers();
				if(mPNs.contains(cmpn))
					CostMfrPNService.add(mPNs.get(mPNs.indexOf(cmpn)), cmpn.getCostCompanyBeans());
				else if(CostMfrPNService.isSet(cmpn))
					mPNs.add(cmpn);
			}
	}
	public void add(ArrayList<CostMfrPNBean> mfrPartNumbers) {
		add(costUnitBean, mfrPartNumbers);
	}

	public static BigDecimal getPrice(CostUnitBean costUnitBean) {
		LOGGER.entry(costUnitBean);
		ArrayList<CostMfrPNBean> mfrPartNumbers = costUnitBean.getMfrPartNumbers();
		return LOGGER.exit(mfrPartNumbers.isEmpty() ? null : CostMfrPNService.getPrice(mfrPartNumbers.get(costUnitBean.getSelectedIndex())));
	}

	public BigDecimal getPrice() {
		return getPrice(costUnitBean);
	}

	public static CostMfrPNBean getMfrPartNumberBean(CostUnitBean costUnitBean) {
		ArrayList<CostMfrPNBean> mfrPartNumbers = costUnitBean.getMfrPartNumbers();
		return mfrPartNumbers.isEmpty() ? null : mfrPartNumbers.get(costUnitBean.getSelectedIndex());
	}

	public CostMfrPNBean getMfrPartNumberBean() {
		return getMfrPartNumberBean(costUnitBean);
	}

	public static String getMfrPN(CostUnitBean costUnitBean) {
//TODO		ArrayList<CostMfrPNBean> mfrPartNumbers = costUnitBean.getMfrPartNumbers();
		return null;//mfrPartNumbers.isEmpty() ? null : mfrPartNumbers.get(costUnitBean.getSelectedIndex()).getMfrPN();
	}

	public String getMfrPN() {
		return getMfrPN(costUnitBean);
	}

	public static String getMfr(CostUnitBean costUnitBean) {
//		ArrayList<CostMfrPNBean> mfrPartNumbers = costUnitBean.getMfrPartNumbers();
		return null;// mfrPartNumbers.isEmpty() ? null : mfrPartNumbers.get(costUnitBean.getSelectedIndex()).getMfr();
	}

	public String getMfr() {
		return getMfr(costUnitBean);
	}

	public static boolean isSet(CostUnitBean costUnitBean) {
		String partNumberStr = costUnitBean.getPartNumberStr();
		return costUnitBean.getComponentId()>0 && partNumberStr!=null && !partNumberStr.isEmpty();
	}

	public boolean isSet() {
		return isSet(costUnitBean);
	}

	public static List<CostCompanyBean> getCompanyBeans(CostUnitBean costUnitBean) {
		ArrayList<CostMfrPNBean> mfrPartNumbers = costUnitBean.getMfrPartNumbers();
		return mfrPartNumbers.isEmpty() ? null : mfrPartNumbers.get(costUnitBean.getSelectedIndex()).getCostCompanyBeans();
	}

	public static List<CostCompanyService> getCompanyServices(List<CostMfrPNBean> costMfrPNBeans) {

		ArrayList<CostCompanyService> costCompanyServices = new ArrayList<>();

		for(CostMfrPNBean c:costMfrPNBeans)
			costCompanyServices.addAll(CostMfrPNService.getCostCompanyServices(c));

		LOGGER.trace("\n\tEntry\t{}\n\tExit\t{}", costMfrPNBeans, costCompanyServices);

		return costCompanyServices.isEmpty() ? null : costCompanyServices;
	}

	public List<CostCompanyBean> getCompanyBeans() {
		return getCompanyBeans(costUnitBean);
	}

	public static String getCompanyName(CostUnitBean costUnitBean) {
		ArrayList<CostMfrPNBean> mfrPartNumbers = costUnitBean.getMfrPartNumbers();
		return mfrPartNumbers.isEmpty() ? null : CostMfrPNService.getCompanyName(mfrPartNumbers.get(costUnitBean.getSelectedIndex())) ;
	}

	public String getCompanyName() {
		return getCompanyName(costUnitBean);
	}

	public static int getCompanyId(CostUnitBean costUnitBean) {
		ArrayList<CostMfrPNBean> mfrPartNumbers = costUnitBean.getMfrPartNumbers();
		return mfrPartNumbers.isEmpty() ? null : CostMfrPNService.getCompanyId(mfrPartNumbers.get(costUnitBean.getSelectedIndex())) ;
	}

	public int getCompanyId() {
		return getCompanyId(costUnitBean);
	}

	public static List<ForPriceBean> getForPrices(CostUnitBean costUnitBean) {
		ArrayList<CostMfrPNBean> mfrPartNumbers = costUnitBean.getMfrPartNumbers();
		return mfrPartNumbers.isEmpty() ? null : CostMfrPNService.getForPrices(mfrPartNumbers.get(costUnitBean.getSelectedIndex())) ;
	}

	public List<ForPriceBean> getForPrices() {
		return getForPrices(costUnitBean);
	}

	public static int getSelectedForPrice(CostUnitBean costUnitBean) {
		ArrayList<CostMfrPNBean> mfrPartNumbers = costUnitBean.getMfrPartNumbers();
		return LOGGER.exit(mfrPartNumbers.isEmpty() ? -1 : CostMfrPNService.getSelectedForPriceIndex(mfrPartNumbers.get(costUnitBean.getSelectedIndex()))) ;
	}

	public int getSelectedForPrice() {
		return getSelectedForPrice(costUnitBean);
	}

	public static int getMinimumOrderQty(CostUnitBean costUnitBean) {
		ArrayList<CostMfrPNBean> mfrPartNumbers = costUnitBean.getMfrPartNumbers();
		return LOGGER.exit(mfrPartNumbers.isEmpty() ? -1 : CostMfrPNService.getForUnits(mfrPartNumbers.get(costUnitBean.getSelectedIndex()))) ;
	}

	public int getForUnits() {
		return getMinimumOrderQty(costUnitBean);
	}

	public static void setForUnits(CostUnitBean costUnitBean, int forUnit) {
		ArrayList<CostMfrPNBean> mfrPartNumbers = costUnitBean.getMfrPartNumbers();
		if(!mfrPartNumbers.isEmpty())
			CostMfrPNService.setForUnits(mfrPartNumbers.get(costUnitBean.getSelectedIndex()), forUnit) ;
	}

	public void setForUnits(int forUnit) {
		setForUnits(costUnitBean, forUnit);
	}

	public static boolean isChanged(CostUnitBean costUnitBean) {
		boolean isChanged = false;
		for(CostMfrPNBean cmpn:costUnitBean.getMfrPartNumbers())
			if(CostMfrPNService.isChanged(cmpn)){
				isChanged = true;
				break;
			}
		return isChanged;
	}

	public boolean isChanged() {
		return isChanged(costUnitBean);
	}

	public static void setPrice(CostUnitBean costUnitBean, BigDecimal price) {
		ArrayList<CostMfrPNBean> mfrPartNumbers = costUnitBean.getMfrPartNumbers();
		if(!mfrPartNumbers.isEmpty())
			CostMfrPNService.setPrice(mfrPartNumbers.get(costUnitBean.getSelectedIndex()), price) ;
	}

	public void setPrice(BigDecimal price) {
		setPrice(costUnitBean, price);
	}

	public static void setSelectedMfrPN(CostUnitBean costUnitBean, int selectedIndex) {
		if(selectedIndex>=0)
			costUnitBean.setSelectedIndex(selectedIndex);
	}

	public void setSelectedMfrPN(int selectedIndex) {
		setSelectedMfrPN(costUnitBean, selectedIndex);
	}

	public static void setSelectedCompanyIndex(CostUnitBean costUnitBean, int index) {
		ArrayList<CostMfrPNBean> mfrPartNumbers = costUnitBean.getMfrPartNumbers();
		if(!mfrPartNumbers.isEmpty())
			mfrPartNumbers.get(costUnitBean.getSelectedIndex()).setSelectedIndex(index);
	}

	public void setSelectedCompanyIndex(int index) {
		setSelectedCompanyIndex(costUnitBean, index);
	}

	public static boolean setForPriceIndex(CostUnitBean costUnitBean, int index) {
		boolean isSet = false;
		ArrayList<CostMfrPNBean> mfrPartNumbers = costUnitBean.getMfrPartNumbers();
		if(!mfrPartNumbers.isEmpty())
			isSet = CostMfrPNService.setForPriceIndex(mfrPartNumbers.get(costUnitBean.getSelectedIndex()), index) ;
		return isSet;
	}

	public boolean setForPriceIndex(int index) {
		boolean isSet = false;
		ArrayList<CostMfrPNBean> mfrPartNumbers = costUnitBean.getMfrPartNumbers();
		if(!mfrPartNumbers.isEmpty())
			isSet = CostMfrPNService.setForPriceIndex(mfrPartNumbers.get(costUnitBean.getSelectedIndex()), index) ;
		return isSet;
	}

	public static int getMfrPartNumberIndex(CostUnitBean costUnitBean) {
		ArrayList<CostMfrPNBean> mfrPartNumbers = costUnitBean.getMfrPartNumbers();
		return mfrPartNumbers.isEmpty() ? null : mfrPartNumbers.get(costUnitBean.getSelectedIndex()).getAlternativeComponentId();
	}

	public int getMfrPartNumberIndex() {
		return getMfrPartNumberIndex(costUnitBean);
	}

	public static int getSelectedCompany(CostUnitBean costUnitBean) {
		return getMfrPartNumberBean(costUnitBean).getSelectedIndex();
	}

	public int getSelectedCompany() {
		return getMfrPartNumberBean().getSelectedIndex();
	}

	public static void reset(CostUnitBean costUnitBean) {
		costUnitBean.setSelectedIndex(0);
		CostMfrPNService.reset(getMfrPartNumberBean(costUnitBean)) ;
	}

	public void reset() {
		reset(costUnitBean) ;
	}

	public static void setSelectedForPrice(CostUnitBean costUnitBean, int moqIndex) {
		CostMfrPNBean mfrPartNumber = getMfrPartNumberBean(costUnitBean);
		if(mfrPartNumber!=null){
			CostCompanyBean company = CostMfrPNService.getCostCompanyBean(mfrPartNumber);
			if(company!=null)
				CostCompanyService.setSelectedForPriceIndex(company, moqIndex);
		}
	}

	public void setSelectedForPrice(int moqIndex) {
		CostMfrPNBean mfrPartNumber = getMfrPartNumberBean();
		if(mfrPartNumber!=null){
			CostCompanyBean company = CostMfrPNService.getCostCompanyBean(mfrPartNumber);
			if(company!=null)
				CostCompanyService.setSelectedForPriceIndex(company, moqIndex);
		}
	}

	@Override
	public String toString() {
		return "CostUnit [costUnitBean=" + costUnitBean + "]";
	}
}
