package irt.data.components;

import irt.data.dao.SecondAndThirdDigitsDAO;
import irt.work.InputTitles;
import irt.work.TextWork;

public class Fan extends Component {

	private static final int FAN = TextWork.FAN;
	public final int CFM 			= 0;
	public final int DIAMETER 		= 1;
	public final int MIN_V 			= 2;
	public final int MAX_V 			= 3;
	public final int DESCRIPTION	= 4;
	public final int MANUFACTURE 	= 5;
	public final int MAN_PART_NUM 	= 6;
	public final int QUANTITY 		= 7;
	public final int LOCATION 		= 8;
	public final int LINK 			= 9;
	public final int PART_NUMBER 	=10;
	public final int NUMBER_OF_FIELDS= 11;

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
		case CFM:
			isSetted = setCFM(valueStr);
			break;
		case DIAMETER:
			isSetted = setDiameter(valueStr);
			break;
		case MIN_V:
			isSetted = setMinV(valueStr);
			break;
		case MAX_V:
			isSetted = setMaxV(valueStr);
			break;
		case PART_NUMBER:
			if(valueStr!=null
					&& valueStr.length()==PART_NUMB_SIZE){
				isSetted = true;
				setPartNumber(valueStr);
			}
			break;
		case MAN_PART_NUM:
			isSetted = true;
			super.setValue(super.MAN_PART_NUM, valueStr);
			break;
		case MANUFACTURE:
			isSetted = true;
			super.setValue(super.MANUFACTURE, valueStr);
			break;
		case DESCRIPTION:
			isSetted = true;
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
