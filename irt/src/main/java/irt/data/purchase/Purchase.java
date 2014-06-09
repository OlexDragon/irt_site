package irt.data.purchase;

import irt.data.HTMLWork;
import irt.data.Menu;
import irt.data.companies.Company;
import irt.data.components.Component;
import irt.data.components.ComponentIds;
import irt.data.components.Data;
import irt.data.Error;
import irt.data.components.movement.ComponentToMove;
import irt.data.components.movement.ComponentsQuantity;
import irt.data.dao.BomDAO;
import irt.data.dao.CompanyDAO;
import irt.data.dao.ComponentDAO;
import irt.data.dao.ComponentsMovementDAO;
import irt.data.dao.KitDAO;
import irt.data.dao.ManufactureDAO;
import irt.data.dao.MenuDAO;
import irt.data.manufacture.ManufacturePartNumber;
import irt.data.user.UserBean;
import irt.table.Field;
import irt.table.HTMLHeader;
import irt.table.OrderBy;
import irt.table.Row;
import irt.table.Table;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.servlet.http.HttpServletRequest;

public class Purchase {

	public static final int PAGE_PURCHASE	= 0;
	public static final int PAGE_MOVEMENT	= 1;
	public static final int PAGE_VENDORS	= 2;
	public static final int PAGE_CM			= 3;
	public static final Menu PAGE_MENU	= new MenuDAO().getMenu("po_page","description");
	public static final String PAGE_PURCHASE_STR	= PAGE_MENU.getDescription(PAGE_PURCHASE);
	public static final String PAGE_MOVEMENT_STR	= PAGE_MENU.getDescription(PAGE_MOVEMENT);
	public static final String PAGE_VENDOR_STR		= PAGE_MENU.getDescription(PAGE_VENDORS);
	public static final String PAGE_CO_MANUF_STR	= PAGE_MENU.getDescription(PAGE_CM);

	private int activePage = PAGE_PURCHASE;// "Sellers" or "Purchase" or "Suppliers"

	private static Error error = new Error();

	private List<Company>	allCompanys;
	private List<ComponentIds> topComponents;		//All components with BOM
	private List<ComponentIds> selectedComponents;	//selected top components

//	private PurchaseOrder purchaseOrder;

	private Table table;
	private Company company;
	private boolean isEditCompany;
	private boolean showAll;
	private boolean isAddCompany;
	private int quantityToAdd = 0;
	private boolean inPersent = false;
	private int orderQty = -1;
	private PurchaseOrder purchaseOrder;
	private int statusToShow = PurchaseOrder.ACTIVE;
	private OrderBy orderBy;
	private boolean isOrderByReference = true;
	private ComponentsQuantity alternative;
	private int kitId;	//use in getTable
	private int companyId;//use in getTable

	public Purchase(){ setTopComponents();	}

	public void setTopComponents() {
		topComponents = new BomDAO().getAllComponentsIdsWithBom();
	}

	public void setActivePage(HttpServletRequest request) {

		int selectedPage = PAGE_MENU.getIntKey(request.getParameter("po"));
		showAll = request.getParameter("show_all")!=null;

		if(selectedPage!=activePage)
			activePage  = selectedPage;

		if(activePage==PAGE_PURCHASE || activePage==PAGE_MOVEMENT){
			company = new CompanyDAO().getCompany(request.getParameter("company"));

			alternative = HTMLWork.getHtmlFields(request, "alt");

			String kitIdStr = request.getParameter("kit");
			kitId = kitIdStr!=null && !(kitIdStr = kitIdStr.replaceAll("\\D", "")).isEmpty() ? Integer.parseInt(kitIdStr) : 0;

			String companyStr = request.getParameter("company");
			companyId = companyStr!=null && !(companyStr = companyStr.replaceAll("\\D", "")).isEmpty() ? Integer.parseInt(companyStr) : 0;

			setTopPartNumbers(request);
			getBomTable();
		}
	}

	private void getBomTable() {

		if (selectedComponents.isEmpty()) 
			setErrorMessage("Select the Top Level Component.");
		else if(selectedContainsZero())
			setErrorMessage("Type the Quantity.");
		else {
			table = activePage==PAGE_PURCHASE ? new BomDAO().getPOTable(selectedComponents, companyId, orderBy) : new BomDAO().getCMTable(selectedComponents, companyId, kitId, orderBy);
			if (table != null) {

				List<Row> rows = table.getRows();
				ExecutorService executor = Executors.newFixedThreadPool(16);

				for (int i = 1; i < rows.size(); i++) 
					executor.execute(new PurchaseData(companyId, kitId, rows.get(i), alternative, activePage, quantityToAdd, inPersent));

				executor.shutdown();

				while(!executor.isTerminated());

				table.hideColumn(0);
			}
			if(orderQty>=0)
				orderQty = -1;
		}
	}

//****************************************************************************************
	public static class PurchaseData extends Thread {

		private static final int FIELD_ID		= 0;
		private static final int PART_NUMBER	= 1;
		private static final int MFR_PN			= 2;
		private static final int NEED_QTY		= 3;
		private static final int STOCK_QTY		= 4;
		private static final int COMPANY_QTY	= 5;
		private int location;
		private int mQty;
		private Row row;
		private int companyId;
		private int kitId;
		private ComponentsQuantity alternative;
		private int activePage;
		private int quantityToAdd;
		private boolean inPersent;
		private int kQty;

		public PurchaseData(int companyId, int kitId, Row row, ComponentsQuantity alteernative, int activePage, int quantityToAdd, boolean inPersent) {
			this.companyId = companyId;
			this.kitId = kitId;
			this.row = row;
			this.alternative = alteernative;
			this.activePage = activePage;
			this.quantityToAdd = quantityToAdd;
			this.inPersent = inPersent;
		}
		@Override
		public void run() {
			kQty = location = 5;
			mQty =6;

			if(companyId>0){
				kQty++;
				mQty++;
				location++;
			}

			if(kitId>0){
				mQty++;
				location++;
			}

			List<Field> fs = row.getRow();
			Field fId= fs.get(FIELD_ID);
			Field fPN = fs.get(PART_NUMBER);
			Field mf = fs.get(MFR_PN);
			Field nf = fs.get(NEED_QTY);
			Field sf = fs.get(STOCK_QTY);
			Field cf = companyId>0 ? fs.get(COMPANY_QTY) : null;
			Field kf = kitId>0 ? fs.get(kQty) : null;
			Field lf = fs.get(location);
			Field tf = fs.get(mQty);
			int componentId = fId.getIntValue();
			int need = Integer.parseInt(nf.getValue());
			int comp = companyId>0 ? Integer.parseInt(cf.getValue()) : 0;
			int kit = kitId>0 ? Integer.parseInt(kf.getValue()) : 0;
			int toMove = Integer.parseInt(tf.getValue());
			int stock = Integer.parseInt(sf.getValue());

			ComponentToMove[] alternativeComponents = new ComponentDAO().getAlternativeComponentsRows(componentId);
			if(alternativeComponents!=null){
				if(alternative!=null){
					int index = alternative.indexOf(alternativeComponents);
					if(index>=0){
						int altId = alternative.get(index).getQuantityToMove();//get alternative component ID
						if(altId!=componentId){
							componentId = altId;
							index = getIndex(alternativeComponents, altId);
							if(index>=0){
								stock = alternativeComponents[index].getStockQuantity();
								sf.setValue(""+stock);
								lf.setValue(alternativeComponents[index].getLocation());
								mf.setValue(alternativeComponents[index].getManufPartNumber());
								tf.setValue(""+(activePage==PAGE_PURCHASE ? stock<need ? need-stock : 0 : stock>need ? need : stock));
								if(kf!=null){
									kf.setValue(""+(kit = new KitDAO().getQuantity(kitId, componentId)-kit));
									toMove -= kit;
								}
								if(cf!=null){
									cf.setValue(""+(comp = new ComponentsMovementDAO().getComponyComponentQuantity(companyId, componentId)-comp));
									toMove -= comp;
								}
							}
						}
					}
				}
				fPN.setValue(HTMLWork.getHtmlSelect(alternativeComponents, componentId, "alt"+componentId, "onchange=\"oneClick('submit')\""));
			}

			if(quantityToAdd>0)
				if(inPersent)
					need *= quantityToAdd /100.0 +1; 
				else
					need += quantityToAdd;

			if (activePage == PAGE_PURCHASE)
				toMove = need > stock + comp ? need-(comp+stock) : 0;
			else
				toMove = need > stock+kit+comp ? stock : need-kit-comp;

			if(toMove<0){
				toMove = 0;
			}
			if(activePage==PAGE_PURCHASE && toMove>0 || activePage==PAGE_MOVEMENT && need>stock+kit+comp)
				row.setClassName("red");
			tf.setValue("<input type=\"text\" class=\"c3em\" name=\"qty"+componentId+"\" id=\"qty"+componentId+"\" value=\""+toMove+"\" />");
		}
	}
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	private static int getIndex(ComponentToMove[] alternativeComponents, int altId) {
		int index = -1;
		for(int i=0; i<alternativeComponents.length; i++)
			if(alternativeComponents[i].getId()==altId){
				index = i;
				break;
			}
		return index;
	}

	public int getActivePage() {
		return activePage;
	}

	private boolean selectedContainsZero() {
		boolean contains = false;
		if(selectedComponents!=null)
			for(ComponentIds sc:selectedComponents)
				if(sc.getQuantity()<=0){
					contains = true;
					break; 
				}
		return contains;
	}

	public void setOrderQty(int orderQty) {
		this.orderQty  = orderQty;
	}

	public void addOrderQuantity(HttpServletRequest request) {
		String qtyStr = request.getParameter("txt_add");

		if(qtyStr!=null){
			inPersent = qtyStr.contains("%") ? true : false;
			if(!(qtyStr = qtyStr.replaceAll("[^\\d]", "")).isEmpty())
				quantityToAdd = Integer.parseInt(qtyStr);
			else
				quantityToAdd = 0;
			getBomTable();
		}
	}

	public void addComponents(List<String> cimponentsIdsList) {
		if(cimponentsIdsList!=null)
			for(String cId:cimponentsIdsList)
				if(!(cId = cId.replaceAll("\\D", "")).isEmpty())
					addComponent(Integer.parseInt(cId));
	}

	public void addComponent(String partNumberStr) {
		addComponent((Component)new ComponentDAO().getData(partNumberStr));
	}

	private void addComponent(int componentId) {
		addComponent(new ComponentDAO().getComponent(componentId));
	}

	public void addComponent(Component component) {
		if(component!=null && component.getId()>0){
			List<PurchaseOrderUnit> purchaseOrderUnits;
			if(purchaseOrder==null)
				purchaseOrder = new PurchaseOrder(purchaseOrderUnits = new ArrayList<PurchaseOrderUnit>());
			else
				purchaseOrderUnits = purchaseOrder.getPurchaseOrderUnits();

			PurchaseOrderUnit purchaseOrderUnit = new PurchaseOrderUnit(component.getId(), component.getPartNumberF(), component.getDescription(), new ManufacturePartNumber(0, component.getManufPartNumber(), new ManufactureDAO().getMfrName(component.getManufId())));
			purchaseOrderUnit.setOrderQuantity(1);
			if(!purchaseOrderUnits.contains(purchaseOrderUnit))
				purchaseOrderUnits.add(purchaseOrderUnit);
			else
				component.setError("The component "+component.getPartNumberF()+" already exist in the PO. <small>(E042)</small>");
		}else
			component.setError("The component "+(component!=null ? component.getPartNumberF() : "")+" does not exist. <small>(E043)</small>");
	}

	public void setTopPartNumbers(HttpServletRequest request) {
		Data topComponent;
		selectedComponents = new ArrayList<>();
		ComponentIds component;

		for(int i=0; true; i++){
			String parameter = request.getParameter("n"+i);

			if(parameter==null){
				if(selectedComponents.size()==0)
					table = null;
				break;
			}

			topComponent = new ComponentDAO().getData(parameter.replaceAll("-", ""));

			if(topComponent==null || topComponent.getPartNumber().length()!=topComponent.PART_NUMB_SIZE){
				if(selectedComponents.size()==0)
					table = null;
				break;
			}

			int index = topComponents.indexOf(topComponent);
			if (index >= 0) {
				component = topComponents.get(index);
				component.setQuantity(request.getParameter("q"+ i));
				selectedComponents.add(component);
			}
		}
	}

	public String getPage() {
		String html;

		switch(activePage){
		case PAGE_PURCHASE:
		case PAGE_MOVEMENT:
			html = getTableStr();
			break;
		default:
			html = getCompaniesPage();
		}

		return html;
	}

	public String chooseComponentsWithBomHTML(){
		String html = "";

		if(topComponents!=null){
			int size = selectedComponents != null ? selectedComponents.size() : 0;

			for (int i = 0; i < topComponents.size(); i++) {
				html += getHtmlSelect( "n"+i,	"onchange=\"oneClick('submit')\"", i);
				html += "<input class=\"c2em\" type=\"text\" name=\"q"+i+"\" id=\"q"+i+"\" value=\""+getQuantity(i)+"\" onclick=\"this.select();\" />";
				if(i>=size) //at least do once
					break;
			}

			if (table != null)
				html += "<br />"
						+ "<input class=\"c3em\" type=\"text\" id=\"txt_add\" name=\"txt_add\" value=\""+quantityToAdd+(inPersent ? " %" : "")+"\" onkeypress=\"return oneKeyPress(event,'submit_add_qty')\" />"
						+ "<input type=\"submit\" name=\"submit_add_qty\" id=\"submit_add_qty\" value=\"Add\"  />"
						+ "<input type=\"submit\" name=\"submit_clean\" id=\"submit_clean\" value=\"Clean\" />" +

						"<input type=\"checkbox\" name=\"check_order\" id=\"check_order\" "+(isOrderByReference ? "checked=\"checked\"" : "")+" onclick=\"oneClick('submit')\" />" +
						"<label for=\"check_order\">Order by Reference</label>";
		}

		return html;
	}

	public void setQuantityToAdd(int quantityToAdd) {
		this.quantityToAdd = quantityToAdd;
		getBomTable();
	}

	private int getQuantity(int index) {
		return selectedComponents!=null && selectedComponents.size()>index ? selectedComponents.get(index).getQuantity() : 0;
	}

	private String getHtmlSelect(String name_id, String command, int selectNumber) {
		ComponentIds select = null;
		List<ComponentIds> componentsToShow = new ArrayList<>(topComponents);

		//remove used components
		if(selectedComponents!=null){
			List<ComponentIds> componentsToRemove = new ArrayList<>(selectedComponents);

			if(selectedComponents.size()>selectNumber){
				select = selectedComponents.get(selectNumber);
				componentsToRemove.remove(select);
			}

			componentsToShow.removeAll(componentsToRemove);
		}

		//get Part Numbers List
		List<Object> partNumbersToShow = new ArrayList<>();
		for(ComponentIds c:componentsToShow)
			if(!partNumbersToShow.contains(c))
				partNumbersToShow.add(c);

		return "<select id=\""+name_id+"\" name=\""+name_id+"\" "+command+" > "+getHtmlOption(partNumbersToShow, select)+" </select>";
	}

	public String getHtmlSelect(List<Object> names, String id, String toSelect, String command) {
		String html = "<select id=\""+id+"\" name=\""+id+"\" "+command+" > ";

		html += getHtmlOption(names, toSelect);
		html += "</select>";
		return html;
	}

	public String getHtmlOption(List<Object> objs, Object obj) {
		String html = "<option >Select</option>";
		for(Object o:objs)
			html += "<option "+(o.equals(obj) ? "selected=\"selected\"" : "")+" > "+o+" </option>";
		return html;
	}

	private String getCompaniesPage() {
		Table table = new CompanyDAO().getCompanysTable(getCompanyTypeID(), isEditCompany, showAll);
		if(table!=null)
			table.setTitle(new HTMLHeader(PAGE_MENU.getDescription(activePage)+"s", "cBlue", 3));
		return ""+table;
	}

	private int getCompanyTypeID() {
		byte id = -1;
		switch(activePage){
		case PAGE_VENDORS:
			id  = Company.TYPE_VENDOR;
			break;
		case PAGE_CM:
			id = Company.TYPE_CM;
		}
		return id;
	}

	public String getErrorMessage() {
		return error.getErrorMessage();
	}

	public static void setErrorMessage(String error) {
		Purchase.error.setErrorMessage(error);
	}

	public void preparePurchaseOrder(ComponentsQuantity componentsQuantity) {

		purchaseOrder = new PurchaseOrder(new ComponentDAO().getPOUnits(componentsQuantity));
		purchaseOrder.setEdit(true);
	}

	public void save(HttpServletRequest request, UserBean userBean) {

		if(company==null)
			company = new Company(-1, request.getParameter("company_name"),
					request.getParameter("seller_name"),
					request.getParameter("e_mail"),
					request.getParameter("telephon"),
					request.getParameter("fax"),
					request.getParameter("address"),
					request.getParameter("po").equals(PAGE_VENDOR_STR) ? Company.TYPE_VENDOR : Company.TYPE_CM,
					request.getParameter("chckbx_active")!=null);
		else
			company.set(request.getParameter("company_name"),
					request.getParameter("seller_name"),
					request.getParameter("e_mail"),
					request.getParameter("telephon"),
					request.getParameter("fax"),
					request.getParameter("address"),
					request.getParameter("po").equals(PAGE_VENDOR_STR) ? Company.TYPE_VENDOR : Company.TYPE_CM,
					request.getParameter("chckbx_active")!=null);

		if(company.isSet()){
			if(company.getId()==-1){
				if(new CompanyDAO().addCompany(company, userBean))
					cancelEdit();
			}else
				if(new CompanyDAO().update(company, userBean))
					cancelEdit();
		}else
			setErrorMessage("All fields must be filled. <small>(E044)</small>");
	}

	public void setCompany(String companyId) {
		companyId = companyId.replaceAll("[^\\d]", "");
		if(!companyId.isEmpty())
			company = new CompanyDAO().getCompany(Integer.parseInt(companyId));
	}

	public List<Company> getAllSellers(){ return allCompanys;		}
	public Company	getCompany()		{ return company;	}
	public Table	getTable()			{ return table;		}
	private String	getTableStr()	{ return table!=null ? table.toString() : "";	}

	public void setCompanys			(LinkedList<Company> Companys){allCompanys = Companys;					}
	public void	setEditCompany		(boolean isEditCompany	)	{ this.isEditCompany = isEditCompany;		}
	public void setAddCompany		(boolean isAddCompany	)	{ this.isAddCompany = isAddCompany;			}
	public void setShowAll			(boolean showAll		) 	{ this.showAll = showAll;	}

	public boolean isShowAll() 		{ return showAll;			}
	public boolean isErrorMessage()	{ return error.isError();	}
	public boolean isAddCompany()	{ return isAddCompany;		}
	public boolean isEdit()			{ return company !=null && !isAddCompany;	}

	public PurchaseOrder getPurchaseOrder() {
		return purchaseOrder;
	}

	public void setPurchaseOrder(PurchaseOrder purchaseOrder) {
		this.purchaseOrder = purchaseOrder;
	}

	public void cancelEdit() {
		company = null;
		isAddCompany = false;
	}

	public int getStatusToShow() {
		return statusToShow;
	}

	public void setStatusToShow(int statusToShow) {
		this.statusToShow = statusToShow;
	}

	public void setOrderBy(String orderBy) {
		if(this.orderBy==null)
			this.orderBy = new OrderBy(orderBy);
		else
			this.orderBy.setOrderBy(orderBy);
		isOrderByReference = false;
		getBomTable();
	}

	public boolean isOrderByReference() {
		return isOrderByReference;
	}

	public void setOrderByReference(boolean isOrderByReference) {
		this.isOrderByReference = isOrderByReference;
		if(isOrderByReference)
			orderBy = null;
		else
			orderBy = new OrderBy(BomDAO.PART_NUMBER);
	}

	public OrderBy getOrderBy() {
		return orderBy;
	}

	public int getKitId() {
		return kitId;
	}

	public int getCompanyId() {
		return companyId;
	}

	public static Purchase parsePurchase(String tmpStr) {
		return null;
	}

	public static Error getError() {
		return error;
	}
}
