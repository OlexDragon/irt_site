package irt.data.purchase;

import irt.data.HTMLWork;
import irt.data.dao.CostDAO;
import irt.table.HTMLHeader;
import irt.table.Row;
import irt.table.Table;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CostService {

	private final Logger logger = LogManager.getLogger();
	private CostBean costBean;

	public CostService(CostBean costBean) {
		this.costBean = costBean!=null ? costBean : new CostBean();
	}

	private static final int	ID	= 0,
								PN	= 1,
								MPN	= 2,
								MFR	= 3,
								VD	= 4,
								PR	= 5,
								FR	= 6,
								MP	= 7,
								QT	= 8,
								TL	= 9;

	public Table getComponentTable() {
		String[] titels = new String[]{"ID","Part Number","Mfr P/N","Mfr","Vendor","Price","MOQ","MOP","Qty","Total"};
		Table table = new Table(titels, null);
		for(CostUnitBean cu:costBean.getCostUnitBeans()){
			BigDecimal totalPrice ;
			String[] row = new String[titels.length];
			row[ID]	= ""+cu.getComponentId();
			row[PN] = cu.getPartNumberStr();
			row[QT] = ""+cu.getQty();
			if(cu.getMfrPartNumbers().isEmpty()){
				table.add( new Row(Arrays.copyOf(row, row.length)));
				row[MFR] = 
				row[MPN] = 
				row[VD] =
				row[PR] =
				row[FR] =
				row[MP] =
				row[TL] = null;
			}else
				for(CostMfrPNBean cmpn:cu.getMfrPartNumbers()){
					if(cmpn.getAlternativeUnit()!=null){
						CostUnitBean alt = cmpn.getAlternativeUnit();
						row[ID]	= ""+alt.getComponentId();
						row[PN] = alt.getPartNumberStr();
						row[QT] = ""+alt.getQty();
					}else{
						row[ID]	= ""+cu.getComponentId();
						row[PN] = cu.getPartNumberStr();
						row[QT] = ""+cu.getQty();
					}
					row[MPN] = cmpn.getMfrPN();
					row[MFR] = cmpn.getMfr();
					if(cmpn.getCostCompanyBeans().isEmpty()){
						table.add( new Row(Arrays.copyOf(row, row.length)));
						row[VD] =
						row[PR] =
						row[MP] =
						row[FR] =
						row[TL] = null;
					}else{
						for(CostCompanyBean cc:cmpn.getCostCompanyBeans()){
							row[VD] = cc.getName();
							if(CostMfrPNService.getForPrices(cmpn).isEmpty()){
								table.add( new Row(Arrays.copyOf(row, row.length)));
								row[PR] =
								row[MP] =
								row[PR] =
								row[TL] = null;
							}else
								for(ForPriceBean fp:cc.getForPriceBeans()) {
									BigDecimal price = fp.getPrice();
									DecimalFormat decimalFormat = new DecimalFormat("#.00####");
									row[PR] = price!=null ? decimalFormat.format(price) : "";
									decimalFormat.applyPattern("#.00");
									row[MP] = price!=null ? decimalFormat.format(price.multiply(new BigDecimal(fp.getForUnits()))) : "";
									row[FR] = ""+fp.getForUnits();
									row[TL] = price!=null && (totalPrice = price.multiply(new BigDecimal(Integer.parseInt(row[QT])))).compareTo(new BigDecimal(0))!=0 ? decimalFormat.format(totalPrice) : null;
									table.add( new Row(Arrays.copyOf(row, row.length)));
						}
					}
				}
			}
		}
		String partnamber = costBean.getPartnamber();
		String description = costBean.getDescription();
		if(partnamber!=null || description!=null)
			table.setTitle(new HTMLHeader(partnamber+" - "+description, "cBlue", 3));

		return table;
	}

	public Table getTable(){

		List<CostUnitBean> costUnitBeans = costBean.getCostUnitBeans();
		if(costBean.getSetIndex()>0 && costBean.isSet()){
			CostSetUnit[] costSetUnits = new CostDAO().getCostSetUnits(costBean.getId(), costBean.getSetIndex());
			logger.trace(costSetUnits);
			if(costSetUnits!=null){
				reset();
				for(CostSetUnit csu:costSetUnits){
					int index = costUnitBeans.indexOf(new CostUnitService(csu.getComponentId(), 0, null, null, null, 0));
					if(index>=0){
						CostUnitBean cu = costUnitBeans.get(index);
						CostUnitService.setSelectedMfrPN(cu, csu.getMfrPNInddex()) ;
						CostUnitService.setSelectedCompanyIndex(cu, csu.getCompanyIndex()) ;
						CostUnitService.setSelectedForPrice(cu, csu.getMoqIndex()) ;
					}
				}
			}
		}

		BigDecimal totalPrice = new BigDecimal(0);
		Table table = new Table(new String[]{"Part Number","Description","Mfr P/N","Mfr","Vendor","Price","MOQ","MOP","Qty","Total"}, null);
		DecimalFormat decimalFormat = new DecimalFormat("#.00####");

		for(CostUnitBean cu:costUnitBeans){
			logger.trace(cu);
			BigDecimal price = CostUnitService.getPrice(cu);

			List<CostCompanyService> companies = CostUnitService.getCompanyServices(cu.getMfrPartNumbers());
			logger.trace("\n\tprice:\t{};\n\t{}", price, companies);
			ArrayList<CostMfrPNBean> mfrPartNumbers = cu.getMfrPartNumbers();
			List<ForPriceBean> forPrices = CostUnitService.getForPrices(cu) ;
			boolean isEdit = costBean.isEdit();
			int qty = cu.getQty();
			BigDecimal lineTotal = new BigDecimal(qty);
			table.add(new Row(new String[]{
					//Part Number
					cu.getPartNumberStr(),
					//Description
					cu.getDescription(),
					//Mfr P/N
					mfrPartNumbers.size()>1
							? HTMLWork.getHtmlSelect(CostMfrPNService.toArray(mfrPartNumbers), ""+CostUnitService.getMfrPartNumberIndex(cu), "alt"+cu.getComponentId(),"onchange=\"whatEdit('mfrPN"+cu.getComponentId()+":'+this.selectedIndex)\"", null)
							: CostUnitService.getMfrPN(cu),
					//Mfr
					CostUnitService.getMfr(cu),
					//Vendor
					companies!=null && companies.size()>1
							? HTMLWork.getHtmlSelect(companies.toArray(new CostCompanyService[companies.size()]), ""+CostUnitService.getCompanyId(cu), "vandor"+cu.getComponentId(), "onchange=\"whatEdit('vendor"+cu.getComponentId()+":'+this.selectedIndex)\"", null)
							: CostUnitService.getCompanyName(cu),
					//Price
					isEdit	? "<input class=\"c3em\" type=\"text\" id=\"price"+cu.getComponentId()+"\" name=\"price"+cu.getComponentId()+"\""+(price!=null	? " value=\""+decimalFormat.format( price)+"\""
																																							: "") +" />"
							: price!=null	? decimalFormat.format(price)
											: null,
					//MOQ
					forPrices!=null && forPrices.size()>1 || isEdit ? HTMLWork.getHtmlSelect(forPrices,CostUnitService.getMinimumOrderQty(cu),"for"+cu.getComponentId(),"onchange=\"whatEdit('for"+cu.getComponentId()+":'+this.selectedIndex)\"") : CostUnitService.getMinimumOrderQty(cu)>=0 ? ""+CostUnitService.getMinimumOrderQty(cu) : "",
					//MOP
					price!=null ? decimalFormat.format(price.multiply(new BigDecimal(CostUnitService.getMinimumOrderQty(cu)))): null,
					//Qty
					""+qty,
					//Total
					price!=null  ? decimalFormat.format(price.multiply(lineTotal)) : null}));

			if(price!=null)
				totalPrice = totalPrice.add(lineTotal);
		}
		if(totalPrice.compareTo(new BigDecimal(0))!=0){
			Row row = new Row(new String[]{"","","","","","","Total","","", decimalFormat.format(totalPrice)});
			table.add(row);
			row.setClassName("cBgYellow");
		}
		String description = costBean.getDescription();
		String partnamber = costBean.getPartnamber();
		if(partnamber!=null || description!=null)
		table.setTitle(new HTMLHeader(partnamber+(description!=null ? " - "+description : ""), "cBlue", 3));
		return table;
	}

	public void reset() {
		for(CostUnitBean cu:costBean.getCostUnitBeans())
			CostUnitService.reset(cu);
	}

	public boolean isEdit() {
		return costBean.isEdit();
	}

	public void setEdit(boolean isEdit) {
		costBean.setEdit(isEdit);
	}

	public void setForUnit(int componentId, int forUnit) {
		List<CostUnitBean> costUnitBeans = costBean.getCostUnitBeans();
		int index = costUnitBeans.indexOf(new CostUnitService(componentId, 0, null, null, null, 0));
		if(index>=0)
			CostUnitService.setForUnits(costUnitBeans.get(index), forUnit) ;
	}

	public boolean isChanged(){
		boolean isChanged = false;

		for(CostUnitBean cu:costBean.getCostUnitBeans())
			if(CostUnitService.isChanged(cu)){
				isChanged = true;
				break;
			}

		return isChanged;
	}

	public void setPrices(int componentId, String priceStr){
		List<CostUnitBean> costUnitBeans = costBean.getCostUnitBeans();
		int index = costUnitBeans.indexOf(new CostUnitService(componentId, 0, null, null, null, 0));
		if(index>=0 && !priceStr.isEmpty())
			CostUnitService.setPrice(costUnitBeans.get(index), new BigDecimal(priceStr));
	}

	public CostUnitBean getCostUnitBean(int componentId) {
		CostUnitBean costUnitBean = null;
		for(CostUnitBean cub:costBean.getCostUnitBeans())
			if(cub.getComponentId()==componentId){
				costUnitBean = cub;
				break;
			}
		return costUnitBean;
	}

	public List<CostUnitBean> getCostUnitBeans() {
		return costBean.getCostUnitBeans();
	}

	public String getClassId() {
		String classId = costBean.getClassId();
		return classId!=null ? classId : "";
	}

	public String getComponentId() {
		String componentId = costBean.getComponentId();
		return componentId!=null ? componentId : "";
	}

	public void setClassId(String classId) {
		costBean.setClassId(classId);
	}

	public CostService setComponentId(String componentId) {
		costBean.setComponentId(componentId);
		return this;
	}

	public void setSelectedCompanyIndex(String id_index) {
		if(id_index!=null){
			String[] split = id_index.split(":");
			if(split.length==2) {
				List<CostUnitBean> costUnitBeans = costBean.getCostUnitBeans();
				CostUnitBean costUnit = costUnitBeans.get(costUnitBeans.indexOf(new CostUnitService(Integer.parseInt(split[0].replaceAll("\\D", "")), 0, null, null, null, 0)));
				CostUnitService.setSelectedCompanyIndex(costUnit, Integer.parseInt(split[1]));
			}
		}
	}

	public void setSelectedMfrPNIndex(String id_index) {
		if(id_index!=null){
			String[] split = id_index.split(":");
			if(split.length==2) {
				CostUnitService cu = new CostUnitService(Integer.parseInt(split[0].replaceAll("\\D", "")), 0, null, null, null, 0);
				List<CostUnitBean> costUnitBeans = costBean.getCostUnitBeans();
				int indexOf = costUnitBeans.indexOf(cu);
				if(indexOf>=0){
					CostUnitBean costUnit = costUnitBeans.get(indexOf);
					CostUnitService.setSelectedMfrPN(costUnit, Integer.parseInt(split[1]));
				}
			}
		}
	}

	public boolean setForPriceIndex(int id, int index) {

		List<CostUnitBean> costUnitBeans = costBean.getCostUnitBeans();
		CostUnitBean costUnit = costUnitBeans.get(costUnitBeans.indexOf(new CostUnitService(id, 0, null, null, null, 0)));
		boolean isSet = CostUnitService.setForPriceIndex(costUnit, index) ;

		return isSet;
	}

	public void add(CostUnitBean costUnitBean) {
		List<CostUnitBean> costUnitBeans = costBean.getCostUnitBeans();
		if(costUnitBeans.contains(costUnitBean)){
			CostUnitBean cuTmp = costUnitBeans.get(costUnitBeans.indexOf(costUnitBean));
			CostUnitService.add(cuTmp, costUnitBean.getMfrPartNumbers()) ;
		}else
			costUnitBeans.add(costUnitBean);
	}

	public void add(int componentId, CostCompanyBean costCompanyBean) {
		List<CostUnitBean> costUnitBeans = costBean.getCostUnitBeans();

		logger.trace("\n\tadd(int componentId={}, CostCompanyBean costCompany={})\n\tcostUnitBeans=\t", componentId, costCompanyBean, costUnitBeans);

		int indexOf = costUnitBeans.indexOf(new CostUnitService(componentId, 0, null, null, null, 0));
		if(indexOf>=0){
			CostUnitBean costUnitBean = costUnitBeans.get(indexOf);
			CostMfrPNBean mfrPartNumberBean = CostUnitService.getMfrPartNumberBean(costUnitBean);
			CostMfrPNService.add(mfrPartNumberBean, costCompanyBean);
		}else
			logger.warn("Component with componentId = '{}' do not exist.", componentId);
	}

	public void remove(int componentId, CostCompanyBean costCompanyBean) {
		List<CostUnitBean> costUnitBeans = costBean.getCostUnitBeans();

		logger.trace("\n\tadd(int componentId={}, CostCompanyBean costCompany={})\n\tcostUnitBeans=\t", componentId, costCompanyBean, costUnitBeans);

		int indexOf = costUnitBeans.indexOf(new CostUnitService(componentId, 0, null, null, null, 0));
		if(indexOf>=0){
			CostUnitBean costUnitBean = costUnitBeans.get(indexOf);
			CostMfrPNBean mfrPartNumberBean = CostUnitService.getMfrPartNumberBean(costUnitBean);
			CostMfrPNService.remove(mfrPartNumberBean, costCompanyBean);
		}else
			logger.warn("Component with componentId = '{}' do not exist.", componentId);
	}

	public int getId() {
		return costBean.getId();
	}

	public boolean isSet(){
		return !costBean.getCostUnitBeans().isEmpty();
	}

	public CostMfrPNBean getCostMfrPN(int id) {

		List<CostUnitBean> costUnitBeans = costBean.getCostUnitBeans();
		int indexOf = costUnitBeans.indexOf(new CostUnitService(id, 0, null, null, null, 0));
		CostMfrPNBean costMfrPNBean = null;
		if(indexOf>=0)
			costMfrPNBean = CostUnitService.getMfrPartNumberBean(costUnitBeans.get(indexOf));

		logger.debug("\n\t"
				+ "ID=\t{}\n\t"
				+ "indexOf=\t{}\n\t"
				+ "costMfrPNBean:\t{}", id, indexOf, costMfrPNBean);
		return costMfrPNBean;
	}

	public CostCompanyBean getCostCompany(int id) {
		List<CostUnitBean> costUnitBeans = costBean.getCostUnitBeans();
		CostMfrPNBean costMfrPN = CostUnitService.getMfrPartNumberBean(costUnitBeans.get(costUnitBeans.indexOf(new CostUnitService(id, 0, null, null, null, 0)))) ;
		logger.debug("\n\t{}", costMfrPN);
		return costMfrPN!=null ? CostMfrPNService.getCostCompanyBean(costMfrPN) : null;
	}

	public void setSetIndex(int setIndex) {
		costBean.setSetIndex(setIndex);
	}

	public int getSetIndex() {
		return costBean.getSetIndex();
	}

	public boolean hasSet(){
		return new CostDAO().hasSet(costBean.getSetIndex(), costBean.getId());
	}

	public void setSet(boolean isSet) {
		costBean.setSet( isSet);
	}

//Cost [id=40,
//	partnamber=00C-10E4CS-02504,
//	description=10nF,
//	costUnits=[
//		CostUnit [componentId=40,
//				partNumberStr=00C-10E4CS-02504,
//				description=10nF,
//				selectedIndex=0,
//				qty=8420,
//				mfrPartNumbers=[
//					CostMfrPN [id=0,
//							mfrPN=GRM155R71E103KA01D,
//							mfr=Murata,
//							selectedIndex=1,
//							costCompanies=[
//								CostCompany [id=0,
//											name=Murata,
//											selectedIndex=0,
//											forPrices=[
//												ForPrice [forUnit=1000,
//															price=0.0040000,
//															newPrice=null,
//															getStatus()=SAVED]
//											],
//											isSet()=true
//								],
//								CostCompany [id=6, name=Future electronics, selectedIndex=0, forPrices=[ForPrice [forUnit=10000, price=0.0023000, newPrice=null, getStatus()=SAVED]], isSet()=true]]], CostMfrPN [id=60, mfrPN=GRM155R71H103KA88D, mfr=Murata, selectedIndex=0, costCompanies=[CostCompany [id=6, name=Future electronics, selectedIndex=0, forPrices=[ForPrice [forUnit=10000, price=0E-7, newPrice=null, getStatus()=SAVED]], isSet()=true]]]]]], isEdit=true, classId=Select, componentId=null, position=65px:445px, setIndex=0, isSet=false]
		public static CostService parseCost(String cookieValue) {
		// TODO Auto-generated method stub
		return null;
	}

		public CostBean getCostBean() {
			return costBean;
		}

		public void setCostBean(CostBean costBean) {
			this.costBean = costBean;
		}

		public void clearChanges() {
			for(CostUnitBean cub:costBean.getCostUnitBeans())
				for(CostMfrPNBean cmpnb:cub.getMfrPartNumbers())
					for(CostCompanyBean ccb:cmpnb.getCostCompanyBeans())
						for(ForPriceBean fpb:ccb.getForPriceBeans())
							if(fpb.getNewPrice()!=null)
								if(fpb.getPrice()==null)
									CostCompanyService.remove(ccb, fpb);
								else
									fpb.setNewPrice(null);
			
		}

		@Override
		public String toString() {
			return "CostService [costBean=" + costBean + "]";
		}
}
