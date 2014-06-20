package irt.data.pcb;

import irt.data.Menu;
import irt.data.components.Component;
import irt.data.dao.ComponentDAO;
import irt.data.dao.MenuDAO;
import irt.data.dao.MenuDAO.OrderBy;
import irt.work.TextWorker;

public class PopulatedBoard extends Board {

	public final int FROM		 	= 0;
	public final int DETAILS	 	= 1;
	public final int REVISION	 	= 2;
	public final int DESCRIPTION 	= 3;
	public final int QUANTITY 		= 4;
	public final int LOCATION 		= 5;
	public final int LINK 			= 6;
	public final int PART_NUMBER 	= 7;
	public final int NUMBER_OF_FIELDS= 8;

	private Menu detailsMenu;

	@Override
	public int getFieldsNumber() {
		return NUMBER_OF_FIELDS;
	}

	@Override
	public void setClassId(){
		setClassId(Component.CLASS_ID_NAME.get(TextWorker.POPULATED_BOARD));
	}

	@Override
	public void setMenu() {
		if(getClassIdStr()==null || !getClassIdStr().equals(getClassId())){
			setF1Menu(new ComponentDAO().getByClassId("PCB", 6));//TODO
			setClassIdStr(getClassId());
			titlesMenu = new MenuDAO().getMenu("pcb_as_title", OrderBy.SEQUENCE);
		}

		if(detailsMenu==null){
			MenuDAO dao = new MenuDAO();
			detailsMenu = dao.getMenu("details", OrderBy.DESCRIPTION);
			if(detailsMenu!=null)
				detailsMenu.add(dao.getMenu("board_type", OrderBy.DESCRIPTION));
		}
	}

	@Override
	public String getSelectOptionHTML(int index) {

		String valueStr = "";
		Menu menu = null;

		switch (index) {

		case FROM:
			valueStr = getFrom();
			menu = getF1Menu();
			break;
		case DETAILS:
			valueStr = getDetails();
			menu = detailsMenu;
			break;
		case REVISION:
			return super.getSelectOptionHTML(super.REVISION);
		}
		return getOptionHTML((menu!=null)?menu.getKeys():null, (menu!=null)?menu.getDescriptions():null, valueStr);
	}

	@Override
	public String getValue(int index){
		String returnStr = "";
		
		switch(index){
		case FROM:
			returnStr = getFrom();
			break;
		case DETAILS:
			returnStr = getDetails();
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
		boolean isSet = true;
		switch(index){
		case FROM:
			isSet = setFrom(valueStr);
			break;
		case DETAILS:
			isSet = setDetails(valueStr);
			break;
		case REVISION:
			isSet = super.setValue(super.REVISION, valueStr);
			break;
		case PART_NUMBER:
			if(valueStr!=null && valueStr.length()==PART_NUMB_SIZE){
				setPartNumber(valueStr);
			}
			break;
		case DESCRIPTION:
			setDescription(valueStr);
			break;
		case QUANTITY:
			isSet = super.setValue(super.QUANTITY, valueStr);
			break;
		case LOCATION:
			isSet = super.setValue(super.LOCATION, valueStr);
			break;
		case LINK:
			isSet = super.setValue(super.LINK, valueStr);
			break;
		default:
			isSet = false;
		}

		return isSet;
	}

	private String getFrom() {
		return getValue(3, 9);
	}

	private String getFromQ() {
		return (getFrom().length()!=0)
				? getFrom()
						: "??????";
	}

	private boolean setFrom( String valueStr) {
		boolean isSet = false;
		if(valueStr!=null && valueStr.length()==6 && !valueStr.equals("Select")){
			setPartNumber(getClassId()+valueStr+getDetailsQ()+getRevisionQ());
			isSet = true;
		}
		return isSet;
	}

	private String getDetails() {
		return getValue(9, 13);
	}

	private String getDetailsQ() {
		return (getDetails().length()!=0)
				? getDetails()
						: "????";
	}

	private boolean setDetails(String valueStr) {
		boolean isSet = false;
		if(valueStr!=null && valueStr.length()==4){
			setPartNumber(getClassId()+getFromQ()+valueStr+getRevisionQ());
			isSet = true;
		}

		return isSet;
	}
}