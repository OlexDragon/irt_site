package irt.data.pcb;

import irt.data.Menu;
import irt.data.components.Component;
import irt.data.dao.ComponentDAO;
import irt.work.InputTitles;
import irt.work.TextWork;

public class Schematic extends Board {

	public final int FROM		 	= 0;
	public final int QUANTITY 		= 1;
	public final int LOCATION 		= 2;
	public final int LINK 			= 3;
	public final int PART_NUMBER 	= 4;
	public final int NUMBER_OF_FIELDS= 5;

	@Override
	public int getFieldsNumber() {
		return NUMBER_OF_FIELDS;
	}

	@Override
	public void setClassId(){
		setClassId(Component.CLASS_ID_NAME.get(TextWork.SCHEMATIC));
	}

	@Override
	public void setTitles(){
		setTitles( new InputTitles(	new String[] 
				{ "For Assempled Board"},
			new String[]
				{ "select"}));
	}

	@Override
	public void setMenu() {
		if(getClassIdStr()==null || !getClassIdStr().equals(getClassId())){
			setF1Menu(new ComponentDAO().getByClassId("PCA", 13));//TODO
			setClassIdStr(getClassId());
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
		case FROM:
			isSet = setFrom(valueStr);
			break;
		case PART_NUMBER:
			if(valueStr!=null
					&& valueStr.length()==PART_NUMB_SIZE){
			isSet = true;
				setPartNumber(valueStr);
			}
			break;
		case QUANTITY:
			isSet = super.setValue(super.QUANTITY, valueStr);
			break;
		case LOCATION:
			isSet = super.setValue(super.LOCATION, valueStr);
			break;
		case LINK:
			isSet = super.setValue(super.LINK, valueStr);
		}

		return isSet;
	}

	private String getFrom() {
		return getPartNumber().substring(3);
	}

	private boolean setFrom(String valueStr) {
		boolean isSet = false;
		if(valueStr!=null && valueStr.length()==13 && !valueStr.equalsIgnoreCase("Select")){
			isSet = true;
			setDescription(new ComponentDAO().getDescription("PCA"+valueStr));
		}else
			valueStr = "?????????????";

		setPartNumber(getClassId()+valueStr);

		return isSet;
	}

	@Override
	public boolean isSet() {
		return true;
	}
}
