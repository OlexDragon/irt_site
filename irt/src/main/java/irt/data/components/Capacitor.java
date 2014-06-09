package irt.data.components;

import irt.data.Menu;
import irt.data.dao.ComponentDAO;
import irt.data.dao.MenuDAO;
import irt.data.dao.SecondAndThirdDigitsDAO;
import irt.work.InputTitles;
import irt.work.TextWork;

import java.text.DecimalFormat;

public class Capacitor extends Component {

	private static final int CAPACITOR = TextWork.CAPACITOR;

	public final static int VALUE 	= 0;
	public final int TYPE 			= 1;
	public final int PACKAGE 		= 2;
	public final static int VOLTAGE = 3;
	public final int SIZE 			= 4;
	public final int DESCRIPTION	= 5;
	public final int MANUFACTURE 	= 6;
	public final int MAN_PART_NUM 	= 7;
	public final int QUANTITY 		= 8;
	public final int LOCATION 		= 9;
	public final int LINK 			= 10;
	public final int PART_NUMBER 	=11;
	public final int NUMBER_OF_FIELDS	= 12;

	@Override
	public int getFieldsNumber() {
		return NUMBER_OF_FIELDS;
	}

	private boolean isOther;

	private static Menu titlesMenu;
	private static Menu mountingMenu;
	private static Menu sizeMenu;
	private static Menu typeMenu;
	
//	private Logger logger = Logger.getLogger(this.getClass());

	@Override
	public void setClassId(){
		setClassId(Component.CLASS_ID_NAME.get(CAPACITOR));
	}

	@Override
	public void setTitles() {
		setTitles(new InputTitles( titlesMenu.getKeys(),titlesMenu.getDescriptions()));
	}

	@Override
	public void setMenu() {
		MenuDAO menuDAO = new MenuDAO();
		if(titlesMenu==null)
			titlesMenu = menuDAO.getMenu("cap_titles", "sequence");
		if(sizeMenu==null)
			sizeMenu = menuDAO.getMenu("size","description");
		if(typeMenu==null)
			typeMenu = menuDAO.getMenu("cap_type","description");
		if(mountingMenu==null)
			mountingMenu = menuDAO.getMenu("cap_mounting","description");
	}

	@Override
	public String getSelectOptionHTML(int index) {

		String[] tmp = null;
		String[] toShow = null;
		String valueStr = "";

		switch (index) {
		case MANUFACTURE:
			return getManufactureOptionHTML();

		case TYPE:
			toShow = typeMenu.getDescriptions();
			tmp = typeMenu.getKeys();
			valueStr = getType();
			break;

		case PACKAGE:
			toShow = mountingMenu.getDescriptions();
			tmp = mountingMenu.getKeys();
			valueStr = getPackage();
			break;

		case SIZE:
			String size = getSize();
			isOther = !size.isEmpty() && !sizeMenu.containsKey(size);
			toShow = sizeMenu.getDescriptions();
			tmp = sizeMenu.getKeys();
			valueStr = isOther ? "OT" : size;
		}

		return getOptionHTML(tmp, toShow, valueStr);
	}

	@Override
	public String getValue(int index){
		String returnStr = "";
		
		switch(index){
		case VALUE:
			returnStr = getValue().isEmpty() ? "" : new  Value(getValue(), TextWork.CAPACITOR).toValueString();
			break;
		case TYPE:
			returnStr = getType();
			break;
		case PACKAGE:
			returnStr = getPackage();
			break;
		case VOLTAGE:
			returnStr = getVoltage().isEmpty() ? "" : new Value(getVoltage(), TextWork.VOLTAGE).toValueString();
			break;
		case SIZE:
			returnStr = getSize();
			break;
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
			returnStr = (getLink()!=null)
							? getLink().getHTML()
									:"";
		}
		
		return returnStr;
	}

	@Override
	public boolean setValue(int index, String valueStr){
		boolean isSet = true;

		switch(index){
		case VALUE:
			isSet = setValue(valueStr);
			break;
		case TYPE:
			isSet = setType(valueStr);
			break;
		case PACKAGE:
			isSet = setPackage(valueStr);
			break;
		case VOLTAGE:
			isSet = setVoltage(valueStr);
			break;
		case SIZE:
			isSet = setSize(valueStr);
			break;
		case PART_NUMBER:
			if(valueStr!=null
				&& valueStr.length()==PART_NUMB_SIZE){
				isSet = true;
				setPartNumber(valueStr);
			}
			break;
		case MAN_PART_NUM:
			super.setValue(super.MAN_PART_NUM, valueStr);
			break;
		case MANUFACTURE:
			super.setValue(super.MANUFACTURE, valueStr);
			break;
		case DESCRIPTION:
			super.setValue(super.DESCRIPTION, valueStr);
			break;
		case QUANTITY:
			isSet = super.setValue(super.QUANTITY, valueStr);
			break;
		case LOCATION:
			isSet = super.setValue(super.LOCATION, valueStr);
			break;
		case LINK:
			isSet = super.setValue(super.LINK, valueStr);
			break;
		default:
			isSet = false;	
		}
		
		return isSet;
	}

	@Override
	public String getValue() {
		return getValue(3,7);
	}

	private String getValueQ() {
		return (getValue().length()!=0)
							? getValue()
									: "????";
	}
	
	private boolean setValue(String valueStr) {
		boolean isSetted = false;

		if(valueStr!=null && !valueStr.isEmpty()){
			Value value = new Value(valueStr, TextWork.CAPACITOR);
			valueStr = value.toString();
			setDbValue(value.toValueString());
			isSetted = true;
		}else
			valueStr = "????";
			
		setPartNumber(getClassId()+valueStr+getTypeQ()+getPackageQ()+getVoltageQ()+getSizeQ());
		return isSetted;
	}

	private String getSize() {
		String returnStr = "";
		
		if (getPartNumber().length()==PART_NUMB_SIZE)
							returnStr = getPartNumber().substring(12);
		
		return (!returnStr.contains("?"))
				? returnStr
						: "";
	}

	private String getSizeQ() {
		return (getSize().length()!=0)
							? getSize()
									: "??";
	}

	private boolean setSize(String valueStr) {
		boolean isSetted = false;
		
		if(valueStr!=null && valueStr.length()==2){
			isSetted = true;
			if(valueStr.equals("OT")){
				if(isOther)
					valueStr = getSize();
				else{
					valueStr = new ComponentDAO().getNewCapacitorID(getPartNumber());
					isOther = true;
				}
			}else
				isOther = false;
		}else
			valueStr = "??";
		
		setPartNumber(getClassId()+getValueQ()+getTypeQ()+getPackageQ()+getVoltageQ()+valueStr);

		return isSetted;
	}

	private String getVoltage() {
		return getValue(9,12);
	}

	private String getVoltageQ() {
		return (getVoltage().length()!=0)
							? getVoltage()
									: "???";
	}

	private boolean setVoltage(String valueStr) {
		boolean isSetted = false;
		Value value = new Value(valueStr, TextWork.VOLTAGE);
		if (value.getIntValue()>0) {
			valueStr = value.toString();
			isSetted = true;
		}else
			valueStr = "???" ;
		
		setPartNumber(getClassId() + getValueQ() + getTypeQ() + getPackageQ()
				+ valueStr + getSizeQ());

		return isSetted;
	}

	private String getPackage() {
		return getValue(8,9);
	}

	private String getPackageQ() {
		return (getPackage().length()!=0)
							? getPackage()
									: "?";
	}

	private boolean setPackage(String valueStr) {
		boolean isSetted = false;
		
		if(valueStr!=null && !valueStr.isEmpty() && valueStr.length()==1){
			valueStr = valueStr.substring(0,1);
			isSetted = true;
		}else
			valueStr =  "?";

		setPartNumber(getClassId()+getValueQ()+getTypeQ()+valueStr+getVoltageQ()+getSizeQ());

		return isSetted;
	}

	private String getType() {
		return getValue(7,8);
	}

	private String getTypeQ() {
		return (getType().length()!=0)
							? getType()
									: "?";
	}

	private boolean setType(String valueStr) {
		boolean isSetted = false;
		
		if(valueStr!=null && valueStr.length()==1){
			valueStr = valueStr.substring(0,1);
			isSetted = true;
		}else
			valueStr = "?";

		setPartNumber(getClassId()+getValueQ()+valueStr+getPackageQ()+getVoltageQ()+getSizeQ());

		return isSetted;
	}

	@Override
	protected String getValue(String partNumber) {
		logger.entry(partNumber);

		String prefix = null;
		String[] beforeEafter;
		double output = 0;

		if (partNumber != null && partNumber.length() > 7) {
			partNumber = partNumber.substring(3, 7);
			if (!partNumber.contains("?") && partNumber.contains("E")) {
				beforeEafter = partNumber.split("E");
				beforeEafter[0] = beforeEafter[0] + '0';
				if (!beforeEafter[1].equals("A"))
					beforeEafter[0] = String.format(
							"%-" + (Integer.parseInt(beforeEafter[1]) + 4)
									+ "s", beforeEafter[0])
							.replaceAll(" ", "0");
				long value = Long.parseLong(beforeEafter[0]);
				if (value >= 1000000000) {
					prefix = "uF";
					output = value / 1000000000.0;
				} else if (value >= 1000000) {
					output = value / 1000000.0;
					prefix = "nF";
				} else {
					output = value / 1000.0;
					prefix = "pF";
				}
			}
		}
		return new DecimalFormat("#.##").format(output)+prefix;
	}

	@Override
	protected String getPartType(String partNumber) {
		String str = null;
		if(partNumber!=null && partNumber.length()>12){
			str = new SecondAndThirdDigitsDAO().getClassDescription(CAPACITOR);
			str += "\\\\"+new MenuDAO().getDescription(TextWork.CAP_TYPE,partNumber.substring(7,8));
			str += "\\\\"+getDbValue().replaceAll("[\\d. ]", "").replace("n", "N");
		}
		return str;
	}

	@Override
	protected String getDbVoltage(String partNumber) {
		logger.entry(partNumber);
		String str = null;
		if(partNumber!=null && partNumber.length()>12){
			str = partNumber.substring(9,12).replaceAll("[^\\d]", "");
			if(!str.isEmpty())
				str = str.isEmpty() ? null : Integer.parseInt(str)+"V";
			else
				str = null;
		}
		return str;
	}

	@Override
	public void resetAll() {
		isOther = false;
		super.resetAll();
	}
}