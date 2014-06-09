package irt.data.pcb;

import irt.data.Menu;
import irt.data.dao.ComponentDAO;
import irt.data.dao.MenuDAO;
import irt.data.metal.MetalParts;
import irt.work.TextWork;

public class Board extends MetalParts {

	private final static int P_N_SIZE =16;

	public final int MATERIAL	 	= 0;
	public final int TYPE		 	= 1;
	public final int REVISION	 	= 2;
	public final int DESCRIPTION	= 3;
	public final int ID		 		= 4;
	public final int QUANTITY 		= 5;
	public final int LOCATION 		= 6;
	public final int LINK 			= 7;
	public final int PART_NUMBER 	= 8;
	public final int NUMBER_OF_FIELDS= 9;

	@Override
	public void setClassId(){
		setClassId(TextWork.BOARD);
	}

	@Override
	public int getFieldsNumber() {
		return NUMBER_OF_FIELDS;
	}

	@Override
	public int getPartNumberSize() {
		return P_N_SIZE;
	}

	@Override
	public void setMenu() {
		if(getClassIdStr()==null || !getClassIdStr().equals(getClassId())){
			setF1Menu(new MenuDAO().getMenu("board_mat","description"));
			setF2Menu(new MenuDAO().getMenu("board_type","description"));
			setClassIdStr(getClassId());
			titlesMenu = new MenuDAO().getMenu("board_title", "sequence");
		}
	}

	@Override
	public String getSelectOptionHTML(int index) {

		String valueStr = "";
		Menu menu = null;

		switch (index) {

		case MATERIAL:
			valueStr = getMaterial();
			menu = getF1Menu();
			break;
		case TYPE:
			valueStr = getType();
			menu = getF2Menu();
			break;
		case REVISION:
			valueStr = getRevision();
			menu = new ComponentDAO().getRevisions(
					(getPartNumber().length()>PART_NUMB_SIZE-3) 
						? getPartNumber().substring(0,PART_NUMB_SIZE-3)
								: getPartNumber());
		}
		return getOptionHTML((menu!=null)?menu.getKeys():null, (menu!=null)?menu.getDescriptions():null, valueStr);
	}

	@Override
	public String getValue(int index){
		String returnStr = "";
		
		switch(index){
		case MATERIAL:
			returnStr = getMaterial();
			break;
		case ID:
			returnStr = getID();
			break;
		case TYPE:
			returnStr = getType();
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
		case MATERIAL:
			isSetted = setMaterial(valueStr);
			break;
		case TYPE:
			isSetted = setType(valueStr);
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
			isSetted = super.setValue(super.DESCRIPTION, valueStr);
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
	public String getNewID() {
		return new ComponentDAO().getNewBoardID();
	}

	@Override
	protected boolean setMPId() {
		boolean isSetted = false;
		String tmpStr = getClassID();

		if(isSet()){
			if(getID().isEmpty()){
				tmpStr += TextWork.addZeroInFront(getNewID(), 4);
				setPartNumber(tmpStr+getMaterialQ()+getTypeQ()+getRevisionQ());
			}
			isSetted = true;
		}else
			resetId();

		return isSetted;
	}

	@Override
	protected void setMPId(String id) {
		String tmpStr = getClassID();
		setPartNumber(tmpStr+"????"+getMaterialQ()+getTypeQ()+getRevisionQ());
	}

	private String getMaterial() {
		return getValue(7, 9);
	}

	private String getMaterialQ() {
		return (getMaterial().length()!=0)
				? getMaterial()
						: "??";
	}

	private boolean setMaterial(String valueStr) {
		boolean isSetted = false;

		if(valueStr!=null && valueStr.length()==2){
			isSetted = true;
			if(!getMaterial().equals(valueStr))
				setPartNumber(getClassID()+"????"+valueStr+getTypeQ()+getRevisionQ());
		}
		else
			setPartNumber(getClassID()+getID()+getIDQ()+"??"+getTypeQ()+getRevisionQ());

		return isSetted;
	}

	private String getType() {
		return getValue(9, 13);
	}

	private String getTypeQ() {
		return (getType().length()!=0)
				? getType()
						: "????";
	}

	private boolean setType(String valueStr) {
		boolean isSetted = false;
		
		if(valueStr!=null && valueStr.length()==4){
			isSetted = true;
			if(!getType().equals(valueStr))
				setPartNumber(getClassID()+"????"+getMaterialQ()+valueStr+getRevisionQ());
		}else
			setPartNumber(getClassID()+getIDQ()+getMaterialQ()+"????"+getRevisionQ());

		return isSetted;
	}

	private String getClassID() {
		return (getClassId().length()==1) ? getClassId()+"??" : getClassId();
	}

	@Override
	protected boolean setRevision(String valueStr) {
		boolean isSetted = false;
		String tmpStr = getClassID();
		tmpStr += getIDQ()+getMaterialQ()+getTypeQ();
		
		if(valueStr!=null && valueStr.length()==3){
			isSetted = true;
			if(!getRevision().equals(valueStr))
				setPartNumber(tmpStr+valueStr);
		}
		else
			setPartNumber(tmpStr+"???");

		return isSetted;
	}
}
