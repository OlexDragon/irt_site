package irt.data.components;

import irt.data.Menu;
import irt.data.dao.ComponentDAO;
import irt.data.dao.MenuDAO;
import irt.work.InputTitles;
import irt.work.TextWork;

public class WireHarness extends Component{

	private static final int WIRE_HARNESS = TextWork.WIRE_HARNESS;

	public final int WIRE_NUMBER	= 0;
	public final int LENGTH			= 1;
	public final int ID 			= 2;
	public final int DESCRIPTION	= 3;
	public final int MANUFACTURE 	= 4;
	public final int MAN_PART_NUM 	= 5;
	public final int QUANTITY 		= 6;
	public final int LOCATION 		= 7;
	public final int LINK 			= 8;
	public final int PART_NUMBER 	= 9;
	public final int NUMBER_OF_FIELDS= 10;
	@Override
	public int getFieldsNumber() {
		return NUMBER_OF_FIELDS;
	}

	private static Menu titlesMenu;

	@Override
	public void setClassId(){
		setClassId(Component.CLASS_ID_NAME.get(WIRE_HARNESS));
	}

	@Override
	public void setTitles() {
		setTitles(new InputTitles( titlesMenu.getKeys(),titlesMenu.getDescriptions()));
	}

	@Override
	public void setMenu() {
		if(titlesMenu==null)
			titlesMenu = new MenuDAO().getMenu("wire_titles", "sequence");
	}

	@Override
	public String getSelectOptionHTML(int index) {
		return getManufactureOptionHTML();
	}

	@Override
	public String getValue(int index){
		String returnStr = "";
		
		Value value;
		switch(index){
		case WIRE_NUMBER:
			value = new Value(getWireNumber(), Value.NUMBER);
			returnStr = value.getIntValue()>0 ? value.toValueString() : "";
			break;
		case LENGTH:
			value = new Value(getWireLength(), Value.NUMBER);
			returnStr = value.getIntValue()>0 ? value.toValueString() : "";
			break;
		case ID:
			returnStr = getID();
			break;
		case MAN_PART_NUM:
			returnStr = getManufPartNumber();
			break;
		case MANUFACTURE:
			returnStr = super.getValue(super.MANUFACTURE);
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
		boolean isSet = true ;

		switch(index){
		case ID:
			isSet = setComponentId();
			break;
		case WIRE_NUMBER:
			setWireNumber(valueStr);
			break;
		case LENGTH:
			setWireLength(valueStr);
			break;
		case PART_NUMBER:
			if(valueStr!=null
					&& valueStr.length()==PART_NUMB_SIZE){
				isSet = true;
				setPartNumber(valueStr);
			}
			break;
		case MAN_PART_NUM:
			isSet = super.setValue(super.MAN_PART_NUM, valueStr);
			break;
		case MANUFACTURE:
			isSet = super.setValue(super.MANUFACTURE, valueStr);
			break;
		case DESCRIPTION:
			isSet = valueStr!=null && !valueStr.isEmpty();
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
	public int getPartNumberSize() {
		return 13;
	}

	@Override
	public void resetSequentialNunber() {
		setPartNumber(getClassId()+getWireNumberQ()+getWireLengthQ()+"????");
	}

	private String getID() {
		String returnStr = "";
		
		if(getPartNumber().length()==PART_NUMB_SIZE)
			returnStr = getPartNumber().substring(9);
		
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

		boolean isSet = isSet();
		if(isSet){
			if(getID().isEmpty()){
				setPartNumber(getClassId()+getWireNumberQ()+getWireLengthQ() + String.format("%4s", new ComponentDAO().getNewHarnessID()).replaceAll(" ", "0"));
			}
		}else
			resetSequentialNunber();

		return isSet;
	}

	@Override
	public boolean isSet() {
		return getPartNumber().length()>9 ? !getPartNumber().substring(0,9).contains("?") : false;
	}

	private String getWireNumber() {
		return getValue(3, 5);
	}

	private String getWireNumberQ() {
		return (!getWireNumber().isEmpty())
				? getWireNumber()
						: "??";
	}

	private void setWireNumber(String numberOfWires) {
		Value value = new Value(numberOfWires, Value.NUMBER);
		if(value.getIntValue()>0){
			if(!getWireNumber().equals(value.getIntStr(2))){
				setPartNumber(getClassId()+value.getIntStr(2)+getWireLengthQ()+getIDQ());
				resetSequentialNunber();
			}
		}else
			resetSequentialNunber();
	}

	private String getWireLength() {
		return getValue(5, 9);
	}

	private String getWireLengthQ() {
		return (!getWireLength().isEmpty())
				? getWireLength()
						: "????";
	}

	private void setWireLength(String lengthStr) {
		Value value = new Value(lengthStr, Value.LENGTH_SM);
		if(value.getIntValue()>0){
			if(!getWireLength().equals(value.toString())){
				setPartNumber(getClassId()+getWireNumberQ()+value.toString()+getIDQ());
				resetSequentialNunber();
			}
		}else
			resetSequentialNunber();
	}
}