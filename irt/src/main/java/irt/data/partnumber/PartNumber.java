package irt.data.partnumber;

import irt.data.FirstDigit;
import irt.data.Search;
import irt.data.Error;
import irt.data.SecondAndThirdDigits;
import irt.data.companies.Company;
import irt.data.components.Component;
import irt.data.components.Data;
import irt.data.components.movement.ComponentsMovement;
import irt.data.dao.ComponentDAO;
import irt.data.dao.CostDAO;
import irt.data.dao.FirstDigitDAO;
import irt.data.dao.MenuDAO;
import irt.data.dao.SecondAndThirdDigitsDAO;
import irt.data.purchase.Cost;
import irt.data.purchase.Purchase;
import irt.data.user.UserBean;
import irt.product.ProductStructure;
import irt.table.OrderBy;
import irt.table.Table;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;

public class PartNumber {

	private static final Logger logger = (Logger) LogManager.getLogger();

	private char selectedFirstChar;
	private static List<FirstDigit> firstDigitList = new FirstDigitDAO().getAll();

	private String selectedSecondStr;
	private List<SecondAndThirdDigits> secondAndThirdDigitsList;

	private static List<User> users = new ArrayList<User>();
	private String selectedUser;

	private Purchase purchase = new Purchase();		//use in SellersServlet
	private ComponentsMovement componentsMovement;	//use in ComponentsMovementServlet

	private Component component;

	private Table table;
	private OrderBy orderBy;

	private static int browserId;
	private ProductStructure productStructure;
	private boolean hasUpdate;
	private Cost cost;

	private static Error error = new Error();

//	private Logger logger = Logger.getLogger(this.getClass());

	public PartNumber() { }

	public void setSecondAndThirdDigitsList() {
		this.secondAndThirdDigitsList = getGroup()!='\0' ? new SecondAndThirdDigitsDAO().getRequired(getGroup()) : null;
	}

	/**
	 * 
	 * @param partNumber ex. 00R-6488-8585-99
	 * @return ex. 00R6488858599
	 */
	public static String validation(String partNumberText) {
		String returnStr = "";

		if (partNumberText != null) {
			partNumberText = partNumberText.toUpperCase().trim();

			for (char ch : partNumberText.toCharArray())
				if ((ch >= '0' && ch <= '9')
						|| (ch >= 'A' && ch <= 'Z')
						|| ch=='?' 
						|| ch=='_'
						|| ch=='*'
						|| ch=='%'
					)
					returnStr += ch;
		}
		return returnStr;
	}

	public static Collection<?> validation(List<String> partNumbersList) {
		List<String> validPN = null;

		if(partNumbersList!=null){
			validPN = new ArrayList<>();
			for(String s:partNumbersList)
				validPN.add(validation(s));
		}
		
		return validPN;
	}

	public String getFirstId(String description) {
		String returnStr = "";
		
		if (description != null && !description.isEmpty())
			for (int i = 0; i < getFirstDigitList().size(); i++)
				if (description.equals(getFirstDigitList().get(i)
						.getDescription())) {
					returnStr += getFirstDigitList().get(i).getId();
				}

		return returnStr;
	}

	public String getSecondId(String description) {
		String returnStr = "";

		if (description!=null && getSecondAndThirdDigitsList() != null)
			for (int i = 0; i < getSecondAndThirdDigitsList().size(); i++)
				if (description.equals(getSecondAndThirdDigitsList().get(i)
						.getDescription())) {
					returnStr += getSecondAndThirdDigitsList().get(i).getId();
				}

		return returnStr;
	}

	public static Component parsePartNumber(String partNumberStr) {
		logger.entry(partNumberStr);

		partNumberStr = validation(partNumberStr);
		Component component;
		if((partNumberStr != null && !partNumberStr.isEmpty())){
			component = (Component) new ComponentDAO().getData(partNumberStr);
			logger.trace("{}", component);

			if(component==null){
				if(partNumberStr.replace("?", "").length() < 3){
					if((component = new PartNumberDetails(null).getComponent(""+partNumberStr.charAt(0)))==null)
						error.setErrorMessage("Part number is not correct. <small>(E030)</small>");
					else
						error.setErrorMessage("Part number is not correct. <small>(E001)</small>");
				}else{
					String classId = partNumberStr.substring(0, 3);
					component = new PartNumberDetails(null).getComponent(classId);
					if(component!=null){
						component.setPartNumber(partNumberStr);
						String partNumberF = component.getPartNumberF();
						if (component.getPartNumber().contains("?"))
							error.setErrorMessage("Part number '" +partNumberF+ "' is not correct. <small>(E031)</small>");
						else
							error.setErrorMessage(partNumberF+" - do not exist. <small>(E032)</small>");
					}else
						error.setErrorMessage("Part number is not correct. <small>(E033)</small>");
				}
			}
		}else{
			component = null;
		}

		return logger.exit(component);
	}

	public String getComponentValue(int argNumber) {
		String returnStr = null;
		
		if(component != null )
			returnStr = component.getValue(argNumber);
		
		return returnStr;
	}
	
	public void setComponentValue(HttpServletRequest req, boolean isAdmin) {
		boolean isSet = true;
		hasUpdate = false;
		if(component!=null){
			component.reset();
		
			int componentTitleSize = getComponentTitleSize();
			for (int i = 0; i < componentTitleSize; i++)
				isSet = setComponentValue(i,req.getParameter("arg" + i)) && isSet;

		}else
			isSet = false;

		if(!isSet && !error.isError())
			error.setErrorMessage("All fields must be filled. <small>(E034)</small>");
		else
			if(component==null)
				error.setErrorMessage("Coming soon");
			else {
				Data c = new ComponentDAO().getData(component.getPartNumber());
				if (c != null) {
					component.setId(c.getId());
					// Check if have to update 'Description', 'Mfr P/N'...
					if (c.getId() > 0) {
						int n = component.getTitleSize();
						for (int i = 0; i < n; i++) {
							String valueDB = c.getValue(i);
							String value = component.getValue(i);
							if (valueDB.isEmpty() && !value.isEmpty() || isAdmin && !valueDB.equals(value)) {
								hasUpdate = true;
								break;
							}
						}
						if(!hasUpdate)
							component = (Component) c;
						else
							component.setId(c.getId());
					}
					
				}
		}
	}

	public static User getUser(String userName){
		User user = null;
		
		int index = users.indexOf(new User(userName, null));
		if(index>=0)
			user = users.get(index);

		return user;
	}

	public static PartNumber getPartNumber(String selectedUser) {
		PartNumber partNumber;
	
		User user = getUser(selectedUser);

		if(user != null){
			user.resetTime();
			partNumber = user.getPartNumber();
		}else{
			partNumber = new PartNumber();
			users.add(new User(selectedUser, partNumber));
			partNumber.setSelectedUser(selectedUser);
			Collections.sort(users);
			if(users.size()>10)
				users.remove(0);
		}
		
		return partNumber;
	}
	
	@Override
	public String toString() {
		return "PartNumber [selectedFirstStr=" + selectedFirstChar
				+ ", selectedSecondStr=" + selectedSecondStr
				+ ", secondAndThirdDigitsList=" + secondAndThirdDigitsList
				+ ", selectedUser=" + selectedUser + "]";
	}

	public String table(){
		String tableStr = "";
		
		if(table!=null)
				tableStr += table;
		
		return tableStr;
	}

	public void search(HttpServletRequest request) {

		String tmpPartNumber;
		Search search = new Search();

		if (component != null) {
			setComponentValue(request, false);
			component.resetSequentialNunber();
			table = search.componentBy(component);
		} else{
			tmpPartNumber = request.getParameter("pnText");
			table = search.componentBy(tmpPartNumber, getOrderBy());
			parsePartNumber(tmpPartNumber);
		}
	}

	public	void search(Component component, OrderBy orderBy)	{ setOrderBy(orderBy);	search(component);		}
	private void search(Component component){ table = new Search().componentBy(component); }
	public void search(String string) {table = new ComponentDAO().getComponentsTable(string, orderBy);}

	public void componentRecet() {
		if(component!=null)
			component = new PartNumberDetails(null).getComponent(component.getClassId());
	}
	
	public boolean hasUpdate(){
		return hasUpdate;
	}
	
	public boolean hasCost(){
		return component!=null ? new CostDAO().hasCost(component.getId()) : false;
	}

	public boolean hasBom(){ String pn = component!=null ? component.getManufPartNumber() : null;
		return pn!=null  ? pn.startsWith("IRT BOM") : false;
	}
	public boolean hasLink(){ return component!=null ? component.getLink().isSet() : false;	}

	public static List<User>getUsers()		{ return users;		}
	public Component		getComponent()	{ return component;	}
	public OrderBy			getOrderBy()	{ return orderBy;	}
	public Purchase			getPurchase()	{ return purchase;	}
	public static int		getBrowserId()	{ return browserId;	}
	public String			getPartNumber()			{ return (component!=null)		? component.getPartNumberF()	: "";	}
	public String 			getClassId()			{ return (getComponent()!=null)	? getComponent().getClassId()	: "";	}
	public int				getComponentTitleSize()	{ return (component!=null)		? component.getTitleSize()		: 0;	}
	public String			getHTML()				{ return (component!=null)		? new PartNumberDetails(component).getHTML() : "";	}
	public String 			getSelectedUser()	{ return selectedUser;	}
	public List<FirstDigit>	getFirstDigitList()	{ return firstDigitList;}
	public String 			getErrorMessage()	{ return error.getErrorMessage();	}
	public ComponentsMovement			getComponentsMovement()			{ return componentsMovement;		}
	public List<SecondAndThirdDigits>	getSecondAndThirdDigitsList()	{ return secondAndThirdDigitsList;	}

	public void setOrderBy		(OrderBy orderBy)		{ this.orderBy = orderBy;			}
	public void setPurchase		(Purchase purchase)		{ this.purchase = purchase;			}
	public static void setBrowserId	(int browserId) 		{ PartNumber.browserId = browserId;		}
	private void setSelectedUser(String selectedUser)	{ this.selectedUser = selectedUser;	}
	public void setComponent	(Component component)	{ this.component = component;		}
	public void setComponentsMovement	(ComponentsMovement componentsMovement) { this.componentsMovement = componentsMovement;		}
	public boolean setComponentValue	(int index, String valueStr)			{ return component.setValue(index, valueStr);	}
	
	public static void setUsers				(List<User> users)					{ PartNumber.users = users;	}

	public boolean isSet()		{ return component!=null ? component.isSet() : false;			}
	public boolean isErrorMessage()	{ return error.isError();	}
	public boolean isExist()		{ return component!=null && component.getId()>0;}
	public boolean hasSchematicPart(){return component.getSchematicPart()!=null && !component.getSchematicPart().isEmpty();}
	public boolean isInPurchaseOrder(){return component!=null && purchase!=null && purchase.getPurchaseOrder()!=null ? purchase.getPurchaseOrder().hasComponent(component.getId()) : false; }
	public boolean isInMoving(){return componentsMovement!=null && component!=null ? componentsMovement.contains(component.getId()) : false;}

	public Table getTable() {
		return table;
	}

	public ProductStructure getProductStructure() {
		return productStructure!=null ? productStructure : (productStructure = new ProductStructure());
	}

	public boolean shawExel(){
		return new MenuDAO().shawExel(component.getClassId());
		
	}

	public Cost getCost() {
		return cost!=null ? cost : (cost=new Cost(0, null, null));
	}

	public void setCost(Cost cost) {
		this.cost = cost;
	}

	public Table getPrices() {
		cost = new CostDAO().getCost(component.getId());
		table = cost.getComponentTable();
		return table;
	}

	public User 	getUser()	{ return getUser(selectedUser); }
	public char 	getGroup()	{ String partNumber = getPartNumber(); return partNumber.isEmpty() ? '\0' : partNumber.charAt(0); }
	public String 	getType()	{ String partNumber = getPartNumber(); return partNumber.replace("?", "").length()>=3 ?  partNumber.substring(1, 3) : ""; }

	public void addQuantity(String qtyDescription, String qtyStr, UserBean userBean) {
		if(qtyStr!=null && userBean.isStock()){
			qtyStr = qtyStr.replaceAll("\\D", "");
			int qtyToMove = Integer.parseInt(qtyStr);
			if(qtyStr.isEmpty())
				error.setErrorMessage("Type the quantity. <small>(E038)</small>");
			else{
					if(qtyToMove>0){
						if(qtyDescription!=null && !qtyDescription.isEmpty()){
							new ComponentsMovement().addQuantity(component.getId(), qtyDescription, component.getQuantity(), qtyToMove, userBean.getID(), Company.TYPE_VENDOR, Company.DETAIL_NON, Company.STOCK, Company.DETAIL_NON);
							component.setQuantityStr(qtyStr);
						}else
							error.setErrorMessage("Fill Description(PO Number, Company ...). <small>(E039)</small>");
					}else
						error.setErrorMessage("Type the quantity. <small>(E040)</small>");
			}
		}else
			error.setErrorMessage("Type the quantity. <small>(E041)</small>");
	}

	public boolean isEdit(){
		return component!=null ? component.isEdit(): false;
	}

	public void setLocation(String newLocation, UserBean userBean) {
		if(component!=null && userBean.isStock()){
			new ComponentDAO().setComponentLocation(component.getId(), newLocation.isEmpty() ? null: newLocation.toUpperCase(), userBean.getID());
			component.setLocation(newLocation);
		}else
			error.setErrorMessage("The location did not updated.");
	}

	public void setHasUpdate(boolean hasUpdate) {
		this.hasUpdate = hasUpdate;
	}

	public static boolean isTop(String partNumberStr) {
		return partNumberStr!=null && !partNumberStr.isEmpty() ? partNumberStr.charAt(0)=='T' : false;
	}
}
