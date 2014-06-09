package irt.data.components;

import irt.data.Menu;
import irt.data.dao.MenuDAO;
import irt.data.dao.SecondAndThirdDigitsDAO;
import irt.work.InputTitles;
import irt.work.TextWork;

import java.text.DecimalFormat;

public class Inductor extends Component {

	private static final int INDUCTOR = TextWork.INDUCTOR;
	public final int VALUE			= 0;
	public final int PRECISION 		= 1;
	public final int PACKAGE 		= 2;
	public final int CURRENT 		= 3;
	public final int DESCRIPTION 	= 4;
	public final int MANUFACTURE 	= 5;
	public final int MAN_PART_NUM 	= 6;
	public final int QUANTITY 		= 7;
	public final int LOCATION 		= 8;
	public final int LINK 			= 9;
	public final int PART_NUMBER 	=10;
	public final int NUMBER_OF_FIELDS= 11;

	private static Menu typeMenu;
	private static Menu precisionMenu;

	@Override
	public int getFieldsNumber() {
		return NUMBER_OF_FIELDS;
	}

	//	private Logger logger = Logger.getLogger(this.getClass());

	@Override
	public void setClassId(){
		setClassId(Component.CLASS_ID_NAME.get(INDUCTOR));
	}

	@Override
	public void setTitles() {
		setTitles(new InputTitles(
				new String[] 
						{ "Value(nH)", "Precision(%)", "Package","Current(mA)","<br />Description",	"Mfr",	"MfrP/N"  },
					new String[]
						{ "text",		"select",		"select",	"text",		"text",				"select",		"text"}));
	}

	@Override
	public void setMenu() {
		if(typeMenu==null){
			typeMenu = new MenuDAO().getMenu("size","description");
			if(typeMenu!=null)
				typeMenu.add( new MenuDAO().getMenu("ic_package","description"));
		}

		if(precisionMenu==null){
			precisionMenu = new MenuDAO().getMenu("precision","description");
		}
	}

	@Override
	public String getSelectOptionHTML(int index) {

		String[] tmp = null;
		String[] toShow = null;
		String valueStr = "";

		switch (index) {
		case MANUFACTURE:
			return getManufactureOptionHTML();

		case PACKAGE:
			toShow = typeMenu.getDescriptions();
			tmp = typeMenu.getKeys();
			valueStr = getPackage();
			break;
		case PRECISION:
			toShow = precisionMenu.getDescriptions();
			tmp = precisionMenu.getKeys();
			valueStr = getPrecision();
		}

		return getOptionHTML(tmp, toShow, valueStr);
	}

	@Override
	public String getValue(int index){
		String returnStr = "";
		
		switch(index){
		case VALUE:
			returnStr = getValue().isEmpty() ? "" : new  Value(getValue(), Value.INDUCTOR).toValueString();
			break;
		case PRECISION:
			returnStr = getPrecision();
			break;
		case PACKAGE:
			returnStr = getPackage();
			break;
		case CURRENT:
			returnStr = getCurrent().isEmpty() ? "" : new  Value(getCurrent(), Value.CURRENT).toValueString();
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
	public boolean setValue(int index, String valueStr) {
		boolean isSetted = false;

		switch (index) {
		case VALUE:
			isSetted = setValue(valueStr);
			break;
		case PRECISION:
			isSetted = setPrecision(valueStr);
			break;
		case CURRENT:
			isSetted = setCurrent(valueStr);
			break;
		case PACKAGE:
			isSetted = setPackage(valueStr);
			break;
		case PART_NUMBER:
			if (valueStr!=null
					&& valueStr.length() == PART_NUMB_SIZE) {
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

	@Override
	public String getValue() {
		return getValue(3, 7);
	}

	private String getValueQ() {
		return (getValue().length()!=0)
							? getValue()
									: "????";
	}
	
	private boolean setValue(String valueStr) {
		boolean isSetted = false;
		
		if(valueStr!=null && !valueStr.isEmpty()){
			Value value = new Value(valueStr, TextWork.INDUCTOR);
			valueStr = value.toString();
			setPartNumber(getClassId()+valueStr+getPrecisionQ()+getPackageQ()+getCurrentQ());
			setDbValue(value.toValueString());
			isSetted = true;
		}else
			setPartNumber(getClassId()+"????"+getPrecisionQ()+getPackageQ()+getCurrentQ());

		return isSetted;
	}

	private String getPrecision() {
		return getValue(7,8);
	}

	private String getPrecisionQ() {
		return (getPrecision().length()!=0)
				? getPrecision()
						: "?";
	}

	private boolean setPrecision(String valueStr) {
		boolean isSetted = false;
		
		if(valueStr!=null && valueStr.length()==1){
			setPartNumber(getClassId()+getValueQ()+valueStr+getPackageQ()+getCurrentQ());
			isSetted = true;
		}else
			setPartNumber(getClassId()+getValueQ()+"?"+getPackageQ()+getCurrentQ());
		
		return isSetted;
	}

	private String getPackage() {
		return getValue(8, 10);
	}

	private String getPackageQ() {
		return (getPackage().length()!=0)
				? getPackage()
						: "??";
	}

	private boolean setPackage(String valueStr) {
		boolean isSetted = false;
		
		if(valueStr!=null && valueStr.length()==2){
			setPartNumber(getClassId()+getValueQ()+getPrecisionQ()+valueStr+getCurrentQ());
			isSetted = true;
		}else
			setPartNumber(getClassId()+getValueQ()+getPrecisionQ()+"??"+getCurrentQ());
		
		return isSetted;
	}

	private String getCurrent() {
		String returnStr = "";
		
		if (getPartNumber().length()==PART_NUMB_SIZE)
			returnStr = getPartNumber().substring(10);
		
		return (!returnStr.contains("?"))
				? returnStr
						: "";
	}

	private String getCurrentQ() {
		return (getCurrent().length()!=0)
				? getCurrent()
						: "????";
	}

	private boolean setCurrent(String valueStr) {
		boolean isSetted = false;
		
		if(valueStr!=null && !valueStr.isEmpty()){
			valueStr = new Value(valueStr, TextWork.CURRENT).toString();
			setPartNumber(getClassId()+getValueQ()+getPrecisionQ()+getPackageQ()+valueStr);
			isSetted = true;
		}else
			setPartNumber(getClassId()+getValueQ()+getPrecisionQ()+getPackageQ()+"????");
		
		return isSetted;
	}

	@Override
	protected String getValue(String partNumber) {
		String prefix = null;
		String[] beforeEafter;
		double output = 0;

		if (partNumber != null && partNumber.length() > 7) {
			partNumber = partNumber.substring(3, 7);
			if (!partNumber.contains("?") && partNumber.contains("E")) {
				beforeEafter = partNumber.split("E");
				beforeEafter[0] = beforeEafter[0] + '0';
				if (!beforeEafter[1].equals("A"))
					beforeEafter[0] = String.format( "%-" + (Integer.parseInt(beforeEafter[1]) + 4) + "s", beforeEafter[0]).replaceAll(" ", "0");
				long value = Long.parseLong(beforeEafter[0]);
				if (value >= 1000000) {
					output = value / 1000000.0;
					prefix = "uH";
				} else {
					output = value / 1000.0;
					prefix = "nH";
				}
			}
		}
		return new DecimalFormat("#.##").format(output) + prefix;
	}

	@Override
	public String getPartNumberF() {
		return TextWork.getPartNumber(getPartNumber(), 3, 10, PART_NUMB_SIZE, PART_NUMB_SIZE);
	}

	@Override
	protected String getPartType(String partNumber) {
		String str = null;
		if(partNumber!=null && partNumber.length()>12){
			str = new SecondAndThirdDigitsDAO().getClassDescription(INDUCTOR);
			str += "\\\\"+getDbValue().replaceAll("[\\d. ]", "");
		}
		return str;
	}

	@Override// Currant
	protected String getDbVoltage(String partNumber) {
		String str = null;
		if(partNumber!=null && partNumber.length()==14){
			str = partNumber.substring(10);
			str = new Value(str, Value.CURRENT).toValueString();
		}
		return str;
	}
}