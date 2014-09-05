package irt.data.purchase;

import irt.data.HTMLWork;
import irt.data.components.Component;
import irt.data.dao.ComponentDAO;
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
import org.codehaus.jackson.annotate.JsonIgnore;

public class CostService {

	@JsonIgnore
	private final Logger logger = LogManager.getLogger();

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

	@JsonIgnore
	private final static ComponentDAO componentDAO = new ComponentDAO();

	private CostBean costBean;

	public CostService() { }

	public CostService(CostBean costBean) {
		this.costBean = costBean!=null ? costBean : new CostBean();
	}

	public CostBean getCostBean() {
		return costBean;
	}

	public void setCostBean(CostBean costBean) {
		this.costBean = costBean;
	}

	@JsonIgnore
	public Table getComponentTable() {
		String[] titels = new String[]{"ID","Part Number","Mfr P/N","Mfr","Vendor","Price","MOQ","MOP","Qty","Total"};
		Table table = new Table(titels, null);
		for(CostUnitBean cu:costBean.getCostUnitBeans()){
			boolean alternativeComponentBeansEmpty = cu.getAlternativeComponentBeans().isEmpty();
			BigDecimal totalPrice ;
			String[] row = new String[titels.length];

			row[ID]	= ""+cu.getComponentId();
			row[QT] = ""+cu.getQty();

			if(alternativeComponentBeansEmpty){
				table.add( new Row(Arrays.copyOf(row, row.length)));

				row[PN] = CostUnitService.getComponent(cu.getComponentId()).getPartNumberF();

				row[MFR] = 
				row[MPN] = 
				row[VD] =
				row[PR] =
				row[FR] =
				row[MP] =
				row[TL] = null;
			}else

				for(AlternativeComponentBean acb:cu.getAlternativeComponentBeans()){
					int alternativeComponentId = acb.getAlternativeComponentId();
					row[PN] = CostUnitService.getPartNumber(cu, alternativeComponentId);
					row[MPN] = CostUnitService.getMfrPN(cu, alternativeComponentId);
					row[MFR] = CostUnitService.getMfr(cu, alternativeComponentId);
					if(acb.getCostCompanyBeans().isEmpty()){
						table.add( new Row(Arrays.copyOf(row, row.length)));
						row[VD] =
						row[PR] =
						row[MP] =
						row[FR] =
						row[TL] = null;
					}else{
						for(CostCompanyBean cc:acb.getCostCompanyBeans()){
							row[VD] = CostCompanyService.getCompanyName(cc.getId());
							if(AlternativeComponentService.getForPrices(acb).isEmpty()){
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

		Component c =  getTopComponent();

		if(c!=null){
			String partnamber = c.getPartNumberF();
			String description = c.getDescription();
			if(partnamber!=null || description!=null)
				table.setTitle(new HTMLHeader(partnamber+" - "+description, "cBlue", 3));
		}

		return table;
	}

	@JsonIgnore
	private Component getTopComponent() {
		return componentDAO.getComponent(costBean.getTopComponentId());
	}

	@JsonIgnore
	public Table getTable(){

		List<CostUnitBean> costUnitBeans = costBean.getCostUnitBeans();
		if(costBean.getSetIndex()>0 && costBean.isSet()){
			CostSetUnit[] costSetUnits = new CostDAO().getCostSetUnits(costBean.getTopComponentId(), costBean.getSetIndex());
			logger.trace(costSetUnits);
			if(costSetUnits!=null){
				reset();
				for(CostSetUnit csu:costSetUnits){
					int index = costUnitBeans.indexOf(new CostUnitBean().setComponentId(csu.getComponentId()));
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
		BigDecimal totalPriceSum = new BigDecimal(0);
		Table table = new Table(new String[]{"Part Number","Description","Mfr P/N","Mfr","Vendor","Price","MOQ","MOP","Qty","Total"}, null);
		DecimalFormat decimalFormat = new DecimalFormat("#.00####");

		for(CostUnitBean cub:costUnitBeans){
			logger.trace(cub);
			BigDecimal price = CostUnitService.getPrice(cub);
			Component component = CostUnitService.getComponent(cub.getComponentId());

			List<CostCompanyService> companies = CostUnitService.getCompanyServices(cub.getAlternativeComponentBeans());
			logger.trace("\n\tprice:\t{};\n\t{}", price, companies);
			List<ForPriceBean> forPrices = CostUnitService.getForPrices(cub) ;

			boolean isEdit = costBean.isEdit();
			int qty = cub.getQty();
			BigDecimal lineTotal = new BigDecimal(qty);
			boolean acbOnlyOne = cub.getAlternativeComponentBeans().size()<2;
			table.add(new Row(new String[]{
					//Part Number
					acbOnlyOne ? component.getPartNumberF() : CostUnitService.getHTMLSelectPartNumber(cub).toString(),
					//Description
					component.getDescription(),
					//Mfr P/N
					acbOnlyOne ? CostUnitService.getMfrPN(cub) : CostUnitService.getHTMLSelectAlternativeComponentBeans(cub).toString(),
					//Mfr
					CostUnitService.getMfr(cub),
					//Vendor
					companies!=null && companies.size()<2
							? CostUnitService.getCompanyName(cub)
							: CostUnitService.getHTMLSelectCompanyBeans(cub).toString(),
					//Price
					isEdit	? "<input class=\"c3em\" type=\"text\" id=\"price"+cub.getComponentId()+"\" name=\"price"+cub.getComponentId()+"\""+(price!=null	? " value=\""+decimalFormat.format( price)+"\""
																																							: "") +" />"
							: price!=null	? decimalFormat.format(price)
											: null,
					//MOQ
					forPrices!=null && forPrices.size()>1 || isEdit ? HTMLWork.getHtmlSelect(forPrices,CostUnitService.getMinimumOrderQty(cub),"for"+cub.getComponentId(),"onchange=\"whatEdit('for"+cub.getComponentId()+":'+this.selectedIndex)\"") : CostUnitService.getMinimumOrderQty(cub)>=0 ? ""+CostUnitService.getMinimumOrderQty(cub) : "",
					//MOP
					price!=null ? decimalFormat.format(price.multiply(new BigDecimal(CostUnitService.getMinimumOrderQty(cub)))): null,
					//Qty
					""+qty,
					//Total
					price!=null  ? decimalFormat.format(totalPrice = price.multiply(lineTotal)) : null}));

			totalPriceSum = totalPriceSum.add(totalPrice);

		}
		if(totalPriceSum.compareTo(new BigDecimal(0))!=0){
			decimalFormat.applyPattern("#.00");
			Row row = new Row(new String[]{"","","","","","","Total","","", decimalFormat.format(totalPriceSum)});
			table.add(row);
			row.setClassName("cBgYellow");
		}
		Component topComponent = getTopComponent();
		if(topComponent!=null){
			String description = topComponent.getDescription();
			String partnamber = topComponent.getPartNumberF();
			if(partnamber!=null || description!=null)
				table.setTitle(new HTMLHeader(partnamber+(description!=null ? " - "+description : ""), "cBlue", 3));
		}
		return table;
	}

	@JsonIgnore
	public void reset() {
		for(CostUnitBean cu:costBean.getCostUnitBeans())
			CostUnitService.reset(cu);
	}

	@JsonIgnore
	public boolean isEdit() {
		return costBean.isEdit();
	}

	@JsonIgnore
	public void setEdit(boolean isEdit) {
		costBean.setEdit(isEdit);
	}

	@JsonIgnore
	public void setForUnit(int componentId, int forUnit) {
		List<CostUnitBean> costUnitBeans = costBean.getCostUnitBeans();
		int index = costUnitBeans.indexOf(new CostUnitBean().setComponentId(componentId));
		if(index>=0)
			CostUnitService.setForUnits(costUnitBeans.get(index), forUnit) ;
	}

	@JsonIgnore
	public boolean isChanged(){
		boolean isChanged = false;

		for(CostUnitBean cu:costBean.getCostUnitBeans())
			if(cu!=null && CostUnitService.isChanged(cu)){
				isChanged = true;
				break;
			}

		return isChanged;
	}

	@JsonIgnore
	public void setPrices(int componentId, String priceStr){
		logger.entry(componentId, priceStr);
		List<CostUnitBean> costUnitBeans = costBean.getCostUnitBeans();
		int index = costUnitBeans.indexOf(new CostUnitBean().setComponentId(componentId));
		if(index>=0 && !priceStr.isEmpty())
			CostUnitService.setPrice(costUnitBeans.get(index), new BigDecimal(priceStr));
	}

	@JsonIgnore
	public List<CostUnitBean> getCostUnitBeans() {
		return costBean.getCostUnitBeans();
	}

	@JsonIgnore
	public String getClassId() {
		String classId = costBean.getClassId();
		return classId!=null ? classId : "";
	}

	@JsonIgnore
	public int getComponentId() {
		return costBean.getTopComponentId();
	}

	@JsonIgnore
	public void setClassId(String classId) {
		costBean.setClassId(classId);
	}

	@JsonIgnore
	public boolean setSelectedCompanyIndex(String id_index) {

		boolean set = false;

		if(id_index!=null){
			String[] split = id_index.split(":");
			if(split.length==2) {
				List<CostUnitBean> costUnitBeans = costBean.getCostUnitBeans();
				CostUnitBean costUnit = costUnitBeans.get(costUnitBeans.indexOf(new CostUnitBean().setComponentId(Integer.parseInt(split[0].replaceAll("\\D", "")))));
				if(set = costUnit!=null)
					CostUnitService.setSelectedCompanyIndex(costUnit, Integer.parseInt(split[1]));
			}
		}

		return set;
	}

	@JsonIgnore
	public boolean setSelectedMfrPNIndex(String id_index) {
		logger.entry(id_index);
		boolean set = false;
		if(id_index!=null){
			String[] split = id_index.split(":");
			if(split.length==2) {

				CostUnitBean cubTmp = new CostUnitBean().setComponentId(Integer.parseInt(split[0].replaceAll("\\D", "")));
				List<CostUnitBean> costUnitBeans = costBean.getCostUnitBeans();

				int indexOf = costUnitBeans.indexOf(cubTmp);
				logger.trace("\n\tindexOf = {}", indexOf);

				if(indexOf>=0){
					CostUnitBean costUnit = costUnitBeans.get(indexOf);
					if(costUnit!=null)
						set = CostUnitService.setSelectedMfrPN(costUnit, Integer.parseInt(split[1]));
				}
			}
		}
		return logger.exit(set);
	}

	@JsonIgnore
	public boolean setForPriceIndex(int id, int index) {

		List<CostUnitBean> costUnitBeans = costBean.getCostUnitBeans();
		CostUnitBean costUnit = costUnitBeans.get(costUnitBeans.indexOf(new CostUnitBean().setComponentId(id)));
		boolean isSet = CostUnitService.setForPriceIndex(costUnit, index) ;

		return isSet;
	}

	@JsonIgnore
	public boolean add(CostUnitBean costUnitBean) {
		logger.entry("\n\t", costUnitBean);

		boolean added = false;
		List<CostUnitBean> costUnitBeans = costBean.getCostUnitBeans();

		int indexOf = costUnitBeans.indexOf(costUnitBean);
		logger.trace("\n\tindexOf = {}", indexOf);

		if(indexOf>=0){
			CostUnitBean cuTmp = costUnitBeans.get(indexOf);
			added = CostUnitService.add(costUnitBean.getAlternativeComponentBeans(), cuTmp.getAlternativeComponentBeans()) ;
		}else
			added = costUnitBeans.add(costUnitBean);

		return logger.exit(added);
	}

	@JsonIgnore
	public void add(int componentId, CostCompanyBean costCompanyBean) {

		logger.trace("\n\tadd(int componentId={}, CostCompanyBean costCompany={})", componentId, costCompanyBean);

		CostUnitBean costUnitBean = getCostUnitBean(componentId);
		if(costUnitBean!=null){
			AlternativeComponentBean alternativeComponentBean = CostUnitService.getAlternativeComponentBean(costUnitBean);
			List<CostCompanyBean> ccbs = new ArrayList<>();
			ccbs.add(costCompanyBean);
			AlternativeComponentService.add(ccbs, alternativeComponentBean.getCostCompanyBeans());
		}else
			logger.warn("Component with componentId = '{}' do not exist.", componentId);
	}

	@JsonIgnore
	public void setSelectedIndex(int componentId, CostCompanyBean costCompanyBean) {
		CostUnitBean costUnitBean = getCostUnitBean(componentId);
		if(costUnitBean!=null);
			for(AlternativeComponentBean acb:costUnitBean.getAlternativeComponentBeans()){
				List<CostCompanyBean> costCompanyBeans = acb.getCostCompanyBeans();
				int indexOf = costCompanyBeans.indexOf(costUnitBean);
				if(indexOf>=0)
					acb.setSelectedIndex(indexOf);
			}
	}

	@JsonIgnore
	public CostUnitBean getCostUnitBean(int componentId) {

		CostUnitBean costUnitBean = null;
		List<CostUnitBean> costUnitBeans = costBean.getCostUnitBeans();
		int indexOf = costUnitBeans.indexOf(new CostUnitBean().setComponentId(componentId));

		if(indexOf>=0)
			costUnitBean = costUnitBeans.get(indexOf);

		return costUnitBean;
	}

	@JsonIgnore
	public void remove(int componentId, CostCompanyBean costCompanyBean) {
		List<CostUnitBean> costUnitBeans = costBean.getCostUnitBeans();

		logger.trace("\n\tadd(int componentId={}, CostCompanyBean costCompany={})\n\tcostUnitBeans=\t", componentId, costCompanyBean, costUnitBeans);

		int indexOf = costUnitBeans.indexOf(new CostUnitBean().setComponentId(componentId));
		if(indexOf>=0){
			CostUnitBean costUnitBean = costUnitBeans.get(indexOf);
			AlternativeComponentBean mfrPartNumberBean = CostUnitService.getAlternativeComponentBean(costUnitBean);
			AlternativeComponentService.remove(mfrPartNumberBean, costCompanyBean);
		}else
			logger.warn("Component with componentId = '{}' do not exist.", componentId);
	}

	@JsonIgnore
	public int getId() {
		return costBean.getTopComponentId();
	}

	@JsonIgnore
	public boolean isSet(){
		return !costBean.getCostUnitBeans().isEmpty();
	}

	@JsonIgnore
	public AlternativeComponentBean getCostMfrPN(int componentId) {

		List<CostUnitBean> costUnitBeans = costBean.getCostUnitBeans();
		int indexOf = costUnitBeans.indexOf(new CostUnitBean().setComponentId(componentId));
		AlternativeComponentBean costMfrPNBean = null;
		if(indexOf>=0)
			costMfrPNBean = CostUnitService.getAlternativeComponentBean(costUnitBeans.get(indexOf));

		logger.debug("\n\t"
				+ "ID=\t{}\n\t"
				+ "indexOf=\t{}\n\t"
				+ "costMfrPNBean:\t{}", componentId, indexOf, costMfrPNBean);
		return costMfrPNBean;
	}

	@JsonIgnore
	public CostCompanyBean getCostCompany(int componentId) {
		List<CostUnitBean> costUnitBeans = costBean.getCostUnitBeans();
		int indexOf = costUnitBeans.indexOf(new CostUnitBean().setComponentId(componentId));
		AlternativeComponentBean costMfrPN = null;

		if(indexOf>=0)
			costMfrPN = CostUnitService.getAlternativeComponentBean(costUnitBeans.get(indexOf)) ;

		logger.debug("\n\t{}", costMfrPN);
		return costMfrPN!=null ? AlternativeComponentService.getCostCompanyBean(costMfrPN) : null;
	}

	@JsonIgnore
	public void setSetIndex(int setIndex) {
		costBean.setSetIndex(setIndex);
	}

	@JsonIgnore
	public int getSetIndex() {
		return costBean.getSetIndex();
	}

	public boolean hasSet(){
		return new CostDAO().hasSet(costBean.getSetIndex(), costBean.getTopComponentId());
	}

	@JsonIgnore
	public void setSet(boolean isSet) {
		costBean.setSet( isSet);
	}

	@JsonIgnore
		public void clearChanges() {
			for(CostUnitBean cub:costBean.getCostUnitBeans())
				for(AlternativeComponentBean cmpnb:cub.getAlternativeComponentBeans())
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
