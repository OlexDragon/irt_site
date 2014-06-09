package irt.data.top;

import irt.data.Menu;
import irt.data.components.Component;
import irt.data.dao.ComponentDAO;
import irt.data.dao.MenuDAO;
import irt.work.InputTitles;
import irt.work.TextWork;

public class TopLevel extends Component {

	private final static int P_N_SIZE =17;

	public final int BAND		 	= 0;
	public final int POWER		 	= 1;
	public final int PACKAGE		= 2;
	public final int DEVICE 		= 3;
	public final int CONFIG 		= 4;
	public final int REVISION 		= 5;
	public final int DESCRIPTION	= 6;
	public final int QUANTITY 		= 7;
	public final int LOCATION 		= 8;
	public final int LINK 			= 9;
	public final int PART_NUMBER 	= 10;
	public final int NUMBER_OF_FIELDS=11;

	private Menu titlesMenu;
	private Menu freqMenu;
	private Menu packMenu;
	private Menu confMenu;
	private Menu deviceMenu;

	//Product Type - CN (Converter); PA (SSPA); BC (BUC); RS (Redundant System) etc
	//F1F1 Start Frequency; F2F2 Stop Frequency
	//Power in dBm
	//Package Type - HM (HubMount); RM (Rack Mount), BR(Brick);  etc
	//configuration
	//Revision Level


	@Override
	public void setClassId(){
		setClassId(TextWork.TOP);
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
	public void setTitles(){
		setTitles( new InputTitles(titlesMenu.getKeys(),titlesMenu.getDescriptions()));
	}

	@Override
	public void setMenu() {
		MenuDAO menuDAO = new MenuDAO();

		titlesMenu = menuDAO.getMenu("top_titles", "sequence");

		setFreqMenuDAO(menuDAO);

		packMenu = menuDAO.getMenu("top_pack","description");
		deviceMenu = menuDAO.getMenu("top_device", "description");
		confMenu = menuDAO.getMenu("top_config","description");
	}

	protected void setFreqMenuDAO(MenuDAO menuDAO) {
		freqMenu = menuDAO.getMenu("band","description");
		freqMenu.add(menuDAO.getMenu("band_uc","description"));
		freqMenu.add(menuDAO.getMenu("band_buc","description"));
		freqMenu.add(menuDAO.getMenu("band_dc","description"));
	}

	@Override
	public String getSelectOptionHTML(int index) {

		String valueStr = "";
		Menu menu = null;

		switch (index) {

		case BAND:
			valueStr = getBand();
			menu = freqMenu;
			break;
		case PACKAGE:
			valueStr = getPackage();
			menu = packMenu;
			break;
		case DEVICE:
			valueStr = getDevice();
			menu = deviceMenu;
			break;
		case CONFIG:
			valueStr = getConfiguration();
			menu = confMenu;
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
		case BAND:
			returnStr = getBand();
			break;
		case POWER:
			returnStr = getPower();
			break;
		case PACKAGE:
			returnStr = getPackage();
			break;
		case DEVICE:
			returnStr = getDevice();
			break;
		case CONFIG:
			returnStr = getConfiguration();
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
		case BAND:
			isSetted = setBand(valueStr);
			break;
		case POWER:
			isSetted = setPower(valueStr);
			break;
		case PACKAGE:
			isSetted = setPackage(valueStr);
			break;
		case DEVICE:
			isSetted = setDevice(valueStr);
			break;
		case CONFIG:
			isSetted = setConfiguration(valueStr);
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
			super.setValue(super.DESCRIPTION, valueStr);
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

	private String getBand() {
		return getValue(3, 6);
	}

	private String getBandQ() {
		return (getBand().length()!=0)
				? getBand()
						: "???";
	}

	private boolean setBand(String valueStr) {
		boolean isSetted = false;
		String tmpStr = getClassIdQ();
		
		if(valueStr!=null && valueStr.length()==3){
			tmpStr += valueStr;
			isSetted = true;
		}
		else
			tmpStr += "???";
		
		setPartNumber(tmpStr+getPowerQ()+getPackageQ()+getDeviceQ()+getConfigurationQ()+getRevisionQ());
		
		return isSetted;
	}

	private String getPower() {
		return getValue(6, 10);
	}

	private String getPowerQ() {
		return (getPower().length()!=0)
				? getPower()
						: "????";
	}

	private boolean setPower(String valueStr) {
		boolean isSetted = false;
		String tmpStr = getClassIdQ();
		tmpStr += getBandQ();

		valueStr = getInt(valueStr, 4);
		if (Integer.parseInt(valueStr) != 0) {
				isSetted = true;
		}else
			valueStr = "????" ;
		
		tmpStr += valueStr;
		setPartNumber(tmpStr+getPackageQ()+getDeviceQ()+getConfigurationQ()+getRevisionQ());
		
		return isSetted;
	}

	private String getPackage() {
		return getValue(10, 12);
	}

	private String getPackageQ() {
		return (getPackage().length()!=0)
				? getPackage()
						: "??";
	}

	private boolean setPackage(String valueStr) {
		boolean isSetted = false;
		String tmpStr = getClassIdQ();
		tmpStr += getBandQ()+getPowerQ();

		if(valueStr!=null && valueStr.length()==2){
			tmpStr += valueStr;
			isSetted = true;
		}
		else
			tmpStr += "??";
		
		setPartNumber(tmpStr+getDeviceQ()+getConfigurationQ()+getRevisionQ());
		
		return isSetted;
	}

	private String getDevice() {
		return getValue(12, 13);
	}

	private String getDeviceQ() {
		return (getDevice().length()!=0)
				? getDevice()
						: "?";
	}

	private boolean setDevice(String valueStr) {
		boolean isSetted = false;
		String tmpStr = getClassIdQ();
		tmpStr += getBandQ()+getPowerQ()+getPackageQ();

		if(valueStr!=null && valueStr.length()==1){
			tmpStr += valueStr;
			isSetted = true;
		}
		else
			tmpStr += "?";
		
		setPartNumber(tmpStr+getConfigurationQ()+getRevisionQ());
		
		return isSetted;
	}

	private String getConfiguration() {
		return getValue(13, 14);
	}

	private String getConfigurationQ() {
		return (getConfiguration().length()!=0)
				? getConfiguration()
						: "?";
	}

	private boolean setConfiguration(String valueStr) {
		boolean isSetted = false;
		String tmpStr = getClassIdQ();
		tmpStr += getBandQ()+getPowerQ()+getPackageQ()+getDeviceQ();

		if(valueStr!=null && valueStr.length()==1){
			tmpStr += valueStr;
			isSetted = true;
		}
		else
			tmpStr += "?";
		
		setPartNumber(tmpStr+getRevisionQ());
		
		return isSetted;
	}

	private String getRevision() {
		String returnStr = "";
		
		if (getPartNumber().length()==PART_NUMB_SIZE)
							returnStr = getPartNumber().substring(14);
		
		return (!returnStr.contains("?"))
				? returnStr
						: "";
	}

	private String getRevisionQ() {
		return (getRevision().length()!=0)
				? getRevision()
						: "???";
	}

	private boolean setRevision(String valueStr) {
		boolean isSetted = false;
		String tmpStr = getClassIdQ();
		tmpStr += getBandQ()+getPowerQ()+getPackageQ()+getDeviceQ()+getConfigurationQ();

		if(valueStr!=null && valueStr.length()==3){
			tmpStr += valueStr;
			isSetted = true;
		}
		else
			tmpStr += "???";
		
		setPartNumber(tmpStr);
		
		return isSetted;
	}

	private String getClassIdQ() {
		return getClassId().length()==1 ? getClassId()+"??" : getClassId();
	}

	@Override
	public String getPartNumberF() {
		return TextWork.getPartNumber(getPartNumber(), 3, 10, 14, PART_NUMB_SIZE);
	}

	protected void setFreqMenu(Menu menu) {
		freqMenu = menu;
	}
}
