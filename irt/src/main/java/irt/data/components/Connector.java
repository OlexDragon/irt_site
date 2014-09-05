package irt.data.components;

import irt.data.Menu;
import irt.data.dao.ComponentDAO;
import irt.data.dao.MenuDAO;
import irt.data.dao.SecondAndThirdDigitsDAO;
import irt.data.dao.MenuDAO.OrderBy;
import irt.work.InputTitles;
import irt.work.TextWorker;

public class Connector extends Component {
	
	private static final int CONNECTOR = TextWorker.CONNECTOR;
	public final int MAN_PART_NUM	= 0;
	public final int CONTACT_NUMBER	= 1;
	public final int MALE_FEMALE	= 2;
	public final int TYPE		 	= 3;
	public final int ID 			= 4;
	public final int DESCRIPTION 	= 5;
	public final int MANUFACTURE 	= 6;
	public final int LINK 			= 7;
	public final int QUANTITY 		= 8;
	public final int LOCATION 		= 9;
	public final int PART_NUMBER 	= 10;
	public final int NUMBER_OF_FIELDS= 11;

	@Override
	public int getFieldsNumber() {
		return NUMBER_OF_FIELDS;
	}
	
	private boolean isExist;
	private Menu typeMenu;
	private Menu maleFemaleMenu;

	@Override
	public void setClassId(){
		setClassId(Component.CLASS_ID_NAME.get(CONNECTOR));
	}

	@Override
	public int getPartNumberSize() {
		return 13;
	}

	@Override
	public void setTitles() {
		setTitles(new InputTitles(
				new String[]{ "Mfr P/N", "Numb.of pin",	"M/F",		"Type",		"Seq.Num.", 	"<br />Description",	"Mfr"},
				new String[]{ "text",	"text", 		"select",	"select", 	"label", 		"text", 				"select"}));
	}

	@Override
	public void setMenu() {
		if(typeMenu==null)
			typeMenu = new MenuDAO().getMenu("con_type", OrderBy.DESCRIPTION);
		if(maleFemaleMenu==null)
			maleFemaleMenu = new MenuDAO().getMenu("M_F", OrderBy.DESCRIPTION);
	}

	@Override
	public String getSelectOptionHTML(int index) {
		String[] tmp = null;
		String[] toShow = null;
		String valueStr = "";

		switch (index) {
		case MANUFACTURE:
			return getManufactureOptionHTML();

		case MALE_FEMALE:
			tmp = maleFemaleMenu.getKeys();
			toShow = maleFemaleMenu.getDescriptions();
			valueStr = getFeMale();
			break;
			
		case TYPE:
			tmp 	= typeMenu.getKeys();
			toShow	= typeMenu.getDescriptions();
			valueStr = getType();
		}
		return getOptionHTML(tmp, toShow, valueStr);
	}

	@Override
	public String getValue(int index){
		String returnStr = "";
		
		switch(index){
		case MAN_PART_NUM:
			returnStr = getMfrPN();
			break;
		case MANUFACTURE:
			returnStr = getMfrId();
			break;
		case ID:
			returnStr = getID();
			break;
		case MALE_FEMALE:
			returnStr = getFeMale();
			break;
		case TYPE:
			returnStr = getType();
			break;
		case CONTACT_NUMBER:
			returnStr = getContacts();
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
		case MALE_FEMALE:
			isSetted = setFeMale(valueStr);
			break;
		case CONTACT_NUMBER:
			isSetted = setContacts(valueStr);
			break;
		case PART_NUMBER:
			if(valueStr!=null
					&& valueStr.length()==PART_NUMB_SIZE){
				isSetted = true;
				setPartNumber(valueStr);
			}
			break;
		case MAN_PART_NUM:
			if(valueStr!=null && !valueStr.isEmpty()){
				Data tmpComp;
				if(isExist = (tmpComp = new ComponentDAO().getByMPN(valueStr))!=null)
					setValues(tmpComp);
				else{
					super.setValue(Data.MAN_PART_NUM, valueStr);
					isExist = false;
				}

				isSetted = true;
			}else{
				super.setValue(Data.MAN_PART_NUM, null);
				isSetted = false;
			}
			break;
		case MANUFACTURE:
			isSetted = true;
			setManufId(valueStr);
			break;
		case DESCRIPTION:
			isSetted = super.setValue(Data.DESCRIPTION, valueStr);
			break;
		case QUANTITY:
			isSetted = super.setValue(Data.QUANTITY, valueStr);
			break;
		case LOCATION:
			isSetted = super.setValue(Data.LOCATION, valueStr);
			break;
		case LINK:
			isSetted = super.setValue(Data.LINK, valueStr);
		}
		
		return isSetted;
	}

	private String getContacts() {
		return getValue(3, 6);
	}

	private String getContactsQ() {
		return (getContacts().length()!=0)
				? getContacts()
						: "???";
	}

	private boolean setContacts(String valueStr) {
		boolean isSetted = false;
		int value = new Value(valueStr, TextWorker.CAPACITOR).getIntValue();
		if (value != 0){
			isSetted = true;
			valueStr = String.format("%3s", value).replaceAll(" ", "0");
		}else
			valueStr = "???";
		
		setPartNumber(getClassId() + valueStr + getFeMaleQ()
				+ getTypeQ() + getIDQ());

		return isSetted;
	}

	private String getFeMale() {
		return getValue(6, 7);
	}

	private String getFeMaleQ() {
		return (getFeMale().length()!=0)
				? getFeMale()
						: "?";
	}

	private boolean setFeMale(String valueStr) {
		boolean isSetted = false;
		String tmpStr = getClassId()+getContactsQ();
		
		if(valueStr!=null && valueStr.length()==1){
			tmpStr += valueStr;
			isSetted = true;
		}
		else
			tmpStr += "?";
		
		setPartNumber(tmpStr+getTypeQ()+getIDQ());
		
		return isSetted;
	}

	private String getType() {
			return getValue(7, 9);
	}

	private String getTypeQ() {
		return (getType().length()!=0)
				? getType()
						: "??";
	}

	private boolean setType(String valueStr) {
		boolean isSetted = false;
		String tmpStr = getClassId()+getContactsQ()+getFeMaleQ();
		
		if(valueStr!=null && valueStr.length()==2){
			tmpStr += valueStr;
			isSetted = true;
		}
		else
			tmpStr += "??";
		
		setPartNumber(tmpStr+getIDQ());
		
		return isSetted;
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
		boolean isSetted = false;
		String tmpStr = getClassId()+getContactsQ()+getFeMaleQ()+getTypeQ();
		
		if(isSet()){
			tmpStr += TextWorker.addZeroInFront(new ComponentDAO().getNewID(), 4); 
			isSetted = true;
		}else
			tmpStr += "????";
			
		setPartNumber(tmpStr);

		return isSetted;
	}

	@Override
	public boolean isSet() {
		return 		!getContacts().isEmpty()
				&&	!getFeMale().isEmpty()
				&&	!getMfrPN().isEmpty()
				&&	!getType().isEmpty();
	}

	@Override
	protected String getPartType(String partNumber) {
		String str = null;
		if(partNumber!=null && partNumber.length()>12){
			str = new SecondAndThirdDigitsDAO().getClassDescription(CONNECTOR);
		}
		return str;
	}
}