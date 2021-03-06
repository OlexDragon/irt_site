package irt.data.components;

import irt.data.Menu;
import irt.data.dao.MenuDAO;
import irt.data.dao.MenuDAO.OrderBy;
import irt.data.dao.SecondAndThirdDigitsDAO;
import irt.work.TextWorker;

import java.text.DecimalFormat;

public class Resistor extends Component {

	private static final int RESISTOR = TextWorker.RESISTOR;
	public static final int VALUE 		= 0;
	public static final int PRECISION 	= 1;
	public static final int SIZE 		= 2;
	private static final int SHIFT 		= 3;
	public static final int DESCRIPTION		= Data.DESCRIPTION		+SHIFT;
	public static final int MANUFACTURE 	= Data.MANUFACTURE		+SHIFT;
	public static final int MAN_PART_NUM 	= Data.MAN_PART_NUM		+SHIFT;
	public static final int QUANTITY 		= Data.QUANTITY			+SHIFT;
	public static final int LOCATION 		= Data.LOCATION			+SHIFT;
	public static final int LINK 			= Data.LINK				+SHIFT;
	public static final int PART_NUMBER 	= Data.PART_NUMBER		+SHIFT;
	public static final int NUMBER_OF_FIELDS= Data.NUMBER_OF_FIELDS	+SHIFT;

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
	protected String getDatabaseNameForTitles() {
		return "resistor_titles";
	}

	@Override
	public void setMenu() {
		super.setMenu();
		if(precisionMenu==null){
			precisionMenu = new MenuDAO().getMenu("prec_res", OrderBy.DESCRIPTION);
		}

		if(sizeMenu==null){
			sizeMenu = new MenuDAO().getMenu("size_res", OrderBy.DESCRIPTION);
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
		logger.entry(index);
		String returnStr = "";

		switch(index){
		case VALUE:
			returnStr = getValue().isEmpty() ? "" : new  Value(getValue(), TextWorker.RESISTOR).toValueString();
			break;
		case PRECISION:
			returnStr = getPrecision();
			break;
		case SIZE:
			returnStr = getSize();
			break;
		default:
			returnStr = super.getValue(index-SHIFT);
		}
		
		return logger.exit(returnStr);
	}

	@Override
	public boolean setValue(int index, String valueStr){
		boolean isSet = false;
		
		switch(index){
		case VALUE:
			isSet = setValue(valueStr);
			break;
		case PRECISION:
			isSet = setPrecision(valueStr);
			break;
		case SIZE:
			isSet = setSize(valueStr);
			break;
		default:
			isSet = super.setValue(index-SHIFT, valueStr);
		}
		
		return isSet;
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
			Value value = new Value(valueStr, TextWorker.RESISTOR);
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