package irt.data.assemblies;

import irt.data.Menu;
import irt.data.components.Component;
import irt.data.dao.ComponentDAO;
import irt.data.dao.MenuDAO;
import irt.work.InputTitles;
import irt.work.TextWork;

public class Assemblies extends Component {

	private static final int P_N_SIZE = 12;//TODO

	public final int TOP_LEVEL	 	= 0;
	public final int OPTION		 	= 1;
	public final int REVISION	 	= 2;
	public final int DESCRIPTION	= 3;
	public final int QUANTITY 		= 4;
	public final int LOCATION 		= 5;
	public final int LINK 			= 6;
	public final int PART_NUMBER 	= 7;
	public final int NUMBER_OF_FIELDS= 8;

	@Override
	public int getFieldsNumber() {
		return NUMBER_OF_FIELDS;
	}

	private static String classIdStr;
	private static Menu titlesMenu;
	private static Menu topMenu;

	@Override
	public void setClassId(){
		setClassId(TextWork.ASSEMBLIES);
	}

	@Override
	public int getPartNumberSize() {
		return P_N_SIZE;
	}

	@Override
	public void setTitles() {
		setTitles(new InputTitles( titlesMenu.getKeys(),titlesMenu.getDescriptions()));
	}

	@Override
	public void setMenu() {
		if(classIdStr==null || !classIdStr.equals(getClassId())){
			titlesMenu = new MenuDAO().getMenu("assem_titles", "sequence");
			topMenu = new MenuDAO().getTopComponentsMenu("assemblied");
			classIdStr = getClassId();
		}
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
		return getOptionHTML((menu!=null)?menu.getKeys():null, (menu!=null)?menu.getDescriptions():null, valueStr);
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
		case PART_NUMBER:
			if(valueStr!=null
					&& valueStr.length()==PART_NUMB_SIZE){
				isSet = true;
				setPartNumber(valueStr);
			}
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
			isSet
			= super.setValue(super.LINK, valueStr);
		}

		return isSet;
	}

	private String getTopLevel() {
		return getValue(3, 8);
	}

	private String getTopLevelQ() {
		return (getTopLevel().length()!=0)
				? getTopLevel()
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
		return getClassId().length()==1 ? getClassId()+"??" : getClassId();
	}

	private String getOption() {
		return getValue(8, 9);
	}

	private String getOptionQ() {
		return (getOption().length()!=0)
				? getOption()
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
