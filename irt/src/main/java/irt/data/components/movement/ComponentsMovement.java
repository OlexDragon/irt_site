package irt.data.components.movement;

import irt.data.Error;
import irt.data.HTMLWork;
import irt.data.companies.Company;
import irt.data.companies.Place;
import irt.data.components.movement.interfaces.ComponentQuantity;
import irt.data.dao.CompanyDAO;
import irt.data.dao.ComponentDAO;
import irt.data.dao.ComponentDAO.Action;
import irt.data.dao.ComponentsMovementDAO;
import irt.data.dao.KitDAO;
import irt.data.dao.MenuDAO;
import irt.data.user.UserBean;
import irt.table.Row;
import irt.table.Table;
import irt.web.ComponentsMovementServlet;
import irt.web.PartNumberServlet;
import irt.web.SellersServlet;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

public class ComponentsMovement {
	public static final int CREATING = 0;
	public static final int CLOSED	 = 1;
	public final static int SENT 	 = 1; // KIT have been sent
	public static final int MOVED	 = 2; //moving details was created
	public static final int EDIT	 = 3;
	public static final int MOVING	 = 4; //moving details was not created yet

	private final Logger logger = LogManager.getLogger();

	private int 					id;
	@JsonProperty("wh")
	private UserBean 				who;
	@JsonProperty("fr")
	private Place 					from;
	@JsonProperty("fd")
	private Company 				fromDetail;
	@JsonProperty("pl")
	private Place 					to;
	@JsonProperty("cmp")
	private Company 				toDetail;
	@JsonProperty("dscr")
	private String					description;
	@JsonProperty("dt")
	private Date 					date;
	@JsonProperty("st")
	private int						status;

	@JsonProperty("dts")
	private ComponentsQuantity		details;
	@JsonProperty("pls")
	private List<Place>				places;

	@JsonProperty("er")
	private boolean 				error;
	@JsonProperty("cmsFr")
	private List<Company> 			companiesFrom;
	@JsonProperty("cmsT")
	private List<Company> 			companiesTo;
	@JsonProperty("sa")
	private boolean 				isShowAll;
	@JsonProperty("ho")
	private String 					historyOf;
	@JsonProperty("em")
	private Error			errorMessage = new Error();

	private final ComponentsMovementDAO componentsMovementDAO = new ComponentsMovementDAO();

	public ComponentsMovement() {
		details = new ComponentsQuantity();
		places = new MenuDAO().getPlaces();
	}

	public ComponentsMovement(ResultSet resultSet) throws SQLException {
		id = resultSet.getInt("id");

		ResultSetMetaData rsmd = resultSet.getMetaData();
		int columnsNumber = rsmd.getColumnCount();
		while(resultSet.next()){
			String[] str = new String[columnsNumber];
			for(int i=0; i<columnsNumber; )
				str[i] = resultSet.getString(++columnsNumber);
			errorMessage.setErrorMessage(Arrays.toString(str));
		}
	}

	public ComponentsMovement(HttpServletRequest request, String string) {

		ComponentsQuantity idAndValue = HTMLWork.getHtmlFields(request, "qty");
		if(error = idAndValue.isEmpty())
			errorMessage.setErrorMessage("Input error. <small>(E001)</small>");
	}

	public static Place getPlace(ComponentsMovement componentsMovement, String link) {
		Place p = null;

		if(link!=null){

			switch(link){
			case PartNumberServlet.HTTP_ADDRESS:
			case SellersServlet.HTTP_ADDRESS:
				p = componentsMovement.getPlace(Company.STOCK);//"Stock"
			}
		}

		return p;
	}

	public static Byte getPlaceId(String link) {
		Byte id = null;

		if(link!=null){

			switch(link){
			case PartNumberServlet.HTTP_ADDRESS:
			case SellersServlet.HTTP_ADDRESS:
				id = Company.STOCK;//"Stock"
			}
		}

		return id;
	}

	public List<Place> getPlaces() {
		return places;
	}

	public void setPlaces(List<Place> places) {
		this.places = places;
	}

	public Boolean add(ComponentToMove componentToMove) {

		Boolean added = null;

		if(componentToMove!=null){
			if(componentToMove.getStockQuantity()>0 && componentToMove.getQuantityToMove()>0 || from==null || from.getId()==Company.TYPE_VENDOR){
				if(added = details.add(componentToMove)){
					//errorMessage.setErrorMessage("The component "+componentToMove.getPartNumber()+" added to the moving list.", "cBlue");
				}else
					errorMessage.setErrorMessage("The component "+componentToMove.getPartNumber()+" already exist in the moving list. <small>(E002)</small>");
			}else
				errorMessage.setErrorMessage(componentToMove.getPartNumber()+" was not moved. The component quantity is 0. <small>(E003)</small>");
		}
		return added;
	}

	public boolean add(String pn, String httpAddress) {

		boolean isMoved = false;
		if (getFrom()==null || getFrom().equals(ComponentsMovement.getPlace(this, httpAddress))) {
			ComponentToMove cq;
			if (pn != null && (cq = new ComponentDAO().getComponentToMove(pn)) != null){
				isMoved = add(cq);
			}else
				errorMessage.setErrorMessage("Part Number "+pn+" do not exist. <small>(E004)</small>");
		}else
			errorMessage.setErrorMessage("You did not finish move from "+getFrom().getName()+". <small>(E005)</small>");

		return isMoved;
	}

	public boolean add(long componentId, String httpAddress) {

		boolean isMoved = false;
		if (getFrom()==null || getFrom().equals(ComponentsMovement.getPlace(this, httpAddress))) {
			ComponentToMove cq;
			if (componentId>0 && (cq = new ComponentDAO().getComponentToMove(componentId)) != null){
				isMoved = add(cq);
			}else
				errorMessage.setErrorMessage("Component ID "+componentId+" do not exist. <small>(E070)</small>");
		}else
			errorMessage.setErrorMessage("You did not finish move from "+getFrom().getName()+". <small>(E071)</small>");

		return isMoved;
	}

	public void add(Long[] componentsIds, String link) {
		for(long l:componentsIds)
			add(l, link);
	}

	@JsonIgnore
	public String getTable(){
		String tableStr = "";

		Table table;
		if (!details.isEmpty()) {
			table = new Table(new String[] { "Part Numb.", "Mfr P/N", "Description", "Location", "Qty" }, null);
			for (ComponentToMove ctm : details.getComponentsToMove()) {
				int componentId = ctm.getId();
				table.add(new Row(new String[]{	ctm.getPartNumber(),
											ctm.getMfrPN(),
											ctm.getDescription(),
											ctm.getLocation(),
											"<input class=\"c3em"+ctm.getColor()+"\" id=\"qty"+componentId+"\" name=\"qty"+componentId+"\" value=\""+ctm.getQuantityToMove()+"\" />"}));
			}
			tableStr = table.toString();
		}else{
			if (historyOf != null && !historyOf.isEmpty())
				table = new ComponentsMovementDAO().getOneComponentHistory(historyOf);
			else {
				if (isShowAll) {
					table = new ComponentsMovementDAO().getAllHistory();
				} else
					table = new ComponentsMovementDAO().getHistory();
			}
			if (table != null)
				tableStr = table.toString();
		}

		return tableStr;
	}

	@JsonIgnore
	public Place getPlace(String placeName) {
		Place place = null;

		if (places != null)
			for (Place p : places)
				if (p.getName().equals(placeName))
					place = p;

		return place;
	}

	@JsonIgnore
	public Place getPlace(int placeId) {
		Place place = null;

		if (places != null)
			for (Place p : places)
				if (p.getId() == placeId)
					place = p;

		return place;
	}

	@JsonIgnore
	public String getPlacesHTML(Place place, String name){
		String html = "<select id=\""+name+"\" name=\""+name+"\" onchange=\"oneClick('submit')\">"
						+"<option>Select</option>";

		String selected;
		if (places != null)
			for (Place p : places){
				selected = p.equals(place) ? "selected=\"selected\" " : "";
				html += "<option value=\""+p.getId()+"\" "+selected+">"
							+p.getName()
						+"</option>";
			}

		return html + "</select>";
	}

	@JsonIgnore
	public String getDetailsHTML(Place place, Company detail, String name){
		List<Company> companies = null;

		if(companiesFrom==null || companiesTo==null){
			switch (place.getId()) {
			case Company.TYPE_CM:
				companies = new CompanyDAO().getAllSuppliers(false);
				break;
			case Company.TYPE_VENDOR:
				companies = new CompanyDAO().getAllSellers(false);
				break;
			case Company.KIT:
				companies = new KitDAO().getAllOpenKITs();
				break;
			case Company.ASSEMBLED:
				companies = new ComponentDAO().getAssembledPCB();
			}

			if (place == from)
				companiesFrom = companies;
			else
				companiesTo = companies;
		}else
			if(place == from)
				companies = companiesFrom;
			else
				companies = companiesTo;

		String html = "<select id=\""+name+"\" name=\""+name+"\" onchange=\"oneClick('submit')\">"
						+"<option>Select</option>";

		if (companies != null){
			String selected;
			for (Company c : companies){
				selected = c.equals(detail) ? "selected=\"selected\" " : "";
				html += "<option value=\""+c.getId()+"\" "+selected+">"
							+c.getCompanyName()+
						"</option>";
			}
		}

		return html + "</select>";
	}

	@JsonIgnore
	public void setFrom(String placeIdStr) {

		if (from==null && placeIdStr != null && !(placeIdStr.replaceAll("[^\\d]", "")).isEmpty()){
			setFrom(Byte.parseByte(placeIdStr));
		}
	}

	@JsonIgnore
	public void setFrom(int placeId) {

			from = getPlace(placeId);
			companiesFrom = null;
	}

	@JsonIgnore
	public void setTo(String placeIdStr) {

		if (to==null && placeIdStr != null && !(placeIdStr.replaceAll("[^\\d]", "")).isEmpty()){
			byte parseByte = Byte.parseByte(placeIdStr);
			setTo(parseByte);
		}
	}

	public void setTo(int placeId) {
		to = getPlace(placeId);
		companiesTo = null;
	}

	@JsonIgnore
	public void setToDetail(String companyIdStr) {

		if(companiesTo!=null && companyIdStr!=null && !(companyIdStr = companyIdStr.replaceAll("[^\\d]", "")).isEmpty()){
			int companyId = Integer.parseInt(companyIdStr);

			for(Company c:companiesTo)
				if(c.getId()==companyId)
					toDetail = c;

			if(toDetail!=null)
				switch(to.getId()){
				case Company.ASSEMBLED:
					ComponentToMove ctm = new ComponentDAO().getComponentToMove(toDetail.getId());
					if(description.isEmpty())
						description = ctm.getDescription();
					ctm.setStockQuantity(10000);
					ctm.setQuantityToMove(1,false);
					add(ctm);
					break;
				case Company.KIT:
					
					String description = new KitDAO().getKitDescription(toDetail.getId());
					if(description!=null)
						this.description = description;
					setFrom(Company.STOCK); 
			}
		}
	}

	@JsonIgnore
	public void setFromDetail(String companyIdStr) {
		if(companyIdStr!=null && !(companyIdStr = companyIdStr.replaceAll("[^\\d]", "")).isEmpty()){
			int companyId = Integer.parseInt(companyIdStr);
			fromDetail = companiesFrom.get(companiesFrom.indexOf(new Company(companyId)));
		}
	}

	public boolean move(HttpServletRequest request) {
		error = false;

		if(isFromToChecked()){
			description = request.getParameter(ComponentsMovementServlet.TXT_DESCRIPTION);
//			if(setQuantityToMove(request) || from.getId()==Company.KIT)
			move(); 
		}else{
			errorMessage.setErrorMessage("All fields must be selected. <small>(E006)</small>");
			error = true;
		}

		return !error;
	}

	@JsonIgnore
	public boolean setQuantityToMove(HttpServletRequest request) {
		error = false;
		ComponentsQuantity htmlFields = HTMLWork.getHtmlFields(request,"qty");
		if(!(error=htmlFields.isEmpty()))
			setQuantityToMove(htmlFields);

		return !error;
	}

	public boolean kitHasComponents(){
		return new KitDAO().hasComponents(toDetail.getId());
	}

	private boolean move() { //userId who make movement

		switch(status){
		case CREATING:
			switch (to.getId()){
			case Company.TYPE_CM:
				logger.trace("\n\t{}", "Company.TYPE_CM");
				moveToCompany();
				break;
			case Company.TYPE_VENDOR:
				logger.trace("\n\t{}", "Company.TYPE_VENDOR");
				errorMessage.setErrorMessage("Coming soon");
				break;
			case Company.STOCK:
				logger.trace("\n\t{}", "Company.TYPE_CM");
				moveToStock();
//TODO			case Company.ASSEMBLED:
//				logger.trace("\n\t{}", "Company.ASSEMBLED");
//				switch(from.getId()){
//				case Company.STOCK:
//					logger.trace("\n\t\t{}", "Company.STOCK");
//					if(new ComponentsMovementDAO().moveFromStockToAssembled(this, who.getID())<=0)
//						errorMessage.setErrorMessage("Input Error. <small>(E007)</small>");
//					break;
//				case Company.TYPE_CM:
//					logger.trace("\n\t\t{}", "Company.TYPE_CM");
//					if(new ComponentsMovementDAO().moveFromSuplierToAssembled(this)<=0)
//						errorMessage.setErrorMessage("Input Error. <small>(E008)</small>");
//					break;
//				case Company.TYPE_VENDOR:
//					logger.trace("\n\t\t{}", "Company.TYPE_VENDOR");
//					if(new ComponentsMovementDAO().moveFromVendor(this, who.getID())<=0)
//						errorMessage.setErrorMessage("Input Error. <small>(E009)</small>");
//				}
//				break;
//
//			case Company.KIT:
//				logger.trace("\n\t{}", "Company.KIT");
//				setStatus(MOVING);
//				if(!new KitDAO().moveFromStock(this))
//					errorMessage.setErrorMessage("Input Error. <small>(E010)</small>");
//				break;
//
//			case Company.BULK:
//				logger.trace("\n\t{}", "Company.BULK");
//				setStatus(MOVING);
//				if(new ComponentsMovementDAO().moveToBaulk(this)<=0)
//					errorMessage.setErrorMessage("Input Error. <small>(E011)</small>");
//				break;
//
//			default:
//				logger.trace("\n\t{}", "default:");
//				setStatus(MOVING);
//				switch(from.getId()){
//				case Company.KIT:
//					logger.trace("\n\t\t{}", "Company.KIT");
//					if(new ComponentsMovementDAO().moveFromKIT(this)<=0)
//						errorMessage.setErrorMessage("Input Error. <small>(E012)</small>");
//					break;
//
//				case Company.TYPE_VENDOR:
//					logger.trace("\n\t\t{}", "Company.TYPE_VENDOR");
//					if ((new ComponentsMovementDAO().createComponentsMovement(this, ComponentDAO.QTY_ADD)) <= 0) {
//						errorMessage.setErrorMessage("Input Error. <small>(E013)</small>");
//						error = true;
//					}
//					break;
//
//				default:
//					logger.trace("\n\t\t{}", "default:");
//					int option;
//					if(to.getId()==Company.STOCK)
//						option = ComponentDAO.QTY_ADD;
//					else
//						option = ComponentDAO.QTY_SUBTACT;
//					if ((new ComponentsMovementDAO().createComponentsMovement(this, option)) <= 0) {
//						errorMessage.setErrorMessage("Input Error. <small>(E014)</small>");
//						error = true;
//					}
//				}
			}
		}

		return !error;
	}

	private void moveToStock() {
		switch(from.getId()){
		case Company.TYPE_CM:
			logger.trace("\n\t{}", "Company.TYPE_CM");
			componentsMovementDAO.moveComponents(this, Action.QTY_ADD);
			break;
		default:
			errorMessage.setErrorMessage("Coming soon.(cs2)");
		}
	}

	private void moveToCompany() {
		switch(from.getId()){
		case Company.STOCK:
			logger.trace("\n\t{}", "Company.STOCK");
			componentsMovementDAO.moveComponents(this, Action.QTY_SUBTACT);
			break;
		default:
			errorMessage.setErrorMessage("Coming soon.(cs1)");
		}
	}


	public void moveComponent(ComponentDAO componentDAO, ComponentsMovementDAO movementDAO, int movementId, ComponentToMove ctm) {

		if(componentDAO.updateQuantity(ctm)){
			if(!movementDAO.insertDetail(movementId,ctm))
				errorMessage.setErrorMessage("Input Error." +ctm.getPartNumber()+" <small>(E015)</small>");
		}else
			errorMessage.setErrorMessage("Input Error." +ctm.getPartNumber()+" <small>(E016)</small>");

		if(errorMessage.isError())
			error = true;
	}

	@JsonIgnore
	private void setQuantityToMove(ComponentsQuantity htmlFields) {

		List<ComponentQuantity> componentsQuantity = htmlFields.getComponentsQuantity();
	    ExecutorService executor = Executors.newFixedThreadPool(16);
		for(int i=0; i<componentsQuantity.size(); i++)
			executor.execute(new ComponentsMovementData(componentsQuantity.get(i), details, from, to, toDetail));

		executor.shutdown();
		while(!executor.isTerminated());
	}

//******************************************************************************************************
	public class ComponentsMovementData extends Thread {

		private ComponentQuantity componentQuantity;
		private volatile ComponentsQuantity details;
		private Place from;
		private Place to;
		private Company toDetail;

		public ComponentsMovementData(ComponentQuantity componentQuantity, ComponentsQuantity details, Place from, Place to, Company toDetail) {
			this.componentQuantity = componentQuantity;
			this.details = details;
			this.to = to;
			this.toDetail = toDetail;
			this.from = from;
		}

		@Override
		public void run() {
			 ComponentToMove ctm = details.getComponentToMove(componentQuantity.getId());
			if(ctm!=null) {
				int quantityToMove = componentQuantity.getQuantityToMove();
				if(quantityToMove==0)
					removeComponentToMove(ctm);
				else if(!ctm.setQuantityToMove(quantityToMove, from==null || from.getId()==Company.TYPE_VENDOR)){
					errorMessage.setErrorMessage("Input error. "+ctm.getPartNumber()+" <small>(E017)</small>");
				}
			}
		}

		private void removeComponentToMove(ComponentToMove ctm) {

			List<ComponentQuantity> csq = details.getComponentsQuantity();
			int index = csq.indexOf(ctm);
			if(index>=0){
				details.remove(index);
				if(to!=null && to.getId()==Company.KIT && toDetail!=null)
						new KitDAO().removeComponent(toDetail.getId(), ctm.getId());
			}
		}
	}
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	@JsonIgnore
	public boolean isFromToChecked() {
		return	from		!=	null	&&
				to			!=	null	&&
				!(from	==	to && fromDetail == toDetail)	 &&
				(fromDetail	!=	null	|| from.getId()	== Company.STOCK) &&	//stock
				(toDetail	!=	null	|| to.getId()	== Company.STOCK || to.getId()==Company.BULK) &&
				description	!=null		&&
				!description.isEmpty()	&&
				(!details.isEmpty() || from.getId()==Company.KIT);
	}

	public void clean() {
		resetFromTo();
		details		= new ComponentsQuantity();
		places		= new MenuDAO().getPlaces();
	}

	public int		getId()	 	{ return id;	}
	public int		getStatus()	{ return status;		}
	public UserBean	getWho() 	{ return who;	}
	public Place	getFrom()	{ return from;	}
	public Place	getTo()	 	{ return to;	}
	public Date		getDate()	{ return date;	}
	public Company	getFromDetail()	{ return fromDetail;	}
	public Company	getToDetail() 		{ return toDetail;		}
	public String	getDescription()	{ return description;	}
	public ComponentsQuantity getDetails() { return details;	}

	public void setId	(int id)		{ this.id = id;		}
	public void setWho	(UserBean who)	{ if(this.who==null) this.who = who;	}
	public void setFrom	(Place from)	{ this.from = from;	}
	public void setTo	(Place to)		{ this.to = to;		}
	public void setDate	(Date date)		{ this.date = date;	}
	public void setDescription	(String description	) { this.description = description;}
	public void setStatus		(int status			) { this.status = status;			}
	public void setFromCompany	(Company fromCompany) { this.fromDetail = fromCompany;	}
	public void setToCompany	(Company toCompany	) { this.toDetail = toCompany;		}
	public void setDetails(ComponentsQuantity details){ this.details = details;		}

	public void resetFromTo() {
		id			= -1;
		from		= null;
		to			= null;
		toDetail	= null;
		description	= null;
		date		= null;
	}

	public boolean showToDetails(){
		return to!=null
				&& to.getId()!=Company.STOCK
				&& to.getId()!=Company.BULK
				&& to.getId()>0 ;
	}

	public boolean showFromDetails(){
		return from!=null
				&& from.getId()!=Company.STOCK
				&& from.getId()!=Company.BULK
				&& from.getId()>0 ;
	}

	public boolean isShowAll() {
		return isShowAll;
	}

	public void setShowAll(boolean isShowAll) {
		this.isShowAll = isShowAll;
	}

	public void historyOf(String partNumberStr) {
		historyOf = partNumberStr;
	}

	public boolean contains(int componentId) {
		return details!=null ? details.contains(componentId) : false;
	}

	@JsonIgnore
	public void getComponentsKit() {
		details = new KitDAO().getComponentsQuantity(toDetail.getId());
	}

	@Override
	public boolean equals(Object obj) {
		return obj!=null ? obj.hashCode()==hashCode() : super.equals(obj);
	}

	@Override
	public int hashCode() {
		return id>0 ? id : super.hashCode();
	}

	public void addQuantity(int componentId, String qtyDescription, int oldQty, int qty, int userId, int from, int fromDetail, int to, int toDetail) {
		if(new ComponentDAO().setQuantity(componentId, qty, Action.QTY_ADD))
			 new ComponentsMovementDAO().insertMovementHistory(userId, from, fromDetail, to, toDetail, qtyDescription, componentId, oldQty, qty);
	}

	public irt.data.Error getErrorMessage() {
		return errorMessage;
	}

	@Override
	public String toString() {
		return "ComponentsMovement [id=" + id + ", who=" + who + ", from="
				+ from + ", fromDetail=" + fromDetail + ", to=" + to
				+ ", toDetail=" + toDetail + ", description=" + description
				+ ", date=" + date + ", status=" + status + ", details="
				+ details + ", places=" + places + ", error=" + error
				+ ", companiesFrom=" + companiesFrom + ", companiesTo="
				+ companiesTo + ", isShowAll=" + isShowAll + ", historyOf="
				+ historyOf + ", errorMessage=" + errorMessage + "]";
	}

	public boolean isError() {
		return error;
	}

	public void setError(boolean error) {
		this.error = error;
	}

	public void setFromDetail(Company fromDetail) {
		this.fromDetail = fromDetail;
	}

	public void setToDetail(Company toDetail) {
		this.toDetail = toDetail;
	}

	public void setErrorMessage(irt.data.Error errorMessage) {
		this.errorMessage = errorMessage;
	}
}