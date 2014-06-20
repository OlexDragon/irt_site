package irt.data.components;

import irt.data.dao.SecondAndThirdDigitsDAO;
import irt.work.InputTitles;
import irt.work.TextWorker;

public class Fan extends Component {

	private static final int FAN = TextWorker.FAN;
	public static final int CFM 		= 0;
	public static final int DIAMETER 	= 1;
	public static final int MIN_V 		= 2;
	public static final int MAX_V 		= 3;
	private static final int SHIFT 		= 4;
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
	
	@Override
	public void setClassId(){
		setClassId(Component.CLASS_ID_NAME.get(FAN));
	}

	@Override
	public int getPartNumberSize() {
		return 13;
	}

	@Override
	public void setTitles() {
		setTitles(new InputTitles(
				new String[]{ "CFM",	"Diameter(mm)",	"Min.V",	"Max.V", "<br />Description",	"Mfr",	"Mfr P/N"},
				new String[]{ "text",	"text",		"text",		"text", 	"text",				"select",		"text"}));
	}

	@Override
	public String getSelectOptionHTML(int index) {
		return (index == MANUFACTURE)
				? getManufactureOptionHTML()
				: "";
	}

	@Override
	public String getValue(int index){
		String returnStr = "";
		
		switch(index){
		case CFM:
			returnStr = getCFM();
			break;
		case DIAMETER:
			returnStr = getDiameter();
			break;
		case MIN_V:
			returnStr = getMinV();
			break;
		case MAX_V:
			returnStr = getMaxV();
			break;
		default:
			returnStr = super.getValue(index-SHIFT);
		}
		
		return returnStr;
	}

	@Override
	public boolean setValue(int index, String valueStr){
		boolean isSet = false;
		
		switch(index){
		case CFM:
			isSet = setCFM(valueStr);
			break;
		case DIAMETER:
			isSet = setDiameter(valueStr);
			break;
		case MIN_V:
			isSet = setMinV(valueStr);
			break;
		case MAX_V:
			isSet = setMaxV(valueStr);
			break;
		default:
			isSet = super.setValue(index-SHIFT, valueStr);
		}
		
		return isSet;
	}

	private String getCFM() {
		return getValue(3, 6);
	}
	
	private String getCFMQ() {
		return (getCFM().length()!=0)
				? getCFM()
						: "???";
	}
	
	private boolean setCFM(String valueStr) {
		boolean isSetted = false;
		
		valueStr = getInt(valueStr,3);
		if (Integer.parseInt(valueStr) != 0) {
				isSetted = true;
		}else
			valueStr = "???" ;
		
		setPartNumber(getClassId() + valueStr + getDiameterQ() + getMinVQ() + getMaxVQ());

		return isSetted;
	}

	private String getDiameter() {
		return getValue(6, 9);
	}

	private String getDiameterQ() {
		return (getDiameter().length()!=0)
				? getDiameter()
						: "???";
	}

	private boolean setDiameter(String valueStr) {
		boolean isSetted = false;
		
		valueStr = getInt(valueStr,3);
		if (Integer.parseInt(valueStr) != 0) {
				isSetted = true;
		}else
			valueStr = "???" ;
		
		setPartNumber(getClassId() + getCFMQ() + valueStr + getMinVQ() + getMaxVQ());

		return isSetted;
	}

	private String getMinV() {
		return getValue(9, 11);
	}

	private String getMinVQ() {
		return (getMinV().length()!=0)
				? getMinV()
						: "??";
	}

	private boolean setMinV(String valueStr) {
		boolean isSetted = false;
		
		valueStr = getInt(valueStr,2);
		if (Integer.parseInt(valueStr) != 0) {
				isSetted = true;
		}else
			valueStr = "??" ;
		
		setPartNumber(getClassId() + getCFMQ() + getDiameterQ() + valueStr + getMaxVQ());

		return isSetted;
	}

	private String getMaxV() {
		String returnStr = "";
		
		if(getPartNumber().length()==PART_NUMB_SIZE)
			returnStr = getPartNumber().substring(11);
		
		return (!returnStr.contains("?"))
								? returnStr
										: "";
	}

	private String getMaxVQ() {
		return (getMaxV().length()!=0)
				? getMaxV()
						: "??";
	}

	private boolean setMaxV(String valueStr) {
		boolean isSetted = false;
		
		valueStr = getInt(valueStr,2);
		if (Integer.parseInt(valueStr) != 0) {
				isSetted = true;
		}else
			valueStr = "??" ;
		
		setPartNumber(getClassId() + getCFMQ() + getDiameterQ() + getMinVQ() + valueStr);

		return isSetted;
	}

	@Override
	protected String getPartType(String partNumber) {
		String str = null;
		if(partNumber!=null && partNumber.length()>12){
			str = new SecondAndThirdDigitsDAO().getClassDescription(FAN);
		}
		return str;
	}
}
