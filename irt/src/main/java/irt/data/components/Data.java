package irt.data.components;

import irt.data.Error;
import irt.data.Link;
import irt.data.Menu;
import irt.data.dao.ComponentDAO;
import irt.data.dao.LinkDAO;
import irt.data.dao.ManufactureDAO;
import irt.data.dao.MenuDAO;
import irt.data.dao.SecondAndThirdDigitsDAO;
import irt.data.manufacture.Manufacture;
import irt.data.partnumber.PartNumber;
import irt.data.partnumber.PartNumberDetails;
import irt.table.OrderBy;
import irt.table.Table;
import irt.work.InputTitles;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;


public abstract class Data {

    protected final Logger logger = (Logger) LogManager.getLogger(getClass().getName());
    protected static final Logger loggerComponent = (Logger) LogManager.getLogger("static.Component.logger");

	public static final Map<Integer, String> CLASS_ID_NAME = new SecondAndThirdDigitsDAO().getMapIdClass();
	public static final Map<String, Integer> CLASS_NAME_ID = new SecondAndThirdDigitsDAO().getMapClassId();

    public final int PART_NUMB_SIZE;

	public final static int DESCRIPTION		= 0;
	public final static int MANUFACTURE 	= 1;
	public final static int MAN_PART_NUM 	= 2;
	public final static int QUANTITY 		= 3;
	public final static int LOCATION 		= 4;
	public final static int LINK 			= 5;
	public final static int PART_NUMBER 	= 6;
	public final static int NUMBER_OF_FIELDS= 7;

	public int getFieldsNumber() {
		return NUMBER_OF_FIELDS;
	}

	protected final String DATABASE_NAME_FOR_TITLES;
	protected static final MenuDAO MENU_DAO = new MenuDAO();
	protected Menu titlesMenu;
	protected List<Menu> menusForHtmlSelects = new ArrayList<>();

	private int id = -1;

	private String partNumber;
	private String manufPartNumber;
	private String description;
	private String dbValue;
	private String dbVoltage;
	private String manufId;
	private String quantityStr;
	private String location;
	private Link link;
	private String footprint;
	private String schematicLetter;
	private String partType;
	private String schematicPart;
	private String classId;

	private Error error = new Error();

	private Data oldData;

	private InputTitles titles;

	private OrderBy orderBy;

	public Data(){
		logger.info("constructor: public {}()", getClass().getSimpleName());
		DATABASE_NAME_FOR_TITLES = getDatabaseNameForTitles();
		setClassId();
		PART_NUMB_SIZE = getPartNumberSize();
		setPartNumber(getClassId());
		setMenu();
		setTitles();
	}

	protected String getDatabaseNameForTitles() {
		return null;
	}

	public abstract 	void 	setClassId();
	public abstract 	int 	getPartNumberSize();
	public abstract 	String 	getDataId();
	public abstract 	String 	getPartNumberF();
	public abstract 	String 	getValue();
	protected abstract 	String 	getPartType(String partNumber);

	public void setMenu(){
		if(DATABASE_NAME_FOR_TITLES!=null){
			titlesMenu = MENU_DAO.getMenu(DATABASE_NAME_FOR_TITLES, irt.data.dao.MenuDAO.OrderBy.SEQUENCE);

			Menu menu;
			for(int i=0; i<10; i++){

				menu = MENU_DAO.getMenu(DATABASE_NAME_FOR_TITLES+i, irt.data.dao.MenuDAO.OrderBy.SEQUENCE);

				if(menu!=null)
					menusForHtmlSelects.add(menu);
				else
					break;
			}
		}
	}

	public void setTitles() {
		if(titlesMenu!=null)
			setTitles(new InputTitles( titlesMenu.getKeys(),titlesMenu.getDescriptions()));
	}

	/**
	 * Set Values by part number from database
	 */
	public boolean setValues(String partNumberStr) {
		boolean isSet = false;
		if(partNumberStr!=null && partNumberStr.length()==getPartNumberSize() && getClassId().equals(partNumberStr.substring(0, 3))){
			Data data = new ComponentDAO().getData(partNumberStr);
			if(data!=null){
				setValues(data);
				isSet = true;
			}else
				setPartNumber(String.format("%"+getPartNumberSize()+"s", partNumberStr).replace(' ', '?'));
		}
		return isSet;
	}

	public void setValues(Data data){
		if (data != null) {
			setId(data.getId());
			setPartNumber(data.getPartNumber());
			setManufPartNumber(data.getManufPartNumber());
			setManufId(data.getManufId());
			setDescription(data.getDescription());
			setQuantityStr(data.getQuantityStr());
			setLink(data.getLink());
			setLocation(data.getLocation());
			setFootprint(data.getFootprint());
			setDbValue(data.getDbValue());
			setDbVoltage(data.getDbVoltage());
			setSchematicPart(data.getSchematicPart());
			setSchematicLetter(data.getSchematicLetter());

		}
	}

	public String getValue(int index){
		logger.entry(index);
		String returnStr = "";

		switch(index){
		case PART_NUMBER:
			returnStr = getPartNumber();
			break;
		case MAN_PART_NUM:
			returnStr = getManufPartNumber();
			break;
		case MANUFACTURE:
			returnStr = getManufId();
			break;
		case DESCRIPTION:
			returnStr = getDescription();
			break;
		case QUANTITY:
			returnStr = getQuantityStr();
			break;
		case LOCATION:
			returnStr = getLocation();
			break;
		case LINK:
			Link link = getLink();
			returnStr = (link!=null)
							? link.getHTML()
									:"";
		}
		
		return logger.exit(returnStr);
	}

	public boolean setValue(int index, String valueStr){
		logger.entry(index, valueStr);
		boolean hasSet = valueStr!=null && !valueStr.trim().isEmpty();

		if(hasSet)
		switch(index){
		case MAN_PART_NUM:
			setManufPartNumber(valueStr);
			break;
		case MANUFACTURE:
			setManufId(valueStr);
			break;
		case DESCRIPTION:
			setDescription(valueStr);
			break;
		case QUANTITY:
			setQuantityStr(valueStr);
			break;
		case LOCATION:
			setLocation(valueStr);
			break;
		case LINK:
			setLink((String)null);
			break;
		default:
			hasSet = false;
		}
		
		return logger.exit(hasSet);
	}

	public void setValue(ResultSet resultSet) throws SQLException, CloneNotSupportedException{

			setId(resultSet.getInt("id"));
			setManufPartNumber(resultSet.getString("manuf_part_number"));
			setManufId(resultSet.getString("manuf_id"));
			setDescription(resultSet.getString("description"));
			setQuantityStr(resultSet.getString("qty"));
			setLocation(resultSet.getString("location"));
			setLink(resultSet.getString("link"));
			setSchematicPart(resultSet.getString("schematic_part"));
			setSchematicLetter(resultSet.getString("schematic_letter"));
			setPartType(resultSet.getString("part_type"));
			setDbValue(resultSet.getString("value"));
			setDbVoltage(resultSet.getString("voltage"));
			setFootprint(resultSet.getString("footprint"));
			setPartNumber(resultSet.getString("part_number"));

			oldData = getCopy();
	}

	protected static Link getLink(String linkIdStr) {

		int linkId = (linkIdStr!=null && !linkIdStr.isEmpty())
								? Integer.parseInt(linkIdStr)
										: -1;
		return (linkId>=0) 
				? new LinkDAO().getLink(linkId)
						: null;
	}

	public String getOptionHTML(String[] values,	String[] toShow, String selectedValue) {
		String returnStr = "";
		if(selectedValue==null)
			selectedValue = "";
		
		if (values != null && toShow!=null)
			for (int i = 0; i < values.length; i++) {
				returnStr += "<option value=\""+values[i]+"\""
						+ ((selectedValue.equals(values[i])) ? " selected=\"selected\"" : "")
						+ "> " + toShow[i] + " </option>\n";
			}
		
		return returnStr;
	}

	public String getManufactureOptionHTML() {
		String valueStr = "";
		Manufacture[] all = new ManufactureDAO().getAll(null);//getOrderBy());

		int size = (all!=null) ? all.length : 0;
		String[] tmp = new String[size];
		String[] toShow = new String[size];

		for(int i=0; i<size; i++){
			if(((tmp[i] = all[i].getId()) == null)
				|| (toShow[i] = all[i].getName().replace("&","&amp;" ))==null)
				tmp[i] = toShow[i] = "";
			valueStr = getManufId();
		}
		
		return getOptionHTML(tmp, toShow, valueStr);
	}

	public void resetAll() {
		reset();
		resetPartNumber();
	}

	public void reset() {
		id = -1;
		manufPartNumber = null;
		manufId 	= null;
		description = null;
		footprint 	= null;
		link 		= null;
		location 	= null;
		quantityStr = null;
		dbValue		= null;
		dbVoltage	= null;
		partType	= null;
		schematicPart= null;
	}

	public String getQuantityStr() { return (quantityStr!=null) ? quantityStr : "";	}

	public int getQuantity() {
		return quantityStr!=null ? Integer.parseInt(quantityStr) : -1;
	}

	public void setQuantityStr(String quantityStr) {
		this.quantityStr = quantityStr!=null && !(quantityStr = quantityStr.replaceAll("\\D", "")).isEmpty() ? quantityStr :null;
	}

	public void setTitles(InputTitles titles) {
		this.titles = titles;
	}

	public boolean setManufPartNumber(String manufPartNumber) {
		this.manufPartNumber = manufPartNumber!=null ? manufPartNumber.trim().toUpperCase() : null;
		if(dbValue==null)
			dbValue = this.manufPartNumber;
		return manufPartNumber!=null && !manufPartNumber.isEmpty();
	}

	public void setManufId(String manufId) {
		this.manufId = manufId;
	}
	
	public String getManufId() {
			return manufId!=null ? manufId : "";
	}
	
	public Link getLink() {
		return (link!=null)
				? link
						: new Link();
	}

	public void setId() {
		if(partNumber!=null && partNumber.length()==PART_NUMB_SIZE && !partNumber.contains("?"))
			id = new ComponentDAO().getId(partNumber);
	}

	public String getPartType() 		{
		if(partType==null || partType.isEmpty())
			partType = getPartType(getPartNumber());
		return partType;
	}

	public void resetPartNumber() { setPartNumber(getClassId());	}

	public String getLocation() { return (location!=null) ? location : "";	}
	public int getTitleSize() 	{ return (titles!=null && titles.getInputTitles()!=null) ? titles.getInputTitles().length : 0;	}
	public int size() { return NUMBER_OF_FIELDS;	}
	public int getId(){ return id;					}
	public String getDescription() 		{ return description!=null ? description : "";		}
	public String getTitle(int index) 	{ return titles.getInputTitle(index).getName();			}
	public String getInputType(int index) { return titles.getInputTitle(index).getInputType();	}
	public String getFootprint()		{ return footprint!=null ? footprint : "";	}
	public InputTitles getTitles() 		{ return titles;			}
	public OrderBy getOrderBy() 		{ return orderBy;			}
	public String getSchematicLetter() 	{ return schematicLetter;	}
	public String getDbValue() 			{ return dbValue;			}
	public String getSchematicPart()	{ return schematicPart;		}
	public String getClassId()			{ return logger.exit(classId);	}
	public String getManufPartNumber() 	{ return manufPartNumber;	}
	public String getDbVoltage()		{ return dbVoltage;			}
	public String getPartNumber()		{ return partNumber;	}
	public void setPartNumber(String partNumberStr) {
		int partNumberSize = getPartNumberSize();
		if(partNumberSize>0)
			partNumber = String.format("%-"+partNumberSize+"s", partNumberStr!=null ? partNumberStr.trim().toUpperCase() : "").replace(' ', '?');
		else
			partNumber = "";
	}
	public void setDescription(String description) { this.description = description!=null && !(description =description.trim()).isEmpty() ? description.toUpperCase() : null;	}
	public void setOrderBy		(String orderBy) 	{ if(this.orderBy==null) this.orderBy = new OrderBy(orderBy); else this.orderBy.setOrderBy(orderBy);						}
	public void setOrderBy		(OrderBy orderBy) 	{ this.orderBy = orderBy; }
	public void setId			(int id) 			{ this.id = id;									}
	public void setFootprint	(String footprint) 	{ this.footprint = footprint;					}
	public void setDbValue		(String value) 		{ this.dbValue = value;							}
	public void setPartType		(String partType) 	{	this.partType = partType;					}
	public void setSchematicPart(String schematicPart) {	this.schematicPart = schematicPart;		}
	public void setSchematicLetter(String schematicLetter) { this.schematicLetter = schematicLetter;}
	public void setClassId		(String classId) 	{ logger.entry(this.classId = classId);}
	public void setLink			(Link link) 		{ this.link = link;	}
	public void setLink			(String linkId) 	{
		if(linkId!=null && !linkId.isEmpty())
			link = new Link(id, null);
		else
			link = null;
	}
	public void setLocation		(String location) 	{ this.location = location;						}
	public void setDbVoltage		(String voltage){ this.dbVoltage = voltage;						}

	@Override
	public boolean equals(Object obj) {
		return obj!=null ? obj.hashCode()==id : false;
	}

	@Override
	public int hashCode() {
		return id>0 ? id : super.hashCode();
	}

	public boolean isEdit() {
		boolean isEdit = false;

		if(oldData!=null && id>0){
			isEdit = !equalsValues(oldData);
		}

		return isEdit;
	}

	public boolean equalsValues(Data oldData) {
		boolean equals = false;

		if(oldData!=null && this.getClassId().equals(oldData.getClassId()))
			for(int i=0; i< getFieldsNumber(); i++){
				String valueStr = getValue(i);
				String oldValueStr = oldData.getValue(i);
				if(!(equals = valueStr==null && oldValueStr==null || (valueStr!=null && oldValueStr!=null && valueStr.equalsIgnoreCase(oldValueStr))))
						break;
			}

		return equals;
	}

	public String getFieldName(int fieldIndex) {
		String name = null;
		switch(fieldIndex){
		case DESCRIPTION:
			name = "`Description`";
			break;
		case MANUFACTURE:
			name = "`manuf_id`";
			break;
		case MAN_PART_NUM:
			name = "`manuf_part_number`";
		}
		return name;
	}

	public static Data parseData(String dataStr) throws CloneNotSupportedException {
		loggerComponent.entry(dataStr);
		Data data = null;

		int startIndex = dataStr.indexOf("[")+1;
		int lastIndex  = dataStr.lastIndexOf("]");
		if(startIndex>0 && lastIndex>0){
			HashMap<String, String> map = new HashMap<>();
			valuesToMap(dataStr.substring(startIndex, lastIndex), map);

			data = PartNumber.parsePartNumber(map.get("partNumber"));
			if(data!=null){
				map.remove("partNumber");
				Set<String> keySet = map.keySet();
				for(String s:keySet)
					switch(s){
					case "id":
						data.setId(Integer.parseInt(map.get(s)));
						break;
					case "manufPartNumber":
						data.setManufPartNumber(map.get(s));
						break;
					case "description":
						data.setDescription(map.get(s));
						break;
					case "bdValue":
						data.setDbValue(map.get(s));
						break;
					case "dbVoltage":
						data.setDbVoltage(map.get(s));
						break;
					case "manufId":
						data.setManufId(map.get(s));
						break;
					case "quantityStr":
						data.setQuantityStr(map.get(s));
						break;
					case "location":
						data.setLocation(map.get(s));
						break;
					case "link":
						data.setLink(map.get(s));
						break;
					case "footprint":
						data.setFootprint(map.get(s));
						break;
					case "schematicLetter":
						data.setSchematicLetter(map.get(s));
						break;
					case "schematicPart":
						data.setSchematicPart(map.get(s));
						break;
					case "orderBy":
						data.setOrderBy(OrderBy.parseOrderBy(map.get(s)));
					}
				data.setOldData();
			}
		}


		return data;
	}

	private void setOldData() throws CloneNotSupportedException {
		oldData = getCopy();
	}

	private static void valuesToMap(String dataStr, HashMap<String, String> map) {
		loggerComponent.entry(dataStr);
		String[] split = dataStr.split(", ");
		for(String s:split){
			String[] values = s.split("=");
			if(values.length>1)
				map.put(values[0], values[1]);
		}
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		Data data =  new PartNumberDetails(null).getComponent(getClassId());
		data.setValues(this);
		return data;
	}

	public Data getCopy() throws CloneNotSupportedException{
		return (Data) clone();
		
	}

	@Override
	public String toString() {
		return getClass().getSimpleName()+
				" [partNumber=" + partNumber+
				(id>0 					? ", id=" + id 							: "")+
				(manufPartNumber!=null	? ", manufPartNumber=" + manufPartNumber: "") +
				(description!=null 		? ", description=" + description 		: "") +
				(dbValue!=null 			? ", bdValue=" + dbValue 				: "") +
				(dbVoltage!=null 		? ", dbVoltage=" + dbVoltage 			: "") +
				(manufId!=null 			? ", manufId="+ manufId 				: "") +
				(quantityStr!=null 		? ", quantityStr=" + quantityStr 		: "") +
				(location!=null 		? ", location=" + location 				: "") +
				(link!=null 			? ", link=" + link 						: "") +
				(footprint!=null 		? ", footprint="+ footprint 			: "") +
				(schematicLetter!=null	?", schematicLetter=" + schematicLetter : "") +
				(schematicPart!=null	? ", schematicPart=" + schematicPart 	: "") +
				(orderBy!=null 			? ", orderBy=" + orderBy 				: "") +
				"]";
	}

	public boolean isExist() {
		return getId()>0;
	}

	public String getTable() {
		String tblStr = "";
		if (isExist()) {
			Table table = new ComponentDAO().getComponentTable(getId());

			if(table!=null){
				table.setRowCount(false);
				tblStr +=table;
			}
		} else {
			if(!error.isError())
					setError(getPartNumberF() + " - do not exist. <small>(E035)</small>");
			}
		return tblStr;
	}

	public Error getError() {
		return error;
	}

	public void setError(String errorMessage) {
		error.setErrorMessage(errorMessage);
	}

	public void setError(String errorMessage, String htmlClassName) {
		error.setErrorMessage(errorMessage, htmlClassName);
	}
}