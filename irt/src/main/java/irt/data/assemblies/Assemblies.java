package irt.data.assemblies;

import irt.data.Menu;
import irt.data.components.Component;
import irt.data.components.Data;
import irt.data.dao.ComponentDAO;
import irt.data.dao.MenuDAO;
import irt.work.TextWorker.PartNumberFirstChar;

public class Assemblies extends Component {

	private static final int P_N_SIZE = 12;//TODO

	public static final int TOP_LEVEL	= 0;
	public static final int OPTION		= 1;
	public static final int REVISION	= 2;
	private static final int SHIFT 		= 3;
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

	private static String classIdStr;
	private static Menu topMenu;

	@Override
	public void setClassId(){
		setClassId(""+PartNumberFirstChar.ASSEMBLIES.getFirstDigit().getFirstChar());
	}

	@Override
	public int getPartNumberSize() {
		return P_N_SIZE;
	}

	@Override
	public void setMenu() {
		super.setMenu();
		if(classIdStr==null || !classIdStr.equals(getClassId())){
			classIdStr = getClassId();
		}
	}

	@Override
	protected String getDatabaseNameForTitles() {
		return "assem_titles";
	}

	@Override
	public String getSelectOptionHTML(int index) {

		String valueStr = "";
		Menu menu = null;

		switch (index) {

		case TOP_LEVEL:
			valueStr = getTopLevel();
			menu = topMenu;
			break;
		case OPTION:
			valueStr = getOption();
			menu = new MenuDAO().getAssembliedOptions(getPartNumber());
			break;
		case REVISION:
			valueStr = getRevision();
			menu = new ComponentDAO().getRevisions(getPartNumber());
		}

		String[] keys;
		String[] descriptions;
		if(menu!=null){
			keys = menu.getKeys();
			descriptions = menu.getDescriptions();
		}else{
			keys = null;
			descriptions = null;
		}

		return getOptionHTML(keys, descriptions, valueStr);
	}

	@Override
	public String getValue(int index){
		String returnStr = "";
		
		switch(index){
		case TOP_LEVEL:
			returnStr = getTopLevel();
			break;
		case OPTION:
			returnStr = getOption();
			break;
		case REVISION:
			returnStr = getRevision();
			break;
		default:
			returnStr = super.getValue(index-SHIFT);
		}
		
		return returnStr;
	}

	@Override
	public boolean setValue(int index, String valueStr) {
		boolean isSet = false;

		switch(index){
		case TOP_LEVEL:
			isSet = setTopLevel(valueStr);
			break;
		case OPTION:
			isSet = setOption(valueStr);
			break;
		case REVISION:
			isSet = setRevision(valueStr);
			break;
		default:
			isSet = super.setValue(index-SHIFT, valueStr);
		}

		return isSet;
	}

	private String getTopLevel() {
		return getValue(3, 8);
	}

	private String getTopLevelQ() {
		String topLevel = getTopLevel();
		return (topLevel.length()!=0)
				? topLevel
						: "?????";
	}

	private boolean setTopLevel(String valueStr) {
		boolean isSetted = false;
		
		if(valueStr!=null && valueStr.length()==5){
			setPartNumber(get()+valueStr+getOptionQ()+getRevisionQ());
			isSetted = true;
		}else
			setPartNumber(get()+"?????"+getOptionQ()+getRevisionQ());
		
		return isSetted;
	}

	protected String get() {
		String classId = getClassId();
		return classId.length()==1 ? classId+"??" : classId;
	}

	private String getOption() {
		return getValue(8, 9);
	}

	private String getOptionQ() {
		String option2 = getOption();
		return (option2.length()!=0)
				? option2
						: "?";
	}

	private boolean setOption(String valueStr) {
		boolean isSetted = false;
		
		if(valueStr!=null && valueStr.length()==1){
			setPartNumber(get()+getTopLevelQ()+valueStr+getRevisionQ());
			isSetted = true;
		}else		
			setPartNumber(get()+getTopLevelQ()+"?"+getOptionQ()+getRevisionQ());
		
		return isSetted;
	}

	protected String getRevision() {
		String returnStr = "";

		if(getPartNumber().length()==PART_NUMB_SIZE)
			returnStr = getPartNumber().substring(PART_NUMB_SIZE-3);

		return (!returnStr.contains("?"))
								? returnStr
										: "";
	}

	protected String getRevisionQ() {
		return (getRevision().length()!=0)
				? getRevision()
						: "???";
	}

	protected boolean setRevision(String valueStr) {
		boolean isSetted = false;

		if(valueStr!=null && valueStr.length()==3){
			isSetted = true;
			if(!getRevision().equals(valueStr))
				setPartNumber(get()+getTopLevelQ()+getOptionQ()+valueStr);
		}else
			setPartNumber(get()+getTopLevelQ()+getOptionQ()+"???");
		
		return isSetted;
	}

	@Override
	public boolean isSet() {
		return super.isSet() && !getDescription().isEmpty();
	}
}
