package irt.data.components;

import irt.data.Menu;
import irt.data.dao.ComponentDAO;
import irt.work.TextWorker;

public class PlasticParts extends Component {

	private static final int PLUG = TextWorker.PLASTIC_PLARTS;

	public static final int TYPE 	= 0;
	public static final int FOR	 	= 1;
	public static final int ID		= 2;
	private static final int SHIFT 	= 3;
	public static final int DESCRIPTION		= Data.DESCRIPTION		+SHIFT;
	public static final int MANUFACTURE 	= Data.MANUFACTURE		+SHIFT;
	public static final int MAN_PART_NUM 	= Data.MAN_PART_NUM		+SHIFT;
	public static final int QUANTITY 		= Data.QUANTITY			+SHIFT;
	public static final int LOCATION 		= Data.LOCATION			+SHIFT;
	public static final int LINK 			= Data.LINK				+SHIFT;
	public static final int PART_NUMBER 	= Data.PART_NUMBER		+SHIFT;
	public static final int NUMBER_OF_FIELDS= Data.NUMBER_OF_FIELDS	+SHIFT;

	@Override
	public int getPartNumberSize() {
		return 11;
	}

	@Override
	public void setClassId(){
		setClassId(CLASS_ID_NAME.get(PLUG));
	}

	@Override
	protected String getDatabaseNameForTitles() {
		return "plastic_titles";
	}

	@Override
	public String getSelectOptionHTML(int index) {

		String valueStr = "";
		Menu menu = null;

		switch (index) {
		case MANUFACTURE:
			return getManufactureOptionHTML();
		case TYPE:
			valueStr = getType();
			menu = menusForHtmlSelects.get(TYPE);
			break;
		case FOR:
			valueStr = getFor();
			int selection;
			switch(getType()){
			case "BT":
				selection = 2;
				break;
			case "HR":
				selection = 3;
				break;
			case "LP":
				selection = 4;
				break;
			default:
				selection = FOR;
			}
			menu = menusForHtmlSelects.get(selection);
			break;
		}

		String[] keys;
		String[] descriptions;
		if(menu!=null){
			keys = menu.getKeys();
			descriptions = menu.getDescriptions();
		}else{
			keys = null;
			descriptions = null;
		}

		return getOptionHTML(keys, descriptions, valueStr);
	}

	@Override
	public String getValue(int index){
		String returnStr = "";
		
		switch(index){
		case ID:
			returnStr = getComponentId();
			break;
		case FOR:
			returnStr = getFor();
			break;
		case TYPE:
			returnStr = getType();
			break;
		default:
			returnStr = super.getValue(index-SHIFT);
		}
		
		return returnStr;
	}

	@Override
	public boolean setValue(int index, String valueStr) {
		boolean isSet = false;

		switch(index){
		case TYPE:
			isSet = setType(valueStr);
			break;
		case FOR:
			isSet = setFor(valueStr);
			break;
		case ID:
			isSet = setComponentId();
			break;
		default:
			isSet = super.setValue(index-SHIFT, valueStr);
		}

		return isSet;
	}

	private String getType() {
		return getValue(3, 5);
	}

	private String getTypeQ() {
		String str = getType();
		return str.isEmpty() ? "??" : str;
	}

	private boolean setType(String valueStr) {
		boolean isSetted = false;
		
		if(valueStr==null || valueStr.length()!=2)
			valueStr = "??";
		else{
			isSetted = true;
		}

		String getFor = getFor();
		if(getFor.isEmpty())
			setPartNumber(getClassId()+valueStr+getComponentIdQ()+getForQ());

		else if(!getFor.equals(valueStr))
			setPartNumber(getClassId()+valueStr+"????"+getForQ());
		
		return isSetted;
	}

	private String getComponentId() {
		return getValue(5, 9);
	}

	private String getComponentIdQ() {
		String str = getComponentId();
		return str.isEmpty() ? "????" : str;
	}

	private boolean setComponentId() {
		boolean isSetted = false;
		String str;

		if(isSet()){
			str = getComponentId();
			if(str.isEmpty()){
				str = String.format("%4s", new ComponentDAO().getNewPlugID()).replaceAll(" ", "0"); 
			}
			isSetted = true;
		}else
			str = "????";

		setPartNumber(getClassId()+getTypeQ()+str+getForQ());

		return isSetted;
	}

	private String getFor() {
		return getValue(9, 11);
	}

	private String getForQ() {
		String str = getFor();
		return str.isEmpty() ? "??" : str;
	}

	private boolean setFor(String valueStr) {
		boolean isSetted = false;
		
		if(valueStr==null || valueStr.length()!=2)
			valueStr = "??";
		else{
			isSetted = true;
		}

		String getFor = getFor();
		if(getFor.isEmpty())
			setPartNumber(getClassId()+getTypeQ()+getComponentIdQ()+valueStr);

		else if(!getFor.equals(valueStr))
			setPartNumber(getClassId()+getTypeQ()+"????"+valueStr);
		
		return isSetted;
	}

	@Override
	public boolean isSet() {
		return 		!getDescription().isEmpty()
				&&	TextWorker.isValid(getType())
				&&	TextWorker.isValid(getFor());
	}
}
