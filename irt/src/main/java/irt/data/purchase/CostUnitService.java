package irt.data.purchase;

import irt.data.companies.Company;
import irt.data.components.AlternativeComponent;
import irt.data.components.Component;
import irt.data.dao.ComponentDAO;
import irt.data.dao.ManufactureDAO;
import irt.data.html.HTMLComponentOption;
import irt.data.html.HTMLComponentSelect;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CostUnitService {

	private static final Logger logger = LogManager.getLogger();

	private final static ComponentDAO componentDAO = new ComponentDAO();
	private final static ManufactureDAO manufactureDAO = new ManufactureDAO();

	private CostUnitBean costUnitBean = new CostUnitBean();

	public CostUnitService(){};

	public CostUnitService(int componentId, int alternativeId, AlternativeComponentBean costMfrPNBean, int qty) {
		costUnitBean.setComponentId(componentId);
		costUnitBean.getAlternativeComponentBeans().add(costMfrPNBean);
		costUnitBean.setQty(qty);
	}

	public int getQty() {
//TODO		CostUnitBean alternativeUnit = costUnitBean.getAlternativeComponentBeans().get(costUnitBean.getSelectedIndex()).getAlternativeUnit();
		return 0;// costUnitBean.getSelectedIndex() == 0 || alternativeUnit==null ? costUnitBean.getQty() :alternativeUnit.getQty();
	}

	public int getComponentId() {
		return costUnitBean.getComponentId();
	}

	public static Component getComponent(int componentId) {
		return componentDAO.getComponent(componentId);
	}

	public int getComponentId(AlternativeComponentService costMfrPNService) {
		CostUnitBean alternativeUnit = costMfrPNService.getAlternativeUnit();
		return alternativeUnit==null ? costUnitBean.getComponentId() : alternativeUnit.getComponentId();
	}

	public String getPartNumberStr() {
//TODO		CostUnitBean alternativeUnit = null;
//		int selectedIndex = costUnitBean.getSelectedIndex();
//		if(selectedIndex != 0)
//			alternativeUnit = costUnitBean.getAlternativeComponentBeans().get(selectedIndex).getAlternativeUnit();
		return null;// alternativeUnit==null ? costUnitBean.getPartNumberStr() : alternativeUnit.getPartNumberStr();
	}

	public String getDescription() {
//TODO		CostUnitBean alternativeUnit = null;
//		int selectedIndex = costUnitBean.getSelectedIndex();
//		if(selectedIndex != 0)
//			alternativeUnit = costUnitBean.getAlternativeComponentBeans().get(selectedIndex).getAlternativeUnit();
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

	public ArrayList<AlternativeComponentBean> getAlternativeComponentBeans() {
		return costUnitBean.getAlternativeComponentBeans();
	}

	public static ArrayList<AlternativeComponentBean> getAlternativeComponentBeans(CostUnitBean costUnitBean) {
		return costUnitBean.getAlternativeComponentBeans();
	}

	public static boolean add(ArrayList<AlternativeComponentBean> newAlternativeComponentBeans, ArrayList<AlternativeComponentBean> alternativeComponentBeans) {
		logger.entry("\n\t", newAlternativeComponentBeans, "\n\t", alternativeComponentBeans);

		boolean added = false;
		if(alternativeComponentBeans!=null && newAlternativeComponentBeans!=null)

			for(AlternativeComponentBean cmpn:newAlternativeComponentBeans) {

				int indexOf = alternativeComponentBeans.indexOf(cmpn);
				logger.trace("\n\tindexOf = {}", indexOf);

				if(indexOf>=0) 
					added = AlternativeComponentService.add(cmpn.getCostCompanyBeans(), alternativeComponentBeans.get(indexOf).getCostCompanyBeans());
				else
					added = alternativeComponentBeans.add(cmpn);
			}
		return logger.exit(added);
	}
	public void add(ArrayList<AlternativeComponentBean> mfrPartNumbers) {
		add(mfrPartNumbers, costUnitBean.getAlternativeComponentBeans());
	}

	public static BigDecimal getPrice(CostUnitBean costUnitBean) {
		logger.entry(costUnitBean);
		ArrayList<AlternativeComponentBean> mfrPartNumbers = costUnitBean.getAlternativeComponentBeans();
		return logger.exit(mfrPartNumbers.isEmpty() ? null : AlternativeComponentService.getPrice(mfrPartNumbers.get(costUnitBean.getSelectedIndex())));
	}

	public BigDecimal getPrice() {
		return getPrice(costUnitBean);
	}
//
//	public static AlternativeComponentBean getAlternativeComponentBean(CostUnitBean costUnitBean) {
//		ArrayList<AlternativeComponentBean> mfrPartNumbers = costUnitBean.getAlternativeComponentBeans();
//		return mfrPartNumbers.isEmpty() ? null : mfrPartNumbers.get(costUnitBean.getSelectedIndex());
//	}

	public AlternativeComponentBean getMfrPartNumberBean() {
		return getAlternativeComponentBean(costUnitBean);
	}

	public static String getMfrPN(CostUnitBean costUnitBean) {
		AlternativeComponentBean acb = getAlternativeComponentBean(costUnitBean);
		String mfrPM;
		if(acb.getAlternativeComponentId()>0){
			AlternativeComponent ac = componentDAO.getAlternativeComponent(acb.getAlternativeComponentId());
			mfrPM = ac.getMfrPN();
		}else{
			Component component = componentDAO.getComponent(costUnitBean.getComponentId());
			mfrPM = component.getMfrPN();
		}
		
		return mfrPM;
	}

	public static String getMfrPN(CostUnitBean costUnitBean, int alternativeComponentId) {
		logger.trace("\n\talternativeComponentId:\t{}\n\tcostUnitBean:\t{}", alternativeComponentId, costUnitBean);

		String mfrPM;

		if(alternativeComponentId==0){
			Component component = componentDAO.getComponent(costUnitBean.getComponentId());
			mfrPM = component.getMfrPN();
		}else{
			AlternativeComponent ac = componentDAO.getAlternativeComponent(alternativeComponentId);
			mfrPM = ac.getMfrPN();
		}

		return mfrPM;
	}

	public String getMfrPN() {
		return getMfrPN(costUnitBean);
	}

	public static String getMfr(CostUnitBean costUnitBean) {
		AlternativeComponentBean acb = getAlternativeComponentBean(costUnitBean);
		String mfr;
		if(acb.getAlternativeComponentId()>0){
			AlternativeComponent ac = componentDAO.getAlternativeComponent(acb.getAlternativeComponentId());
			mfr = manufactureDAO.getMfrName(ac.getMfrId());
		}else{
			Component component = componentDAO.getComponent(costUnitBean.getComponentId());
			mfr = manufactureDAO.getMfrName(component.getMfrId());
		}
		
		return mfr;
	}

	public static String getMfr(CostUnitBean costUnitBean, int alternativeComponentId) {
		String mfr;
		if(alternativeComponentId>0){
			AlternativeComponent ac = componentDAO.getAlternativeComponent(alternativeComponentId);
			mfr = manufactureDAO.getMfrName(ac.getMfrId());
		}else{
			Component component = componentDAO.getComponent(costUnitBean.getComponentId());
			mfr = manufactureDAO.getMfrName(component.getMfrId());
		}
		
		return mfr;
	}

	public static AlternativeComponentBean getAlternativeComponentBean( CostUnitBean costUnitBean) {
		return getAlternativeComponentBean(costUnitBean, costUnitBean.getSelectedIndex());
	}

	private static AlternativeComponentBean getAlternativeComponentBean( CostUnitBean costUnitBean, int index) {

		ArrayList<AlternativeComponentBean> avbs = costUnitBean.getAlternativeComponentBeans();

		AlternativeComponentBean acb;
		if(avbs.isEmpty())
			acb = null;
		else
			acb = avbs.get(index);

		return acb;
	}

	public String getMfr() {
		return getMfr(costUnitBean);
	}

	public static boolean isSet(CostUnitBean costUnitBean) {
		return costUnitBean.getComponentId()>0;
	}

	public boolean isSet() {
		return isSet(costUnitBean);
	}

	public static List<CostCompanyBean> getCompanyBeans(CostUnitBean costUnitBean) {
		ArrayList<AlternativeComponentBean> mfrPartNumbers = costUnitBean.getAlternativeComponentBeans();
		return mfrPartNumbers.isEmpty() ? null : mfrPartNumbers.get(costUnitBean.getSelectedIndex()).getCostCompanyBeans();
	}

	public static List<CostCompanyService> getCompanyServices(List<AlternativeComponentBean> costMfrPNBeans) {

		ArrayList<CostCompanyService> costCompanyServices = new ArrayList<>();

		for(AlternativeComponentBean c:costMfrPNBeans)
			costCompanyServices.addAll(AlternativeComponentService.getCostCompanyServices(c));

		logger.trace("\n\tEntry\t{}\n\tExit\t{}", costMfrPNBeans, costCompanyServices);

		return costCompanyServices.isEmpty() ? null : costCompanyServices;
	}

	public List<CostCompanyBean> getCompanyBeans() {
		return getCompanyBeans(costUnitBean);
	}

	public static String getCompanyName(CostUnitBean costUnitBean) {
		ArrayList<AlternativeComponentBean> mfrPartNumbers = costUnitBean.getAlternativeComponentBeans();
		return mfrPartNumbers.isEmpty() ? null : AlternativeComponentService.getCompanyName(mfrPartNumbers.get(costUnitBean.getSelectedIndex())) ;
	}

	public String getCompanyName() {
		return getCompanyName(costUnitBean);
	}

	public static int getCompanyId(CostUnitBean costUnitBean) {
		ArrayList<AlternativeComponentBean> mfrPartNumbers = costUnitBean.getAlternativeComponentBeans();
		return mfrPartNumbers.isEmpty() ? null : AlternativeComponentService.getCompanyId(mfrPartNumbers.get(costUnitBean.getSelectedIndex())) ;
	}

	public int getCompanyId() {
		return getCompanyId(costUnitBean);
	}

	public static List<ForPriceBean> getForPrices(CostUnitBean costUnitBean) {
		ArrayList<AlternativeComponentBean> mfrPartNumbers = costUnitBean.getAlternativeComponentBeans();
		return mfrPartNumbers.isEmpty() ? null : AlternativeComponentService.getForPrices(mfrPartNumbers.get(costUnitBean.getSelectedIndex())) ;
	}

	public List<ForPriceBean> getForPrices() {
		return getForPrices(costUnitBean);
	}

	public static int getSelectedForPrice(CostUnitBean costUnitBean) {
		ArrayList<AlternativeComponentBean> mfrPartNumbers = costUnitBean.getAlternativeComponentBeans();
		return logger.exit(mfrPartNumbers.isEmpty() ? -1 : AlternativeComponentService.getSelectedForPriceIndex(mfrPartNumbers.get(costUnitBean.getSelectedIndex()))) ;
	}

	public int getSelectedForPrice() {
		return getSelectedForPrice(costUnitBean);
	}

	public static int getMinimumOrderQty(CostUnitBean costUnitBean) {
		ArrayList<AlternativeComponentBean> mfrPartNumbers = costUnitBean.getAlternativeComponentBeans();
		return logger.exit(mfrPartNumbers.isEmpty() ? -1 : AlternativeComponentService.getForUnits(mfrPartNumbers.get(costUnitBean.getSelectedIndex()))) ;
	}

	public int getForUnits() {
		return getMinimumOrderQty(costUnitBean);
	}

	public static void setForUnits(CostUnitBean costUnitBean, int forUnit) {
		ArrayList<AlternativeComponentBean> mfrPartNumbers = costUnitBean.getAlternativeComponentBeans();
		if(!mfrPartNumbers.isEmpty())
			AlternativeComponentService.setForUnits(mfrPartNumbers.get(costUnitBean.getSelectedIndex()), forUnit) ;
	}

	public void setForUnits(int forUnit) {
		setForUnits(costUnitBean, forUnit);
	}

	public static boolean isChanged(CostUnitBean costUnitBean) {
		boolean isChanged = false;
		for(AlternativeComponentBean cmpn:costUnitBean.getAlternativeComponentBeans())
			if(AlternativeComponentService.isChanged(cmpn)){
				isChanged = true;
				break;
			}
		return isChanged;
	}

	public boolean isChanged() {
		return isChanged(costUnitBean);
	}

	public static void setPrice(CostUnitBean costUnitBean, BigDecimal price) {
		logger.entry(costUnitBean, price);
		ArrayList<AlternativeComponentBean> alternativeComponentBeans = costUnitBean.getAlternativeComponentBeans();
		if(!alternativeComponentBeans.isEmpty())
			AlternativeComponentService.setPrice(alternativeComponentBeans.get(costUnitBean.getSelectedIndex()), price) ;
	}

	public void setPrice(BigDecimal price) {
		setPrice(costUnitBean, price);
	}

	public static boolean setSelectedMfrPN(CostUnitBean costUnitBean, int selectedIndex) {
		logger.entry("\n\t", costUnitBean, "\n\t", selectedIndex);
		boolean set;
		if(selectedIndex>=0 && selectedIndex<costUnitBean.getAlternativeComponentBeans().size()){
			costUnitBean.setSelectedIndex(selectedIndex);
			set = true;
		}else
			set = false;
		return logger.exit(set);
	}

	public void setSelectedMfrPN(int selectedIndex) {
		setSelectedMfrPN(costUnitBean, selectedIndex);
	}

	public static void setSelectedCompanyIndex(CostUnitBean costUnitBean, int index) {
		ArrayList<AlternativeComponentBean> mfrPartNumbers = costUnitBean.getAlternativeComponentBeans();
		if(!mfrPartNumbers.isEmpty())
			mfrPartNumbers.get(costUnitBean.getSelectedIndex()).setSelectedIndex(index);
	}

	public void setSelectedCompanyIndex(int index) {
		setSelectedCompanyIndex(costUnitBean, index);
	}

	public static boolean setForPriceIndex(CostUnitBean costUnitBean, int index) {
		boolean isSet = false;
		ArrayList<AlternativeComponentBean> mfrPartNumbers = costUnitBean.getAlternativeComponentBeans();
		if(!mfrPartNumbers.isEmpty())
			isSet = AlternativeComponentService.setForPriceIndex(mfrPartNumbers.get(costUnitBean.getSelectedIndex()), index) ;
		return isSet;
	}

	public boolean setForPriceIndex(int index) {
		boolean isSet = false;
		ArrayList<AlternativeComponentBean> mfrPartNumbers = costUnitBean.getAlternativeComponentBeans();
		if(!mfrPartNumbers.isEmpty())
			isSet = AlternativeComponentService.setForPriceIndex(mfrPartNumbers.get(costUnitBean.getSelectedIndex()), index) ;
		return isSet;
	}

	public static int getMfrPartNumberIndex(CostUnitBean costUnitBean) {
		ArrayList<AlternativeComponentBean> mfrPartNumbers = costUnitBean.getAlternativeComponentBeans();
		return mfrPartNumbers.isEmpty() ? null : mfrPartNumbers.get(costUnitBean.getSelectedIndex()).getAlternativeComponentId();
	}

	public int getMfrPartNumberIndex() {
		return getMfrPartNumberIndex(costUnitBean);
	}

	public static int getSelectedCompany(CostUnitBean costUnitBean) {
		return getAlternativeComponentBean(costUnitBean).getSelectedIndex();
	}

	public int getSelectedCompany() {
		return getMfrPartNumberBean().getSelectedIndex();
	}

	public static void reset(CostUnitBean costUnitBean) {
		costUnitBean.setSelectedIndex(0);
		AlternativeComponentService.reset(getAlternativeComponentBean(costUnitBean)) ;
	}

	public void reset() {
		reset(costUnitBean) ;
	}

	public static void setSelectedForPrice(CostUnitBean costUnitBean, int moqIndex) {
		AlternativeComponentBean mfrPartNumber = getAlternativeComponentBean(costUnitBean);
		if(mfrPartNumber!=null){
			CostCompanyBean company = AlternativeComponentService.getCostCompanyBean(mfrPartNumber);
			if(company!=null)
				CostCompanyService.setSelectedForPriceIndex(company, moqIndex);
		}
	}

	public void setSelectedForPrice(int moqIndex) {
		AlternativeComponentBean mfrPartNumber = getMfrPartNumberBean();
		if(mfrPartNumber!=null){
			CostCompanyBean company = AlternativeComponentService.getCostCompanyBean(mfrPartNumber);
			if(company!=null)
				CostCompanyService.setSelectedForPriceIndex(company, moqIndex);
		}
	}

	@Override
	public String toString() {
		return "CostUnit [costUnitBean=" + costUnitBean + "]";
	}

	public static HTMLComponentSelect getHTMLSelectAlternativeComponentBeans(CostUnitBean costUnitBean) {

		String id = "mfrPN"+costUnitBean.getComponentId();

		HTMLComponentSelect select = new HTMLComponentSelect();
		select.setId(id);
		select.setJavaScript("onchange=\"whatEdit('" + id + ":'+this.selectedIndex)\"");

		HTMLComponentOption.setSelectedValue(""+costUnitBean.getSelectedIndex());

		for(int i=0; i<costUnitBean.getAlternativeComponentBeans().size(); i++)
			select.add(new HTMLComponentOption(""+i,  getMfrPN(costUnitBean, i)));

		return select;
	}

	public static HTMLComponentSelect getHTMLSelectCompanyBeans(CostUnitBean costUnitBean) {

		String id = "vendor"+costUnitBean.getComponentId();

		HTMLComponentSelect select = new HTMLComponentSelect();
		select.setId(id);
		select.setJavaScript("onchange=\"whatEdit('" + id + ":'+this.selectedIndex)\"");

		AlternativeComponentBean alternativeComponentBean = getAlternativeComponentBean(costUnitBean);
		List<CostCompanyBean> companyBeans = getCompanyBeans(costUnitBean);
		HTMLComponentOption.setSelectedValue(""+alternativeComponentBean.getSelectedIndex());

		for(int i=0; i<companyBeans.size(); i++){
			Company company = CostCompanyService.getCompany(companyBeans.get(i));
			if(company!=null)
				select.add(new HTMLComponentOption(""+i,  company.getCompanyName()));
			else{
				Component component = componentDAO.getComponent(costUnitBean.getComponentId());
				String mfrName = manufactureDAO.getMfrName(component.getMfrId());
				select.add(new HTMLComponentOption(""+i,  mfrName));
			}
		}

		return select;
	}

	public static HTMLComponentSelect getHTMLSelectPartNumber(CostUnitBean costUnitBean) {
		logger.entry("\n\t{}", costUnitBean);

		String id = "cpn"+costUnitBean.getComponentId();

		HTMLComponentSelect select = new HTMLComponentSelect();
		select.setId(id);
		select.setJavaScript("onchange=\"whatEdit('" + id + ":'+this.selectedIndex)\"");

		HTMLComponentOption.setSelectedValue(""+costUnitBean.getSelectedIndex());

		for(int i=0; i<costUnitBean.getAlternativeComponentBeans().size(); i++) 
			select.add(new HTMLComponentOption(""+i,  getPN(costUnitBean, i)));

		return select;
	}

	private static String getPN(CostUnitBean costUnitBean, int index) {
		logger.trace("\n\tcostUnitBean:\t{}\n\tcostUnitBean:\t{}", index, costUnitBean);

		AlternativeComponentBean acb = costUnitBean.getAlternativeComponentBeans().get(index);

		int alternativeComponentId = acb.getAlternativeComponentId();
		return getPartNumber(costUnitBean, alternativeComponentId);
	}

	public static String getPartNumber(CostUnitBean costUnitBean, int alternativeComponentId) {
		String pn;

		switch(alternativeComponentId){
		default:
			AlternativeComponent ac = componentDAO.getAlternativeComponent(alternativeComponentId);
			Component component = componentDAO.getComponentByMfrPN(ac.getMfrPN());
			if(component!=null){
				pn = component.getPartNumberF();
				break;
			}
		case 0:
			component = componentDAO.getComponent(costUnitBean.getComponentId());
			pn = component.getPartNumberF();
		}

		return pn;
	}
}
