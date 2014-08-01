package irt.data.metal;

import irt.data.Menu;
import irt.data.components.Component;
import irt.data.dao.ComponentDAO;
import irt.data.dao.MenuDAO;
import irt.data.dao.MenuDAO.OrderBy;
import irt.work.InputTitles;
import irt.work.TextWorker;
import irt.work.TextWorker.PartNumberFirstChar;

public class MetalParts extends Component {

	private final static int P_N_SIZE =14;

	public final int FINISH1	 	= 0;
	public final int FINISH2	 	= 1;
	public final int OPTION		 	= 2;
	public final int REVISION	 	= 3;
	public final int DESCRIPTION	= 4;
	public final int ID		 		= 5;
	public final int QUANTITY 		= 6;
	public final int LOCATION 		= 7;
	public final int LINK 			= 8;
	public final int PART_NUMBER 	= 9;
	public final int NUMBER_OF_FIELDS= 10;
	@Override
	public int getFieldsNumber() {
		return NUMBER_OF_FIELDS;
	}

	protected static Menu titlesMenu;
	private static Menu f1Menu;
	private static Menu f2Menu;
	private static String classIdStr;

	@Override
	public void setClassId(){
		setClassId(""+PartNumberFirstChar.METAL_PARTS.getFirstDigit().getFirstChar());
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
		if(getClassIdStr()==null || !getClassIdStr().equals(getClassId())){
			titlesMenu = new MenuDAO().getMenu("metal_titles", OrderBy.SEQUENCE);
			f1Menu = new MenuDAO().getMenu("metal_f1", OrderBy.DESCRIPTION);
			f2Menu = new MenuDAO().getMenu("metal_f2", OrderBy.DESCRIPTION);
			setClassIdStr(getClassId());
		}
	}

	@Override
	public String getSelectOptionHTML(int index) {

		String valueStr = "";
		Menu menu = null;

		switch (index) {

		case FINISH1:
			valueStr = getFinish1();
			menu = f1Menu;
			break;
		case FINISH2:
			valueStr = getFinish2();
			menu = f2Menu;
			break;
		case OPTION:
			valueStr = getOption();
			menu = new ComponentDAO().getMetalPartsOptions(getPartNumber());
			break;
		case REVISION:
			valueStr = getRevision();
			menu = new ComponentDAO().getRevisions((getPartNumber().length()>9) ? getPartNumber().substring(0, 9) : getPartNumber());
		}
		return getOptionHTML((menu!=null)?menu.getKeys():null, (menu!=null)?menu.getDescriptions():null, valueStr);
	}

	@Override
	public String getValue(int index){
		String returnStr = "";
		
		switch(index){
		case FINISH1:
			returnStr = getFinish1();
			break;
		case FINISH2:
			returnStr = getFinish2();
			break;
		case ID:
			returnStr = getID();
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
		boolean isSetted = false;

		switch(index){
		case ID:
			isSetted = setMPId();
			break;
		case FINISH1:
			isSetted = setFinish1(valueStr);
			break;
		case FINISH2:
			isSetted = setFinish2(valueStr);
			break;
		case OPTION:
			isSetted = setOption(valueStr);
			break;
		case REVISION:
			isSetted = setRevision(valueStr);
			break;
		case PART_NUMBER:
			if(valueStr!=null
					&& valueStr.length()==PART_NUMB_SIZE){
				isSetted = true;
				setPartNumber(valueStr);
			}
			break;
		case DESCRIPTION:
			isSetted = valueStr!=null && !valueStr.isEmpty();
			super.setValue(Component.DESCRIPTION, valueStr);
			break;
		case QUANTITY:
			isSetted = super.setValue(Component.QUANTITY, valueStr);
			break;
		case LOCATION:
			isSetted = super.setValue(Component.LOCATION, valueStr);
			break;
		case LINK:
			isSetted = super.setValue(Component.LINK, valueStr);
		}
		
		return isSetted;
	}

	protected String getID() {
		String returnStr = "";
		
		if(getPartNumber().length()>7)
			returnStr = getPartNumber().substring(3,7);
		
		return (!returnStr.contains("?"))
								? returnStr
										: "";
	}

	protected String getIDQ() {
		return (getID().length()!=0)
				? getID()
						: "????";
	}

	protected boolean setMPId() {
		boolean isSetted = false;

		if(isSet()){
			if(getID().isEmpty())
				setMPId(String.format("%4s", getNewID()).replace(' ', '0'));
			isSetted = true;
		}else
			resetId();

		return isSetted;
	}

	protected String getNewID() {
		return new ComponentDAO().getNewMetalID();
	}

	protected void resetId() {
		setMPId("????");
	}

	protected void setMPId(String id) {
		setPartNumber(get()+id+getFinish1Q()+getFinish2Q()+getOptionQ()+getRevisionQ());
	}

	private String getFinish1() {
		return getValue(7, 8);
	}

	private String getFinish1Q() {
		return (getFinish1().length()!=0)
				? getFinish1()
						: "?";
	}

	private boolean setFinish1(String valueStr) {
		boolean isSetted = false;
		
		if(valueStr!=null && valueStr.length()==1){
			isSetted = true;
			if(!getFinish1().equals(valueStr)){
				resetId();
				setPartNumber(get()+getIDQ()+valueStr+getFinish2Q()+getOptionQ()+getRevisionQ());
			}
		}
		else		
			setPartNumber(get()+getIDQ()+"?"+getFinish2Q()+getOptionQ()+getRevisionQ());
		
		return isSetted;
	}

	private String getFinish2() {
		return getValue(8, 9);
	}

	private String getFinish2Q() {
		return (getFinish2().length()!=0)
				? getFinish2()
						: "?";
	}

	private boolean setFinish2(String valueStr) {
		boolean isSetted = false;
		
		if(valueStr!=null && valueStr.length()==1){
			isSetted = true;
			if(!getFinish2().equals(valueStr)){
				resetId();
				setPartNumber(get()+getIDQ()+getFinish1Q()+valueStr+getOptionQ()+getRevisionQ());
			}
		}else
			setPartNumber(get()+getIDQ()+getFinish1Q()+"?"+getOptionQ()+getRevisionQ());
		
		return isSetted;
	}

	private String getOption() {
		return getValue(9,11);
	}

	protected String getOptionQ() {
		return (getOption().length()!=0)
				? getOption()
						: "??";
	}

	private boolean setOption(String valueStr) {
		boolean isSetted = false;

		if(valueStr!=null && valueStr.length()==2){
			isSetted = true;
			setPartNumber(get()+getIDQ()+getFinish1Q()+getFinish2Q()+valueStr+getRevisionQ());
		}else
			setPartNumber(get()+getIDQ()+getFinish1Q()+getFinish2Q()+"??"+getRevisionQ());

		return isSetted;
	}

	protected String get() {
		return getClassId()+"??";
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
				setPartNumber(get()+getIDQ()+getFinish1Q()+getFinish2Q()+getOptionQ()+valueStr);
		}else
			setPartNumber(get()+getIDQ()+getFinish1Q()+getFinish2Q()+getOptionQ()+"???");
		
		return isSetted;
	}

	@Override
	public boolean isSet() {
		return 		TextWorker.isValid(getFinish1()) 
				&&	TextWorker.isValid(getFinish2())
				&&	!getOption().isEmpty()
				&&	!getDescription().isEmpty()
				&&	TextWorker.isValid(getRevision());
	}

	public static Menu getF1Menu() {
		return f1Menu;
	}

	public static void setF1Menu(Menu f1Menu) {
		MetalParts.f1Menu = f1Menu;
	}

	public static Menu getF2Menu() {
		return f2Menu;
	}

	public static void setF2Menu(Menu f2Menu) {
		MetalParts.f2Menu = f2Menu;
	}

	protected static void setClassIdStr(String classIdStr) {
		MetalParts.classIdStr = classIdStr;
	}

	protected static String getClassIdStr() {
		return classIdStr;
	}

}
