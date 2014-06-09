package irt.data.components;

import irt.data.Menu;
import irt.data.dao.ComponentDAO;
import irt.data.dao.MenuDAO;
import irt.data.dao.SecondAndThirdDigitsDAO;
import irt.work.InputTitles;
import irt.work.TextWork;

public class Diode extends Component {

	private static final int DIODE = TextWork.DIODE;

	public final int MAN_PART_NUM	= 0;
	public final int TYPE 			= 1;
	public final int PACKAGE 		= 2;
	public final int ID 			= 3;
	public final int DESCRIPTION	= 4;
	public final int MANUFACTURE	= 5;
	public final int QUANTITY 		= 6;
	public final int LOCATION 		= 7;
	public final int LINK 			= 8;
	public final int PART_NUMBER 	= 9;
	public final int NUMBER_OF_FIELDS= 10;

	@Override
	public int getFieldsNumber() {
		return NUMBER_OF_FIELDS;
	}
	
	private static Menu packMenu;
	private static Menu typeMenu;
	private static String activeClass;

	private boolean isExist;

	@Override
	public void setClassId(){
		setClassId(Component.CLASS_ID_NAME.get(DIODE));
	}

	@Override
	public int getPartNumberSize() {
		return 11;
	}

	@Override
	public void setTitles() {
		setTitles(new InputTitles(
				new String[]{ "Mfr P/N","Type",		"Package",	"SeqN", "<br />Description",	"Mfr"},
				new String[]{ "text",	"select",	"select", 	"label",	"text",				"select"}));
	}

	@Override
	public void setMenu() {
		if(activeClass != getClassId()){
			activeClass = getClassId();
			packMenu = getPackages();
			typeMenu = getTypes();
		}
	}

	public Menu getPackages() {
		return new MenuDAO().getMenu("ic_package","description");
	}

	public Menu getTypes() {
		return new MenuDAO().getMenu("diode_type","description");
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
			tmp = typeMenu.getKeys();
			toShow = typeMenu.getDescriptions();
			valueStr = getType();
			break;

		case PACKAGE:
			tmp = packMenu.getKeys();
			toShow = packMenu.getDescriptions();
			valueStr = getPackage();
		}
		
		return getOptionHTML(tmp, toShow, valueStr);
	}

	@Override
	public String getValue(int index){
		String returnStr = "";
		
		switch(index){
		case MAN_PART_NUM:
			returnStr = getManufPartNumber();
			break;
		case ID:
			returnStr = getID();
			break;
		case TYPE:
			returnStr = getType();
			break;
		case PACKAGE:
			returnStr = getPackage();
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
		case TYPE:
			isSetted = setType(valueStr);
			break;
		case PACKAGE:
			isSetted = setPackage(valueStr);
			break;
		case PART_NUMBER:
			if(valueStr!= null 
					&& valueStr.length()==PART_NUMB_SIZE){
				isSetted = true;
				setPartNumber(valueStr);
			}
			break;
		case MAN_PART_NUM:
			if(valueStr!=null
					&& valueStr!=null && !valueStr.isEmpty()){
				Data tmpComp;
				if(isExist = (tmpComp = new ComponentDAO().getByMPN(valueStr))!=null){
					setValues(tmpComp);
					if(getClassId().equals(tmpComp.getPartNumber().substring(0, 3)))
						isSetted = true;
					else
						getError().setErrorMessage("Manufacture P/N '"+getManufPartNumber()
								+"' - alredy exist.<br />P/N - "+tmpComp.getPartNumberF());				
				}else{
					super.setValue(super.MAN_PART_NUM, valueStr);
					isSetted = true;
				}
			}else
				super.setValue(super.MAN_PART_NUM, null);
			break;
		case MANUFACTURE:
			isSetted = super.setValue(super.MANUFACTURE, valueStr);
			break;
		case DESCRIPTION:
			isSetted = super.setValue(super.DESCRIPTION, valueStr);
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

	private String getID() {
		String returnStr = "";
		
		if(getPartNumber().length()>7)
			returnStr = getPartNumber().substring(3,7);
		
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
		String tmpStr = getClassId();
		
		if(isSet()){
			tmpStr += TextWork.addZeroInFront(new ComponentDAO().getNewID(), 4); 
			isSetted = true;
		}else
			tmpStr += "????";
			
		setPartNumber(tmpStr+getTypeQ()+getPackageQ());

		return isSetted;
	}

	protected String getType() {
		return getValue(7, 9);
	}

	private String getTypeQ() {
		return (getType().length()!=0)
				? getType()
						: "??";
	}

	private boolean setType(String valueStr) {
		boolean isSetted = false;
		String tmpStr = getClassId()+getIDQ();
		
		if(valueStr!=null && valueStr.length()==2){
			tmpStr += valueStr;
			isSetted = true;
		}
		else
			tmpStr += "??";
		
		setPartNumber(tmpStr+getPackageQ());
		
		return isSetted;
	}

	protected String getPackage() {
		String returnStr = "";
		
		if(getPartNumber().length()==PART_NUMB_SIZE)
			returnStr = getPartNumber().substring(9);
		
		return (!returnStr.contains("?"))
								? returnStr
										: "";
	}

	private String getPackageQ() {
		return (getPackage().length()!=0)
				? getPackage()
						: "??";
	}

	private boolean setPackage(String valueStr) {
		boolean isSetted = false;
		String tmpStr = getClassId()+getIDQ()+getTypeQ();
		
		if(valueStr!=null && valueStr.length()==2){
			tmpStr += valueStr;
			isSetted = true;
		}
		else
			tmpStr += "??";
		
		setPartNumber(tmpStr);
		
		return isSetted;
	}

	@Override
	public boolean isSet() {
		return 		!getManufPartNumber().isEmpty() 
				&&	getID()!=null
				&&	!getType().isEmpty()
				&&	!getPackage().isEmpty();
	}

	@Override
	protected String getPartType(String partNumber) {
		String str = null;
		if(partNumber!=null && partNumber.length()>10){
			str = new SecondAndThirdDigitsDAO().getClassDescription(DIODE);
		}
		return str;
	}
}