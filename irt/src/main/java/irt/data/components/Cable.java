package irt.data.components;

import irt.data.Menu;
import irt.data.dao.ComponentDAO;
import irt.data.dao.MenuDAO;
import irt.data.dao.MenuDAO.OrderBy;
import irt.work.InputTitles;
import irt.work.TextWorker;

public class Cable extends Component {

	private static final int CABLES = TextWorker.CABLES;

	public final int CON1_TYPE		= 0;
	public final int CON2_TYPE		= 1;
	public final int CABLE_TYPE		= 2;
	public final int LENGTH			= 3;
	public final int ID				= 4;
	private static final int SHIFT 		= 5;
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
	private static Menu connectorMenu;
	private static Menu cableMenu;

	@Override
	public void setClassId(){
		setClassId(Component.CLASS_ID_NAME.get(CABLES));
	}

	@Override
	public void setTitles() {
		setTitles(new InputTitles( titlesMenu.getKeys(),titlesMenu.getDescriptions()));
	}

	@Override
	public void setMenu() {
		if(titlesMenu==null)
			titlesMenu = new MenuDAO().getMenu("cable_titles", OrderBy.SEQUENCE);
		if(connectorMenu==null)
			connectorMenu = new MenuDAO().getMenu("cab_con_type", OrderBy.DESCRIPTION);
		if(cableMenu==null)
			cableMenu = new MenuDAO().getMenu("cable_type", OrderBy.DESCRIPTION);
	}

	@Override
	public int getPartNumberSize() {
		return 16;
	}

	@Override
	public String getSelectOptionHTML(int index) {
		
		String[] tmp = null;
		String[] toShow = null;
		String valueStr = "";

		switch (index) {
		case MANUFACTURE:
			return getManufactureOptionHTML();
			
		case CON1_TYPE:
			toShow = connectorMenu.getDescriptions();
			tmp = connectorMenu.getKeys();
			valueStr = getCon1Type();
			break;
			
		case CON2_TYPE:
			toShow = connectorMenu.getDescriptions();
			tmp = connectorMenu.getKeys();
			valueStr = getCon2Type();
			break;
			
		case CABLE_TYPE:
			toShow = cableMenu.getDescriptions();
			tmp = cableMenu.getKeys();
			valueStr = getCableType();
			
		}

		return getOptionHTML(tmp, toShow, valueStr);
	}

	@Override
	public String getValue(int index){
		String returnStr = "";
		
		switch(index){
		case CON1_TYPE:
			returnStr = getCon1Type();
			break;
		case CON2_TYPE:
			returnStr = getCon2Type();
			break;
		case LENGTH:
			Value value = new Value(getLength(), Value.LENGTH_SM);
			returnStr = value.getIntValue()>0 ? value.toValueString() : "";
			break;
		case CABLE_TYPE:
			returnStr = getCableType();
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
		case CON1_TYPE:
			setCon1Type(valueStr);
			break;
		case CON2_TYPE:
			setCon2Type(valueStr);
			break;
		case LENGTH:
			setLength(valueStr);
			break;
		case CABLE_TYPE:
			setCableType(valueStr);
			break;
		default:
			isSet = super.setValue(index-SHIFT, valueStr);
		}
		
		return isSet;
	}

	@Override
	public void resetSequentialNunber() {
		setPartNumber(getClassId()+getCon1TypeQ()+getCon2TypeQ()+getCableTypeQ()+getLengthQ()+"???");
	}

	private String getID() {
		String returnStr = "";
		
		if(getPartNumber().length()==PART_NUMB_SIZE)
			returnStr = getPartNumber().substring(13);

		
		return (!returnStr.contains("?"))
								? returnStr
										: "";
	}

	private String getIDQ() {
		String id = getID();
		return (id.length()!=0)
				? id
						: "???";
	}

	private boolean setComponentId() {

		if(isSet()){
			if(getID().isEmpty()){
				setPartNumber(getClassId()+getCon1TypeQ()+getCon2TypeQ()+getCableTypeQ()+getLengthQ()+String.format("%03s", new ComponentDAO().getNewSequentialNumber(TextWorker.COUNT_CABLE)));
			}
		}else
			resetSequentialNunber();

		return isSet();
	}

	@Override
	public boolean isSet() {
		return getPartNumber().length()==PART_NUMB_SIZE ? !getPartNumber().substring(0,13).contains("?") : false;
	}

	private String getCon1Type() {
		return getValue(3,5);
	}

	private String getCon1TypeQ() {
		String con1Type = getCon1Type();
		return (con1Type.length()!=0)
				? con1Type
						: "??";
	}

	private void setCon1Type(String valueStr) {
		if(valueStr==null || valueStr.length()!=2)
			valueStr = "??";
		else if(!valueStr.equals(getCon1Type()))
				resetSequentialNunber();

		setPartNumber(getClassId()+valueStr+getCon2TypeQ()+getCableTypeQ()+getLengthQ()+getIDQ());
	}

	private String getCon2Type() {
		return getValue(5,7);
	}

	private String getCon2TypeQ() {
		return (getCon2Type().length()!=0)
				? getCon2Type()
						: "??";
	}

	private void setCon2Type(String valueStr) {
		if(valueStr==null || valueStr.length()!=2)
			valueStr = "??";
		else if(!valueStr.equals(getCon2Type()))
			resetSequentialNunber();

		setPartNumber(getClassId()+getCon1TypeQ()+valueStr+getCableTypeQ()+getLengthQ()+getIDQ());
	}

	private String getCableType() {
		return getValue(7,9);
	}

	private String getCableTypeQ() {
		return (getCableType().length()!=0)
				? getCableType()
						: "??";
	}

	private void setCableType(String valueStr) {
		if(valueStr==null || valueStr.length()!=2)
			valueStr = "??";
		else if(!valueStr.equals(getCableType()))
			resetSequentialNunber();

		setPartNumber(getClassId()+getCon1TypeQ()+getCon2TypeQ()+valueStr+getLengthQ()+getIDQ());
	}

	private String getLength() {
		return getValue(9,13);
	}

	private String getLengthQ() {
		return (getLength().length()!=0)
				? getLength()
						: "????";
	}

	private void setLength(String lengthStr) {
		Value value = new Value(lengthStr, Value.LENGTH_SM);
		if(value.getIntValue()>0){
			if(!value.toString().equals(getLength())){
				setPartNumber(getClassId()+getCon1TypeQ()+getCon2TypeQ()+getCableTypeQ()+value.toString()+"???");
			}
		}else
			setPartNumber(getClassId()+getCon1TypeQ()+getCon2TypeQ()+getCableTypeQ()+"???????");

	}
}
