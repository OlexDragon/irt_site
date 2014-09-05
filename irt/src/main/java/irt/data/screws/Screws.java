package irt.data.screws;

import irt.data.Menu;
import irt.data.components.Component;
import irt.data.dao.ComponentDAO;
import irt.data.dao.MenuDAO;
import irt.data.dao.MenuDAO.OrderBy;
import irt.work.InputTitles;
import irt.work.TextWorker;
import irt.work.TextWorker.PartNumberFirstChar;

public class Screws extends Component {

	public final int SCR_NUMBER		= 0;
	public final int SEQ_NUMBER		= 1;
	public final int DESCRIPTION	= 2;
	public final int MANUFACTURE 	= 3;
	public final int MAN_PART_NUM 	= 4;
	public final int QUANTITY 		= 5;
	public final int LOCATION 		= 6;
	public final int LINK 			= 7;
	public final int PART_NUMBER 	= 8;
	public final int NUMBER_OF_FIELDS= 9;
	@Override
	public int getFieldsNumber() {
		return NUMBER_OF_FIELDS;
	}

	private static Menu titlesMenu;
	private static Menu scrNumberMenu;

	@Override
	public void setClassId(){
		setClassId(""+PartNumberFirstChar.SCREWS.getFirstDigit().getFirstChar());
	}

	@Override
	public void setTitles() {
		setTitles(new InputTitles( titlesMenu.getKeys(),titlesMenu.getDescriptions()));
	}

	@Override
	public void setMenu() {
		if(titlesMenu==null){
			titlesMenu 		= new MenuDAO().getMenu(new String[]{"scr_titles","titles"}, OrderBy.SEQUENCE);
			scrNumberMenu 	= new MenuDAO().getMenu("scr_number", OrderBy.SEQUENCE);
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
			
		case SCR_NUMBER:
			toShow = scrNumberMenu.getDescriptions();
			tmp = scrNumberMenu.getKeys();
			valueStr = getScrNumber();
			break;
		}

		return getOptionHTML(tmp, toShow, valueStr);
	}

	@Override
	public String getValue(int index){
		String returnStr = "";
		
		switch(index){
		case SCR_NUMBER:
			returnStr = getScrNumber();
			break;
		case SEQ_NUMBER:
			returnStr = getSeqNumber();
			break;
			
		case MAN_PART_NUM:
			returnStr = getMfrPN();
			break;
		case MANUFACTURE:
			returnStr = super.getValue(Component.MANUFACTURE);
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
		boolean isSet = true ;

		switch(index){
		case SCR_NUMBER:
			if(valueStr!= null && !valueStr.equals(getScrNumber())){
				resetSequentialNunber();
				setScrNumber(valueStr);
			}break;
		case SEQ_NUMBER:
			setSeqNumber(valueStr);
			break;

		case PART_NUMBER:
				setPartNumber(valueStr);
			break;
		case MAN_PART_NUM:
			setManufPartNumber(valueStr);
			break;
		case MANUFACTURE:
			setManufId(valueStr);
			break;
		case DESCRIPTION:
			setDescription(valueStr);
			break;
		case QUANTITY:
			isSet = super.setValue(Component.QUANTITY, valueStr);
			break;
		case LOCATION:
			isSet = super.setValue(Component.LOCATION, valueStr);
			break;
		case LINK:
			isSet = super.setValue(Component.LINK, valueStr);
			break;
		default:
			isSet = false;
		}
		
		return isSet;
	}
	@Override
	public int getPartNumberSize() {
		return 9;
	}

	@Override
	public void resetSequentialNunber() {
		setPartNumber(get()+getScrNumberQ()+"???");
	}

	private String getSeqNumber() {
		String returnStr = "";
		
		if(getPartNumber().length()==PART_NUMB_SIZE)
			returnStr = getPartNumber().substring(6);
		
		return (!returnStr.contains("?"))
								? returnStr
										: "";
	}

	private String getSeqNumberQ() {
		return (getSeqNumber().length()!=0)
				? getSeqNumber()
						: "???";
	}

	private boolean setSeqNumber(String valueStr) {

		boolean isSet = isSetSeqNumber();
		if(isSet){
			if(getSeqNumber().isEmpty()){
				setPartNumber(get()+getScrNumberQ() + String.format("%3s", new ComponentDAO().getNewSequentialNumber(TextWorker.COUNT_SCREW)).replaceAll(" ", "0"));
			}
		}else
			resetSequentialNunber();

		return isSet;
	}

	public boolean isSetSeqNumber() {
		String pn = getPartNumber();
		return pn!=null && pn.length()>6 ? !getPartNumber().substring(0,6).contains("?") : false;
	}

	private String getScrNumber() {
		return getValue(3, 6);
	}

	private String getScrNumberQ() {
		return (getScrNumber().length()!=0)
				? getScrNumber()
						: "???";
	}

	private void setScrNumber(String valueStr) {
		if(valueStr==null || valueStr.length()!=3)
			valueStr = "???";
		
		setPartNumber(get()+valueStr+getSeqNumberQ());
	}

	protected String get() {
		return getClassId()+"??";
	}
}