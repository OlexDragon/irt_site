package irt.data.components;

import irt.data.Menu;
import irt.data.dao.ComponentDAO;
import irt.data.dao.MenuDAO;
import irt.data.dao.SecondAndThirdDigitsDAO;
import irt.work.InputTitles;
import irt.work.TextWork;

public class IC extends Component {

	private static final int IC_RF = TextWork.IC_RF;

	public final int MAN_PART_NUM	= 0;
	public final int MANUFACTURE 	= 1;
	public final int PACKAGE 		= 2;
	public final int PIN_NUMBER 	= 3;
	public final int DESCRIPTION 	= 4;
	public final int ID 			= 5;
	public final int LINK 			= 6;
	public final int QUANTITY 		= 7;
	public final int LOCATION 		= 8;
	public final int PART_NUMBER 	= 9;
	public final int NUMBER_OF_FIELDS= 10;

	@Override
	public int getFieldsNumber() {
		return NUMBER_OF_FIELDS;
	}

	private static Menu packMenu;

	/**
	 * Manufacture part Number already exist
	 */
	private boolean isExist;

//	private Logger logger = Logger.getLogger(this.getClass());

	@Override
	public void setClassId(){
		setClassId(Component.CLASS_ID_NAME.get(IC_RF));
	}

	@Override
	public void setTitles() {
		setTitles(new InputTitles(
				new String[]{"Mfr P/N","Mfr",	"Package", "<br />Leads Number", "Description", "SeqN" },
				new String[]{ "text", 		"select", 		"select", 		"text", 		"text", 	"label"}));
	}

	@Override
	public void setMenu() {
		if(packMenu==null)
			packMenu = new MenuDAO().getMenu("ic_package","description");
	}

	@Override
	public String getSelectOptionHTML(int index) {

		String valueStr = "";

		switch (index) {
		case MANUFACTURE:
			return getManufactureOptionHTML();

		case PACKAGE:
			valueStr = getPackage();
		}
		return getOptionHTML(packMenu.getKeys(), packMenu.getDescriptions(), valueStr);
	}

	@Override
	public String getValue(int index){
		String returnStr = "";
		
		switch(index){
		case MAN_PART_NUM:
			returnStr = getManufPartNumber();
			break;
		case MANUFACTURE:
			returnStr = getManufactureId();
			break;
		case ID:
			returnStr = getID();
			break;
		case PACKAGE:
			returnStr = getPackage();
			break;
		case PIN_NUMBER:
			returnStr = getLeadsNumber();
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
			break;
		case PART_NUMBER:
			returnStr = getPartNumber();
		}
		
		return returnStr;
	}

	@Override
	public boolean setValue(int index, String valueStr) {
		boolean isSetted = isExist;

		if(index==MAN_PART_NUM || !isExist)
		switch(index){
		case ID:
			isSetted = setComponentId();
			break;
		case PACKAGE:
			isSetted = setPackage(valueStr);
			break;
		case PIN_NUMBER:
			isSetted = setLeadsNumber(valueStr);
			break;
		case PART_NUMBER:
			if(valueStr!=null
					&& valueStr.length()==PART_NUMB_SIZE){
				isSetted = true;
				setPartNumber(valueStr);
			}
			break;
		case MAN_PART_NUM:
			if(valueStr!=null && !valueStr.isEmpty() && !valueStr.equals(getManufPartNumber())){
				setPartNumber(getClassId()+getManufactureIdQ()+"????"+getPackageQ()+getLeadsNumberQ());
				Data tmpComp;
				if(isExist = (tmpComp = new ComponentDAO().getByMPN(valueStr))!=null)
					setValues(tmpComp);
				else{
					super.setValue(super.MAN_PART_NUM, valueStr);
					isExist = false;
				}

				isSetted = true;
			}else{
				super.setValue(super.MAN_PART_NUM, null);
				isExist = false;
			}
			break;
		case MANUFACTURE:
			setPartNumber(getClassId()+getManufactureIdQ()+"????"+getPackageQ()+getLeadsNumberQ());
			isSetted = setManufactureId(valueStr);
			break;
		case DESCRIPTION:
			setPartNumber(getClassId()+getManufactureIdQ()+"????"+getPackageQ()+getLeadsNumberQ());
			isSetted = valueStr!=null && !valueStr.isEmpty();
			super.setValue(super.DESCRIPTION, valueStr);
			break;
		case QUANTITY:
			isSetted = super.setValue(super.QUANTITY, valueStr);
			break;
		case LOCATION:
			isSetted = super.setValue(super.LOCATION, valueStr);
			break;
		case LINK:
			isSetted = super.setValue(super.LINK, valueStr);
		}
		
		return isSetted;
	}

	private String getManufactureId() {
		return getValue(3, 5);
	}

	private String getManufactureIdQ() {
		return (getManufactureId().length()!=0)
				? getManufactureId()
						: "??";
	}

	private boolean setManufactureId(String manufId) {
		boolean isSetted = false;
		
		if(manufId!=null && manufId.length()==2){
			if(!getManufactureId().equals(manufId)){
				setPartNumber(getClassId()+manufId+"????"+getPackageQ()+getLeadsNumberQ());
			}
			super.setManufId(getManufactureIdQ());
			isSetted = true;
		}else
			setPartNumber(getClassId()+"??"+getIDQ()+getPackageQ()+getLeadsNumberQ());


		return isSetted;
	}

	private String getID() {
		String returnStr = "";
		
		if(getPartNumber().length()>9)
			returnStr = getPartNumber().substring(5,9);
		
		return (!returnStr.contains("?"))
								? returnStr
										: "";
	}

	private String getIDQ() {
		return (getID().length()!=0)
				? getID()
						: "????";
	}

	private boolean setComponentId() {
		boolean isSetted = false;
		String tmpStr = getClassId()+getManufactureIdQ();

		if(isSet()){
			if(getID().isEmpty()){
				tmpStr += String.format("%4s", new ComponentDAO().getNewID()).replaceAll(" ", "0"); 
				setPartNumber(tmpStr+getPackageQ()+getLeadsNumberQ());
			}
			isSetted = true;
		}else
			setPartNumber(tmpStr+"????"+getPackageQ()+getLeadsNumberQ());

		return isSetted;
	}

	protected String getPackage() {
		return getValue(9, 11);
	}

	private String getPackageQ() {
		return (getPackage().length()!=0)
				? getPackage()
						: "??";
	}

	private boolean setPackage(String valueStr) {
		boolean isSetted = false;
		
		if(valueStr!=null && valueStr.length()==2){
			if(!getPackage().equals(valueStr))
				setPartNumber(getClassId()+getManufactureIdQ()+"????"+valueStr+getLeadsNumberQ());
			isSetted = true;
		}
		else
			setPartNumber(getClassId()+getManufactureIdQ()+getIDQ()+"??"+getLeadsNumberQ());
		
		return isSetted;
	}

	private String getLeadsNumber() {
		String returnStr = "";
		
		if(getPartNumber().length()==PART_NUMB_SIZE)
			returnStr = getPartNumber().substring(11);
		
		return (!returnStr.contains("?"))
								? returnStr
										: "";
	}

	private String getLeadsNumberQ() {
		return (getLeadsNumber().length()!=0)
				? getLeadsNumber()
						: "???";
	}

	private boolean setLeadsNumber(String valueStr) {
		boolean isSet;

		if(valueStr!=null && !valueStr.isEmpty() && valueStr.length()<=3){
			valueStr = valueStr.replaceAll("\\D", "");
			valueStr = String.format("%3s", valueStr).replace(' ', '0');
			if (checkLeadsQuantity(valueStr)) {
				isSet = true;
				if (!getLeadsNumber().equals(valueStr))
					setPartNumber(getClassId()+getManufactureIdQ()+"????"+getPackageQ()+valueStr);
			}else
				isSet = false;
		}else
			isSet = false;
		if(!isSet)
				setPartNumber(getClassId()+getManufactureIdQ()+getIDQ()+getPackageQ()+"???");
		
		return isSet;
	}

	protected boolean checkLeadsQuantity(String valueStr) {
		return Integer.parseInt(valueStr) > 0;
	}

	@Override
	public boolean isSet() {
		return 		!getManufPartNumber().isEmpty() 
				&&	TextWork.isValid(getManufactureId())
				&&	!getDescription().isEmpty()
				&&	TextWork.isValid(getPackage())
				&&	TextWork.isValid(getLeadsNumber());
	}

	@Override
	protected String getPartType(String partNumber) {
		String str = null;
		if(partNumber!=null && partNumber.length()>12)
			str = new SecondAndThirdDigitsDAO().getClassDescription(IC_RF);
		return str;
	}
}
