package irt.data.components;

import irt.data.Menu;
import irt.data.dao.ComponentDAO;
import irt.data.dao.MenuDAO;
import irt.data.dao.SecondAndThirdDigitsDAO;
import irt.work.InputTitles;
import irt.work.TextWork;

public class PowerSupply extends Component {

	private static final int POWER_SUPPLY = TextWork.POWER_SUPPLY;
	public final int TYPE 			= 0;
	public final int INPUT 			= 1;
	public final int OUTPUT 		= 2;
	public final int OUTPUT_VOLTAGE	= 3;
	public final int PACKAGE 		= 4;
	public final int ID			 	= 5;
	public final int DESCRIPTION 	= 6;
	public final int MANUFACTURE 	= 7;
	public final int MAN_PART_NUM	= 8;
	public final int LINK 			= 9;
	public final int QUANTITY 		= 10;
	public final int LOCATION 		= 11;
	public final int PART_NUMBER 	= 12;
	public final int NUMBER_OF_FIELDS= 13;

	@Override
	public int getFieldsNumber() {
		return NUMBER_OF_FIELDS;
	}

	private static Menu packMenu;
	private static Menu typeMenu;
	private static Menu inputMenu;
	private static Menu outputsMenu;
	private static Menu outVMenu;

	@Override
	public void setClassId(){
		setClassId(Component.CLASS_ID_NAME.get(POWER_SUPPLY));
	}

	@Override
	public int getPartNumberSize() {
		return 13;
	}

	@Override
	public void setTitles() {
		setTitles(new InputTitles(
				new String[]{"Type",	"Input",	"Outputs",	"Out.V",	"Package",	"Seq.N", "<br />Description",	"Mfr",	"Mfr P/N" },
				new String[]{ "select", "select", 	"select",	"select", 	"select", 	"label",		"text", 			"Select",		"text" }));
	}

	@Override
	public void setMenu() {
		if(packMenu==null)
			packMenu = new MenuDAO().getMenu("ps_package","description");
		if(typeMenu==null)
			typeMenu = new MenuDAO().getMenu("ps_type","description");
		if(inputMenu==null)
			inputMenu = new MenuDAO().getMenu("ps_input","description");
		if(outputsMenu==null)
			outputsMenu = new MenuDAO().getMenu("ps_outputs","description");
		if(outVMenu==null)
			outVMenu = new MenuDAO().getMenu("ps_out_v","sequence");
	}

	@Override
	public String getSelectOptionHTML(int index) {
		String[] tmp = null;
		String[] toShow = null;
		String valueStr = "";
		Menu menu = null;

		switch (index) {
		case MANUFACTURE:
			return getManufactureOptionHTML();

		case PACKAGE:
			menu = packMenu;
			valueStr = getPackage();
			break;
		case TYPE:
			menu = typeMenu;
			valueStr = getType();
			break;
		case INPUT:
			menu = inputMenu;
			valueStr = getInput();
			break;
		case OUTPUT:
			menu = outputsMenu;
			valueStr = getOutput();
			break;
		case OUTPUT_VOLTAGE:
			menu = outVMenu;
			valueStr = getOutV();
		}

		toShow = menu.getDescriptions();
		tmp = menu.getKeys();

		return getOptionHTML(tmp, toShow, valueStr);
	}

	@Override
	public String getValue(int index){
		String returnStr = "";
		
		switch(index){
		case TYPE:
			returnStr = getType();
			break;
		case INPUT:
			returnStr = getInput();
			break;
		case OUTPUT:
			returnStr = getOutput();
			break;
		case PACKAGE:
			returnStr = getPackage();
			break;
		case ID:
			returnStr = getID();
			break;
		case MAN_PART_NUM:
			returnStr = getManufPartNumber();
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
		boolean isSetted = false;
		switch(index){
		case TYPE:
			isSetted = setType(valueStr);
			break;
		case INPUT:
			isSetted = setInput(valueStr);
			break;
		case OUTPUT:
			isSetted = setOutput(valueStr);
			break;
		case OUTPUT_VOLTAGE:
			isSetted = setOutV(valueStr);
			break;
		case PACKAGE:
			isSetted = setPackage(valueStr);
			break;
		case ID:
			isSetted = setComponentId();
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
		case LINK:
			isSetted = super.setValue(super.LINK, valueStr);
		}
		
		return isSetted;
	}

	private String getType() {
		return getValue(3,5);
	}

	private String getTypeQ() {
		return (getType().length()!=0)
				? getType()
						: "??";
	}

	private boolean setType(String valueStr) {
			boolean isSetted = false;
			
			if(valueStr!=null && valueStr.length()==2){
				isSetted = true;
				if(!getType().equals(valueStr)){
					resetId();
					setPartNumber(getClassId()+valueStr+getInutQ()+getOutputQ()+getOutVQ()+getPackageQ()+getIdQ());
				}
			}else
				setPartNumber(getClassId()+"??"+getInutQ()+getOutputQ()+getOutVQ()+getPackageQ()+getIdQ());

			return isSetted;
		}

	private String getInput() {
		return getValue(5,7);
	}

	private String getInutQ() {
		return (getInput().length()!=0)
				? getInput()
						: "??";
	}

	private boolean setInput(String valueStr) {
		boolean isSetted = false;
		
		if(valueStr!=null && valueStr.length()==2){
			isSetted = true;
			if(!getInput().equals(valueStr)){
				resetId();
				setPartNumber(getClassId()+getTypeQ()+valueStr+getOutputQ()+getOutVQ()+getPackageQ()+getIdQ());
			}
		}else
			setPartNumber(getClassId()+getTypeQ()+"??"+getOutputQ()+getOutVQ()+getPackageQ()+getIdQ());

		return isSetted;
	}

	private String getOutput() {
		return getValue(7,8);
	}

	private String getOutputQ() {
		return (getOutput().length()!=0)
				? getOutput()
						: "?";
	}

	private boolean setOutput(String valueStr) {
		boolean isSetted = false;
		
		if(valueStr!=null && valueStr.length()==1){
			isSetted = true;
			if(!getOutput().equals(valueStr)){
				resetId();
				setPartNumber(getClassId()+getTypeQ()+getInutQ()+valueStr+getOutVQ()+getPackageQ()+getIdQ());
			}
		}else
			setPartNumber(getClassId()+getTypeQ()+getInutQ()+"?"+getOutVQ()+getPackageQ()+getIdQ());

		return isSetted;
	}

	private String getOutV() {
		return getValue(8,9);
	}

	private String getOutVQ() {
		return (getOutV().length()!=0)
				? getOutV()
						: "?";
	}

	private boolean setOutV(String valueStr) {
		boolean isSetted = false;
		
		if(valueStr!=null && valueStr.length()==1){
			isSetted = true;
			if(!getOutV().equals(valueStr)){
				resetId();
				setPartNumber(getClassId()+getTypeQ()+getInutQ()+getOutputQ()+valueStr+getPackageQ()+getIdQ());
			}
		}else
			setPartNumber(getClassId()+getTypeQ()+getInutQ()+getOutputQ()+"?"+getPackageQ()+getIdQ());

		return isSetted;
	}

	private String getPackage() {
		return getValue(9,11);
	}

	private String getPackageQ() {
		return (getPackage().length()!=0)
				? getPackage()
						: "??";
	}

	private boolean setPackage(String valueStr) {
		boolean isSetted = false;

		if(valueStr!=null && valueStr.length()==2){
			isSetted = true;
			if(!getPackage().equals(valueStr)){
				resetId();
				setPartNumber(getClassId()+getTypeQ()+getInutQ()+getOutputQ()+getOutVQ()+valueStr+getIdQ());
			}
		}else
			setPartNumber(getClassId()+getTypeQ()+getInutQ()+getOutputQ()+getOutVQ()+"??"+getIdQ());

		return isSetted;
	}

	private String getID() {
		String returnStr = "";
		if(getPartNumber().length()==PART_NUMB_SIZE)
			returnStr = getPartNumber().substring(11);
		
		return (!returnStr.contains("?"))
								? returnStr
										: "";
	}

	private String getIdQ() {
		return (getID().length()!=0)
				? getID()
						: "??";
	}

	private void resetId() {
		setPartNumber(getClassId()+getTypeQ()+getInutQ()+getOutputQ()+getOutVQ()+getPackageQ()+"??");
	}

	private boolean setComponentId() {
		boolean isSetted = false;
		String tmpStr = getClassId()+getTypeQ()+getInutQ()+getOutputQ()+getOutVQ()+getPackageQ();

		if(isSet()){
			if(getID().isEmpty()){
				tmpStr += TextWork.addZeroInFront(new ComponentDAO().getPSNewID(), 2); 
				setPartNumber(tmpStr);
			}
			isSetted = true;
		}else{
			tmpStr += "??";
			setPartNumber(tmpStr);
		}

		return isSetted;
	}

	@Override
	public boolean isSet() {
		return 		!getType().isEmpty() 
				&&	!getInput().isEmpty()
				&&	!getOutput().isEmpty()
				&&	!getPackage().isEmpty();
	}

	@Override
	protected String getPartType(String partNumber) {
		String str = null;
		if(partNumber!=null && partNumber.length()>12)
			str = new SecondAndThirdDigitsDAO().getClassDescription(POWER_SUPPLY);
		return str;
	}
}