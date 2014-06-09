package irt.data.components;

import irt.data.Menu;
import irt.data.dao.MenuDAO;
import irt.data.dao.SecondAndThirdDigitsDAO;
import irt.work.TextWork;

public class RF_PowerFET extends Isolator {
	
	private static final int FET = TextWork.FET;

	@Override
	public void setClassId(){
		setClassId(Component.CLASS_ID_NAME.get(FET));
	}

	@Override
	public int getPartNumberSize() {
		return 14;
	}

	@Override
	public Menu getPackages() {
		return new MenuDAO().getMenu("fet_type","description");
	}

	@Override
	public Menu getTypes() {
		return new MenuDAO().getMenu("fet_package","description");
	}

	@Override
	protected String getPartType(String partNumber) {
		String str = null;
		if(partNumber!=null && partNumber.length()>12)
			str = new SecondAndThirdDigitsDAO().getClassDescription(FET);
		return str;
	}

	@Override
	protected Menu getTitlesMenu() {
		return new MenuDAO().getMenu("fet_titles","sequence");
	}

	@Override
	public String getValue(int index) {
		String value = "";
		switch(index){
		case MANUFACTURE:
			getManufactureId();
			break;
		case BAND:
			value = getBand();
			break;
		case TYPE:
			value = getType();
			break;
		case PACKAGE:
			value = getPackage();
			break;
		case POWER:
			value = getPower();
			break;
		default:
			value = super.getValue(index);
		}
		
		return value;
	}

	private String getManufactureId() {
		return getValue(3, 5);
	}

	private String getManufactureIdQ() {
		return (getManufactureId().length()==2)
				? getManufactureId()
						: "??";
	}

	@Override
	protected String getBand() {
		return getValue(5, 8);
	}

	@Override
	protected String getType() {
		String returnStr = "";
		
		if (getPartNumber().length()==PART_NUMB_SIZE)
							returnStr = getPartNumber().substring(12);
		
		return returnStr.contains("?") ? "" : returnStr;
	}

	@Override
	protected String getPackage() {
		return getValue(10,12);
	}

	@Override
	protected String getPower() {
		return getValue(8,10);
	}

	@Override
	public boolean setValue(int index, String valueStr) {
		boolean isSetted = false;

		switch(index){
		case MANUFACTURE:
			setManufactureId(valueStr);
			isSetted = super.setValue(index, valueStr);
			break;
		case BAND:
			isSetted = setBand(valueStr);
			break;
		case TYPE:
			isSetted = setType(valueStr);
			break;
		case PACKAGE:
			isSetted = setPackage(valueStr);
			break;
		case POWER:
			isSetted = setPower(valueStr);
			break;
		default:
			isSetted = super.setValue(index, valueStr);
		}

		return isSetted;
	}

	private boolean setManufactureId(String manufId) {
		boolean isSetted = false;

		if(manufId!=null && manufId.length()==2){
			isSetted = true;
		}else
			manufId =  "??";

		setPartNumber(getClassId()+manufId+getBandQ()+getPoverQ()+getPackageQ()+getTypeQ());

		return isSetted;
	}

	private boolean setBand(String valueStr) {
		boolean isSetted = false;

		if(valueStr!=null && valueStr.length()==3){
			isSetted = true;
		}else
			valueStr =  "???";

		setPartNumber(getClassId()+getManufactureIdQ()+valueStr+getPoverQ()+getPackageQ()+getTypeQ());

		return isSetted;
	}

	private boolean setType(String valueStr) {
		boolean isSetted = false;
		
		if(valueStr!=null && valueStr.length()==2){
			isSetted = true;
		}else
			valueStr =  "??";

		setPartNumber(getClassId()+getManufactureIdQ()+getBandQ()+getPoverQ()+getPackageQ()+valueStr);

		return isSetted;
	}

	private boolean setPackage(String valueStr) {
		boolean isSetted = false;
		
		if(valueStr!=null && valueStr.length()==2){
			isSetted = true;
		}else
			valueStr =  "??";

		setPartNumber(getClassId()+getManufactureIdQ()+getBandQ()+getPoverQ()+valueStr+getTypeQ());

		return isSetted;
	}

	private boolean setPower(String valueStr) {
		boolean isSetted = false;

		valueStr = getInt(valueStr, 2);
		if (Integer.parseInt(valueStr) != 0) {
				isSetted = true;
		}else
			valueStr = "??" ;

		setPartNumber(getClassId()+getManufactureIdQ()+getBandQ()+valueStr+getPackageQ()+getTypeQ());
		return isSetted;
	}
}
