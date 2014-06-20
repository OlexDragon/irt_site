package irt.data.components;

import irt.data.Menu;
import irt.data.dao.ComponentDAO;
import irt.data.dao.MenuDAO;
import irt.data.dao.MenuDAO.OrderBy;
import irt.work.InputTitles;
import irt.work.TextWorker;

public class Gasket extends Component {

	private static final int GASKET = TextWorker.GASKET;

	public static final int GASKET_TYPE	= 0;
	public static final int DIAMETER	= 1;
	public static final int ID			= 2;
	private static final int SHIFT 		= 3;
	public static final int DESCRIPTION		= Data.DESCRIPTION		+SHIFT;
	public static final int MANUFACTURE 	= Data.MANUFACTURE		+SHIFT;
	public static final int MAN_PART_NUM 	= Data.MAN_PART_NUM		+SHIFT;
	public static final int QUANTITY 		= Data.QUANTITY			+SHIFT;
	public static final int LOCATION 		= Data.LOCATION			+SHIFT;
	public static final int LINK 			= Data.LINK				+SHIFT;
	public static final int PART_NUMBER 	= Data.PART_NUMBER		+SHIFT;
	public static final int NUMBER_OF_FIELDS= Data.NUMBER_OF_FIELDS	+SHIFT;
	@Override
	public int getFieldsNumber() {
		return NUMBER_OF_FIELDS;
	}

	private static Menu titlesMenu;
	private static Menu typeMenu;

	@Override
	public void setClassId(){
		setClassId(Component.CLASS_ID_NAME.get(GASKET));
	}

	@Override
	public void setTitles() {
		setTitles(new InputTitles( titlesMenu.getKeys(),titlesMenu.getDescriptions()));
	}

	@Override
	public void setMenu() {
		if(titlesMenu==null)
			titlesMenu = new MenuDAO().getMenu("gskt_titles", OrderBy.SEQUENCE);
		if(typeMenu==null)
			typeMenu = new MenuDAO().getMenu("gasket_type", OrderBy.DESCRIPTION);
	}

	@Override
	public int getPartNumberSize() {
		return 12;
	}

	@Override
	public String getSelectOptionHTML(int index) {
		
		String[] tmp = null;
		String[] toShow = null;
		String valueStr = "";

		switch (index) {
		case MANUFACTURE:
			return getManufactureOptionHTML();
			
		case GASKET_TYPE:
			toShow = typeMenu.getDescriptions();
			tmp = typeMenu.getKeys();
			valueStr = getType();
			break;
		}

		return getOptionHTML(tmp, toShow, valueStr);
	}
	@Override
	public String getValue(int index){
		String returnStr = "";
		
		switch(index){
		case GASKET_TYPE:
			returnStr = getType();
			break;
		case DIAMETER:
			returnStr = getDiameter();
			break;
		case ID:
			returnStr = getID();
			break;
		default:
			returnStr = super.getValue(index-SHIFT);
		}
		
		return returnStr;
	}

	@Override
	public boolean setValue(int index, String valueStr) {
		boolean isSet = true ;

		switch(index){
		case ID:
			setComponentId();
			break;
		case GASKET_TYPE:
			setType(valueStr);
			break;
		case DIAMETER:
			setDiameter(valueStr);
			break;
		default:
			isSet = super.setValue(index-SHIFT, valueStr);
		}
		
		return isSet;
	}

	private String getID() {
		return getValue(5,9);
	}

	private String getComponentIDQ() {
		String componentID = getID();
		return (componentID.length()!=0)
				? componentID
						: "????";
	}

	private boolean setComponentId() {

		if(isSet()){
			if(getID().isEmpty()){
				setPartNumber(getClassId()+getTypeQ()+String.format("%4s", new ComponentDAO().getNewSequentialNumber(TextWorker.COUNT_GASKET)).replaceAll(" ", "0")+getDiameterQ());
			}
		}else
			setPartNumber(getClassId()+getTypeQ()+"????"+getDiameterQ());//reset sequential number

		return isSet();
	}

	@Override
	public boolean isSet() {
		return getPartNumber().length()==PART_NUMB_SIZE
				? !(getTypeQ()+getDiameterQ()).contains("?")
						: false;
	}

	private String getType() {
		return getValue(3,5);
	}

	private String getTypeQ() {
		String type = getType();
		return (type.length()!=0)
				? type
						: "??";
	}

	private void setType(String valueStr) {
		if(valueStr==null || valueStr.length()!=2)
			setPartNumber(getClassId()+"??"+getComponentIDQ()+getDiameterQ());
		else{
			if(!valueStr.equals(getType()))
				setPartNumber(getClassId()+valueStr+"????"+getDiameterQ());//reset sequential number
			else
				setPartNumber(getClassId()+valueStr+getComponentIDQ()+getDiameterQ());
		}
	}

	private String getDiameter() {
		String returnStr = "";
		
		if(getPartNumber().length()==PART_NUMB_SIZE)
			returnStr = getPartNumber().substring(9);

		
		return (!returnStr.contains("?"))
								? returnStr
										: "";
	}

	private String getDiameterQ() {
		String type = getDiameter();
		return (type.length()!=0)
				? type
						: "??";
	}

	private void setDiameter(String valueStr) {

		if(valueStr!=null)
			valueStr = valueStr.replaceAll("\\D", "").replaceAll("^[0]+", "");
		else
			valueStr = "";

		if(valueStr.isEmpty())
			setPartNumber(getClassId()+getTypeQ()+getComponentIDQ()+"???");
		else{
			if(valueStr.length()>3)
				valueStr = valueStr.substring(0, 3);

			valueStr = String.format("%03d", Integer.parseInt(valueStr));
			setPartNumber(getClassId()+getTypeQ()+getComponentIDQ()+valueStr);
		}
	}
}