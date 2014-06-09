package irt.data.components;

import irt.data.Menu;
import irt.data.dao.MenuDAO;
import irt.data.dao.SecondAndThirdDigitsDAO;
import irt.work.InputTitles;
import irt.work.TextWork;

public class Isolator extends Component {
	
	private static final int ISOLATOR = TextWork.ISOLATOR;
	public static final int BAND 			= 0;
	public static final int POWER 			= 1;
	public static final int PACKAGE 		= 2;
	public static final int TYPE 			= 3;
	public static final int DESCRIPTION	= 4;
	public static final int MANUFACTURE 	= 5;
	public static final int MAN_PART_NUM 	= 6;
	public static final int QUANTITY 		= 7;
	public static final int LOCATION 		= 8;
	public static final int LINK 			= 9;
	public static final int PART_NUMBER 	=10;
	public static final int NUMBER_OF_FIELDS= 11;

	@Override
	public int getFieldsNumber() {
		return NUMBER_OF_FIELDS;
	}
	
	private static Menu titlesMenu;
	private static Menu bandMenu;
	private static Menu packMenu;
	private static Menu typeMenu;
	private static String activeClass;

	@Override
	public void setClassId(){
		setClassId(Component.CLASS_ID_NAME.get(ISOLATOR));
	}

	@Override
	public int getPartNumberSize() {
		return 12;
	}

	@Override
	public void setTitles() {
		setTitles(new InputTitles(titlesMenu.getKeys(),titlesMenu.getDescriptions()));
	}

	@Override
	public void setMenu() {

		if(activeClass==null || !activeClass.equals(getClassId())){
			titlesMenu = getTitlesMenu();
			activeClass = getClassId();
			packMenu = getPackages();
			typeMenu = getTypes();
			bandMenu = new MenuDAO().getMenu("band","description");
		}
	}

	protected Menu getTitlesMenu() {
		return new MenuDAO().getMenu("isol_titles","sequence");
	}

	public Menu getPackages() {
		return new MenuDAO().getMenu("isol_package","description");
	}


	public Menu getTypes() {
		return new MenuDAO().getMenu("isol_type","description");
	}

	@Override
	public String getSelectOptionHTML(int index) {
		
		String[] tmp = null;
		String[] toShow = null;
		String valueStr = "";
		Menu menu = null;

		switch (index) {
		case MANUFACTURE:
			return getManufactureOptionHTML();
			
		case BAND:
			menu = bandMenu;
			valueStr = getBand();
			break;
			
		case PACKAGE:
			menu = packMenu;
			valueStr = getPackage();
			break;
			
		case TYPE:
			menu = typeMenu;
			valueStr = getType();
		}

		toShow = menu.getDescriptions();
		tmp = menu.getKeys();

		return getOptionHTML(tmp, toShow, valueStr);
	}

	@Override
	public String getValue(int index){
		String returnStr = "";
		
		switch(index){
		case BAND:
			returnStr = getBand();
			break;
		case TYPE:
			returnStr = getType();
			break;
		case PACKAGE:
			returnStr = getPackage();
			break;
		case POWER:
			returnStr = getPower();
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
		boolean isSetted = false;
		
		switch(index){
		case BAND:
			isSetted = setBand(valueStr);
			break;
		case TYPE:
			isSetted = setType(valueStr);
			break;
		case PACKAGE:
			isSetted = setPackage(valueStr);
			break;
		case POWER:
			isSetted = setPower(valueStr);
			break;
		case PART_NUMBER:
			if(valueStr!=null
					&& valueStr.length()==PART_NUMB_SIZE){
				isSetted = true;
				setPartNumber(valueStr);
			}
			break;
		case MAN_PART_NUM:
			isSetted = super.setValue(super.MAN_PART_NUM, valueStr);
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

	protected String getBand() {
		return getValue(3,6);
	}

	protected String getBandQ() {
		return (getBand().length()!=0)
				? getBand()
						: "???";
	}

	private boolean setBand(String valueStr) {
		boolean isSetted = false;

		if(valueStr!=null && valueStr.length()==3){
			isSetted = true;
		}else
			valueStr =  "???";

		setPartNumber(getClassId()+valueStr+getPoverQ()+getPackageQ()+getTypeQ());

		return isSetted;
	}

	protected String getPower() {
		return getValue(6,8);
	}

	protected String getPoverQ() {
		return (getPower().length()!=0)
				? getPower()
						: "??";
	}

	private boolean setPower(String valueStr) {
		boolean isSetted = false;
		
		valueStr = getInt(valueStr, 2);
		if (Integer.parseInt(valueStr) != 0) {
				isSetted = true;
		}else
			valueStr = "??" ;
		
		setPartNumber(getClassId()+getBandQ()+valueStr+getPackageQ()+getTypeQ());

		return isSetted;
	}

	protected String getPackage() {
		return getValue(8,10);
	}

	protected String getPackageQ() {
		return (getPackage().length()!=0)
				? getPackage()
						: "??";
	}

	private boolean setPackage(String valueStr) {
		boolean isSetted = false;
		
		if(valueStr!=null && valueStr.length()==2){
			isSetted = true;
		}else
			valueStr =  "??";

		setPartNumber(getClassId()+getBandQ()+getPoverQ()+valueStr+getTypeQ());

		return isSetted;
	}

	protected String getType() {
		String returnStr = "";
		
		if (getPartNumber().length()==PART_NUMB_SIZE)
							returnStr = getPartNumber().substring(10);
		
		return returnStr.contains("?") ? "" : returnStr;
	}

	protected String getTypeQ() {
		return (getType().length()!=0)
				? getType()
						: "??";
	}

	private boolean setType(String valueStr) {
		boolean isSetted = false;
		
		if(valueStr!=null && valueStr.length()==2){
			isSetted = true;
		}else
			valueStr =  "??";

		setPartNumber(getClassId()+getBandQ()+getPoverQ()+getPackageQ()+valueStr);

		return isSetted;
	}

	@Override
	protected String getPartType(String partNumber) {
		String str = null;
		if(partNumber!=null && partNumber.length()>12){
			str = new SecondAndThirdDigitsDAO().getClassDescription(ISOLATOR);
		}
		return str;
	}

	@Override
	public String getPartNumberF() {
		return TextWork.getPartNumber(getPartNumber(), 3, 8, PART_NUMB_SIZE, PART_NUMB_SIZE);
	}
}