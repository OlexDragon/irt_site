package irt.data.purchase;

import irt.data.HTMLWork;
import irt.data.dao.CostDAO;
import irt.table.HTMLHeader;
import irt.table.Row;
import irt.table.Table;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Cost {

	private int id;
	private String partnamber;
	private String description;
	private List<CostUnit> costUnits = new ArrayList<>();
	private boolean isEdit;

	private String classId = "";
	private String componentId = "";
	private String position;
	private int setIndex;
	private boolean isSet;

	public Cost(int id, String partnamber, String description) {
		super();
		this.id = id;
		this.partnamber = partnamber;
		this.description = description;
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
		for(CostUnit cu:costUnits){
			Price totalPrice ;
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
				for(CostMfrPN cmpn:cu.getMfrPartNumbers()){
					if(cmpn.getAlternativeUnit()!=null){
						CostUnit alt = cmpn.getAlternativeUnit();
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
					if(cmpn.getCostCompanies().isEmpty()){
						table.add( new Row(Arrays.copyOf(row, row.length)));
						row[VD] =
						row[PR] =
						row[MP] =
						row[FR] =
						row[TL] = null;
					}else{
						for(CostCompany cc:cmpn.getCostCompanies()){
							row[VD] = cc.getName();
							if(cmpn.getForPrices().isEmpty()){
								table.add( new Row(Arrays.copyOf(row, row.length)));
								row[PR] =
								row[MP] =
								row[PR] =
								row[TL] = null;
							}else
								for(ForPrice fp:cc.getForPrices()) {
									Price price = fp.getPrice();
									row[PR] = price.getValue(2, 6);
									row[MP] = new Price(price.getValue(2, 6), fp.getForUnits()).getValue(2, 2);
									row[FR] = ""+fp.getForUnits();
									row[TL] = price!=null && !((totalPrice = new Price(price.getValue(6, 6), Integer.parseInt(row[QT]))).getValueLong()==0) ? totalPrice.getValue(2, 2) : null;
									table.add( new Row(Arrays.copyOf(row, row.length)));
						}
					}
				}
			}
		}
		if(partnamber!=null || description!=null)
		table.setTitle(new HTMLHeader(partnamber+" - "+description, "cBlue", 3));
		return table;
	}

	public Table getTable(){
		Table table = new Table(new String[]{"Part Number","Description","Mfr P/N","Mfr","Vendor","Price","MOQ","MOP","Qty","Total"}, null);
		Price totalPrice = new Price(0);

		if(setIndex>0 && isSet){
			CostSetUnit[] costSetUnits = new CostDAO().getCostSetUnits(id, setIndex);
			if(costSetUnits!=null){
				reset();
				for(CostSetUnit csu:costSetUnits){
					int index = costUnits.indexOf(new CostUnit(csu.getComponentId(), 0, null, null, null, 0));
					if(index>=0){
						CostUnit cu = costUnits.get(index);
						cu.setSelectedMfrPN(csu.getMfrPNInddex());
						cu.setSelectedCompanyIndex(csu.getCompanyIndex());
						cu.setSelectedForPrice(csu.getMoqIndex());
					}
				}
			}
		}

		for(CostUnit cu:costUnits){
			Price price = null;
			if(cu.getPrice()!=null)
				price = new Price(cu.getPrice().getValue(6, 6), cu.getQty());
			List<CostCompany> companies = cu.getCompanies();
			ArrayList<CostMfrPN> mfrPartNumbers = cu.getMfrPartNumbers();
			List<ForPrice> forPrices = cu.getForPrices();
			table.add(new Row(new String[]{cu.getPartNumberStr(),
											cu.getDescription(),
											mfrPartNumbers.size()>1 ? HTMLWork.getHtmlSelect(mfrPartNumbers.toArray(new CostMfrPN[mfrPartNumbers.size()]), ""+cu.getMfrPartNumberIndex(), "alt"+cu.getComponentId(),"onchange=\"whatEdit('mfrPN"+cu.getComponentId()+":'+this.selectedIndex)\"", null) : cu.getMfrPN(),
											cu.getMfr(),
											companies!=null && companies.size()>1 ? HTMLWork.getHtmlSelect(companies.toArray(new CostCompany[companies.size()]), ""+cu.getCompanyId(), "vandor"+cu.getComponentId(), "onchange=\"whatEdit('vendor"+cu.getComponentId()+":'+this.selectedIndex)\"", null): cu.getCompanyName(),
											isEdit ? "<input class=\"c3em\" type=\"text\" id=\"price"+cu.getComponentId()+"\" name=\"price"+cu.getComponentId()+"\""+(cu.getPrice()!=null && cu.getPrice().getValueLong()!=0 ? " value=\""+cu.getPrice().getValue(2, 6)+"\"" : "") +" />": cu.getPrice()!=null ? cu.getPrice().getValue(2, 6) : null,
											forPrices!=null && forPrices.size()>1 || isEdit ? HTMLWork.getHtmlSelect(forPrices,cu.getForUnits(),"for"+cu.getComponentId(),"onchange=\"whatEdit('for"+cu.getComponentId()+":'+this.selectedIndex)\"") : cu.getForUnits()>=0 ? ""+cu.getForUnits() : cu.getForUnits()>0 ? ""+cu.getForUnits() : "",
											cu.getPrice()!=null ? new Price(cu.getPrice().getValue(0, 6), cu.getForUnits()).getValue(2, 2): null,
											""+cu.getQty(),
											price!=null && !(price.getValueLong()==0) ? price.getValue(2, 2) : null}));
			if(price!=null)
				totalPrice.addValue(price.getValueLong());
		}
		if(totalPrice.getValueLong()!=0){
			Row row = new Row(new String[]{"","","","","","","Total","","",totalPrice.getValue(2,2)});
			table.add(row);
			row.setClassName("cBgYellow");
		}
		if(partnamber!=null || description!=null)
		table.setTitle(new HTMLHeader(partnamber+(description!=null ? " - "+description : ""), "cBlue", 3));
		return table;
	}

	public void reset() {
		for(CostUnit cu:costUnits)
			cu.reset();
	}

	public boolean isEdit() {
		return isEdit;
	}

	public void setEdit(boolean isEdit) {
		this.isEdit = isEdit;
	}

	@Override
	public String toString() {
		return "Cost [costUnits=" + costUnits + ", isEdit=" + isEdit + "]";
	}

	public void setForUnit(int componentId, int forUnit) {
		int index = costUnits.indexOf(new CostUnit(componentId, 0, null, null, null, 0));
		if(index>=0)
			costUnits.get(index).setForUnits(forUnit);
	}

	public boolean isChanged(){
		boolean isChanged = false;

		for(CostUnit cu:costUnits)
			if(cu.isChanged()){
				isChanged = true;
				break;
			}

		return isChanged;
	}

	public void setPrices(int componentId, String priceStr){
		int index = costUnits.indexOf(new CostUnit(componentId, 0, null, null, null, 0));
		if(index>=0 && !priceStr.isEmpty())
			costUnits.get(index).setPrice(new Price(priceStr));
	}

	public List<CostUnit> getCostUnits() {
		return costUnits;
	}

	public String getClassId() {
		return classId!=null ? classId : "";
	}

	public String getComponentId() {
		return componentId!=null ? componentId : "";
	}

	public void setClassId(String classId) {
		this.classId = classId;
	}

	public void setComponentId(String componentId) {
		this.componentId = componentId;
	}

	public void setSelectedCompanyIndex(String id_index) {
		if(id_index!=null){
			String[] split = id_index.split(":");
			if(split.length==2) {
				CostUnit costUnit = costUnits.get(costUnits.indexOf(new CostUnit(Integer.parseInt(split[0].replaceAll("\\D", "")), 0, null, null, null, 0)));
				costUnit.setSelectedCompanyIndex(Integer.parseInt(split[1]));
			}
		}
	}

	public void setSelectedMfrPNIndex(String id_index) {
		if(id_index!=null){
			String[] split = id_index.split(":");
			if(split.length==2) {
				CostUnit cu = new CostUnit(Integer.parseInt(split[0].replaceAll("\\D", "")), 0, null, null, null, 0);
				int indexOf = costUnits.indexOf(cu);
				if(indexOf>=0){
					CostUnit costUnit = costUnits.get(indexOf);
					costUnit.setSelectedMfrPN(Integer.parseInt(split[1]));
				}
			}
		}
	}

	public boolean setForPriceIndex(int id, int index) {

		CostUnit costUnit = costUnits.get(costUnits.indexOf(new CostUnit(id, 0, null, null, null, 0)));
		boolean isSet = costUnit.setForPriceIndex(index);

		return isSet;
	}

	public void add(CostUnit costUnit) {
		if(costUnits.contains(costUnit)){
			CostUnit cuTmp = costUnits.get(costUnits.indexOf(costUnit));
			cuTmp.add(costUnit.getMfrPartNumbers());
		}else
			costUnits.add(costUnit);
	}

	public void add(int componentId, CostCompany costCompany) {
		costUnits.get(costUnits.indexOf(new CostUnit(componentId, 0, null, null, null, 0))).getMfrPartNumber().add(costCompany);
	}

	public int getId() {
		return id;
	}

	public boolean isSet(){
		return !costUnits.isEmpty();
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public CostMfrPN getCostMfrPN(int id) {

		int indexOf = costUnits.indexOf(new CostUnit(id, 0, null, null, null, 0));
		CostMfrPN costMfrPN = null;
		if(indexOf>=0)
			costMfrPN = costUnits.get(indexOf).getMfrPartNumber();
		return costMfrPN;
	}

	public CostCompany getCostCompany(int id) {
		CostMfrPN costMfrPN = costUnits.get(costUnits.indexOf(new CostUnit(id, 0, null, null, null, 0))).getMfrPartNumber();
		return costMfrPN!=null ? costMfrPN.getCompany() : null;
	}

	public void setSetIndex(int setIndex) {
		this.setIndex = setIndex;
	}

	public int getSetIndex() {
		return setIndex;
	}

	public boolean hasSet(){
		return new CostDAO().hasSet(setIndex, id);
	}

	public void setSet(boolean isSet) {
		this.isSet = isSet;
	}
}
