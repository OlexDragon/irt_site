package irt.data.components;

import irt.data.Menu;
import irt.data.dao.MenuDAO;
import irt.data.dao.SecondAndThirdDigitsDAO;
import irt.work.InputTitles;
import irt.work.TextWork;

import java.text.DecimalFormat;

public class Resistor extends Component {

	private static final int RESISTOR = TextWork.RESISTOR;
	public final int VALUE = 0;
	public final int PRECISION = 1;
	public final int SIZE = 2;
	public final int DESCRIPTION = 3;
	public final int MANUFACTURE = 4;
	public final int MAN_PART_NUM = 5;
	public final int QUANTITY = 6;
	public final int LOCATION = 7;
	public final int LINK = 8;
	public final int PART_NUMBER =9;
	public final int NUMBER_OF_FIELDS= 10;

	private static Menu precisionMenu;
	private static Menu sizeMenu;

	@Override
	public int getFieldsNumber() {
		return NUMBER_OF_FIELDS;
	}
	
	@Override
	public void setClassId(){
		setClassId(Component.CLASS_ID_NAME.get(RESISTOR));
	}

	@Override
	public int getPartNumberSize() {
		return 13;
	}

	@Override
	public void setTitles() {
		setTitles(new InputTitles(
				new String[] 
						{ "Value(Ohm)","Precision(%)",	"Size",	"<br />Description",	"Mfr",	"Mfr P/N" },
					new String[]
						{ "text",	"select",		"select",	"text",				"select",		"text"}));
	}

	@Override
	public void setMenu() {
		if(precisionMenu==null){
			precisionMenu = new MenuDAO().getMenu("prec_res","description");
		}

		if(sizeMenu==null){
			sizeMenu = new MenuDAO().getMenu("size_res","description");
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
			
		case PRECISION:
			tmp = precisionMenu.getKeys();
			toShow = precisionMenu.getDescriptions();
			valueStr = getPrecision();
			break;
			
		case SIZE:
			tmp = sizeMenu.getKeys();
			toShow = sizeMenu.getDescriptions();
			valueStr = getSize();
		}
		
		return getOptionHTML(tmp, toShow, valueStr);
	}

	@Override
	public String getValue(int index){
		String returnStr = "";

		switch(index){
		case VALUE:
			returnStr = getValue().isEmpty() ? "" : new  Value(getValue(), TextWork.RESISTOR).toValueString();
			break;
		case PRECISION:
			returnStr = getPrecision();
			break;
		case SIZE:
			returnStr = getSize();
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
		case VALUE:
			isSetted = setValue(valueStr);
			break;
		case PRECISION:
			isSetted = setPrecision(valueStr);
			break;
		case SIZE:
			isSetted = setSize(valueStr);
			break;
		case PART_NUMBER:
			if(valueStr!=null
					&& valueStr.length()==14){
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

	@Override
	protected String getValue(String partNumber) {
		String prefix = null;
		String[] beforeEafter;
		double output = 0;

		if(partNumber!=null && partNumber.length()>8){
			partNumber = partNumber.substring(3,8);
			beforeEafter = partNumber.split("E");
			beforeEafter[0] = beforeEafter[0];
			if(beforeEafter.length>1){
				if(!beforeEafter[1].equals("A"))
					beforeEafter[0] = String.format("%-"+(Integer.parseInt(beforeEafter[1])+4)+"s", beforeEafter[0]).replaceAll(" ", "0");
				long value = Long.parseLong(beforeEafter[0]);
				if (value >= 1000000000) {
					prefix = "M";
					output = value / 1000000000.0;
				} else if (value >= 1000000) {
					output = value / 1000000.0;
					prefix = "K";
				} else {
					output = value / 1000.0;
					prefix = "R";
				}
			}
		}
		return new DecimalFormat("#.###").format(output)+prefix;
	}

	@Override
	public String getValue() {
		return getValue(3, 8);
	}

	private String getValueQ() {
		return (getValue().length()!=0)
							? getValue()
									: "?????";
	}
	
	private boolean setValue(String valueStr) {
		boolean isSetted = false;
		
		if(valueStr!=null && !valueStr.isEmpty()){
			Value value = new Value(valueStr, TextWork.RESISTOR);
			valueStr = value.toString();
			setPartNumber(getClassId()+valueStr+getPrecisionQ()+getSizeQ());
			setDbValue(value.toValueString());
			isSetted = true;
		}else
			setPartNumber(getClassId()+"?????"+getPrecisionQ()+getSizeQ());
		
		return isSetted;
	}

	private String getSize() {
		String returnStr = "";
		
		if (getPartNumber().length()==PART_NUMB_SIZE)
			returnStr = getPartNumber().substring(9);

		return (!returnStr.contains("?"))
				? returnStr
						: "";
	}

	private String getSizeQ() {
		return (getSize().length()!=0)
				? getSize()
						: "????";
	}

	private boolean setSize(String valueStr) {
		boolean isSetted = false;
		
		if(valueStr!=null && !valueStr.isEmpty() && !valueStr.equals("Select") && valueStr.length()==4){
			setPartNumber(getClassId()+getValueQ()+getPrecisionQ()+valueStr);
			isSetted = true;
		}else
			setPartNumber(getClassId()+getValueQ()+getPrecisionQ()+"????");
		
		return isSetted;
	}

	private String getPrecision() {
		return getValue(8, 9);
	}

	private String getPrecisionQ() {
		return (getPrecision().length()!=0)
				? getPrecision()
						: "?";
	}

	private boolean setPrecision(String valueStr) {
		boolean isSetted = false;
		
		if(valueStr!=null && valueStr.length()==1){
			setPartNumber(getClassId()+getValueQ()+valueStr+getSizeQ());
			isSetted = true;
		}else
			setPartNumber(getClassId()+getValueQ()+"?"+getSizeQ());
		
		return isSetted;
	}

	@Override
	protected String getPartType(String partNumber) {
		String str = null;
		if(partNumber!=null && partNumber.length()>12)
			str = new SecondAndThirdDigitsDAO().getClassDescription(RESISTOR);
		str += "\\\\"+getDbValue().replaceAll("[\\d. ]", "");
		return str;
	}
}