package irt.work;

import irt.data.Error;
import irt.data.FirstDigit;
import irt.data.dao.FirstDigitDAO;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class TextWorker {

	private static final FirstDigitDAO FIRST_DIGIT_DAO = new FirstDigitDAO();

	public static final int	COUNT_COMPONENTS		= 1,
							COUNT_POWER_SUPPLIES	= 2,
							COUNT_METAL_PARTS		= 3,
							COUNT_PCB				= 4,
							COUNT_HARNESS 			= 5,
							COUNT_CABLE				= 6,
							COUNT_SCREW 			= 7,
							COUNT_NUTS	 			= 7,
							COUNT_GASKET			= 8,
							COUNT_PLUGS			= 9;

	public enum PartNumberFirstChar{
		COMPONENT	(FIRST_DIGIT_DAO.get(1)),//"0";
		ASSEMBLIES	(FIRST_DIGIT_DAO.get(2)),//"A";
		METAL_PARTS	(FIRST_DIGIT_DAO.get(3)),//"M";
		BOARD		(FIRST_DIGIT_DAO.get(4)),//"P";
		RAW_MATERIAL(FIRST_DIGIT_DAO.get(5)),//"R";
		SCREWS		(FIRST_DIGIT_DAO.get(6)),//"S";
		TOP			(FIRST_DIGIT_DAO.get(7)),//"T";
		PLASTIC		(FIRST_DIGIT_DAO.get(8));//"L";
		
		private FirstDigit firstDigit;

		public FirstDigit getFirstDigit() {
			return firstDigit;
		}

		private PartNumberFirstChar(FirstDigit firstDigit){
			this.firstDigit = firstDigit;
		}

		public static PartNumberFirstChar valueOf(int id){
			PartNumberFirstChar firstChar = null;

			for(PartNumberFirstChar fd:values())
				if(fd.getFirstDigit().getId() == id){
					firstChar = fd;
					break;
				}

			return firstChar;
		}

		public static PartNumberFirstChar valueOf(char partNumberFirstChar){
			PartNumberFirstChar firstChar = null;

			for(PartNumberFirstChar fd:PartNumberFirstChar.values())
				if(fd.getFirstDigit().getFirstChar() == partNumberFirstChar){
					firstChar = fd;
					break;
				}

			return firstChar;
		}
	}

//	public static final String COMPONENT		= ""+FIRST_DIGIT_DAO.get(1).getFirstChar();//"0";
	public static final int	OTHER			= 1;
	public static final int	AMPLIFIER 		= 2;
	public static final int	CAPACITOR		= 3;
	public static final int	DIODE			= 4;
	public static final int	FAN				= 5;
	public static final int	INDUCTOR		= 6;
	public static final int	RESISTOR		= 7;
	public static final int TRANSISTOR		= 8;
	public static final int	CONNECTOR		= 17;
	public static final int	IC_NON_RF		= 25;
	public static final int	ISOLATOR		= 26;
	public static final int	MICROCONTROLLER	= 28;
	public static final int	POWER_SUPPLY	= 30;
	public static final int	FET				= 31;
	public static final int	IC_RF			= 32;
	public static final int VARICAP			= 38;
	public static final int VOLTAGE_REGULATOR= 39;
	public static final int WIRE_HARNESS 	= 48;
	public static final int CABLES 			= 49;
	public static final int GASKET			= 55;

	public static final int POWER 			= 1050;
	public static final int CURRENT 		= 1051;
	public static final int VOLTAGE 		= 1052;
	public static final int LENGTH_SM		= 1053;
	public static final int NEMBER 			= 1054;

	public static final int	PLASTIC_PLARTS	= 56;

	//	public static final String ASSEMBLIES 			= ""+FIRST_DIGIT_DAO.get(2).getFirstChar();//"A";
	public static final int CARRIER_ASSEMBLIES 	= 11;
	public static final int COVER_ASSEMBLIES 	= 53;
	public static final int ENCLOSURE_ASSEMBLIES = 22;
	public static final int KIT_ASSEMBLIES 		= 27;
	public static final int SWALL_ASSEMBLIES 	= 52;
	public static final int WG_ASSEMBLIES 		= 40;

//	public static final String METAL_PARTS	= ""+FIRST_DIGIT_DAO.get(3).getFirstChar();//"M";
	public static final int BRACKET					= 10;
	public static final int CARRIER					= 12;
	public static final int COVER					= 20;
	public static final int DEVICE_BLOK				= 21;
	public static final int ENCLOSURE 				= 23;
	public static final int HEATING					= 24;
	public static final int SHEET_METAL_BACKET 		= 34;
	public static final int SHEET_METAL_FLAT 		= 35;
	public static final int SHEET_METAL_ENCLOSURE	= 37;
	public static final int WAVEGUIDE				= 41;
	public static final int WALLS					= 42;
	
//	public static final String BOARD 			= ""+FIRST_DIGIT_DAO.get(4).getFirstChar();//"P";
	public static final int POPULATED_BOARD		= 13;
	public static final int BARE_BOARD 			= 14;
	public static final int BOOTLOADER_LOADED_BOARD= 16;
	public static final int SOFT_LOADED_BOARD	= 18;
	public static final int SCHEMATIC 			= 36;
	public static final int GERBER				= 44; 
	public static final int PROJECT 			= 50;

//	public static final String RAW_MATERIAL = ""+FIRST_DIGIT_DAO.get(5).getFirstChar();//"R";
	public static final int WIRE 			= 57;

//	public static final String SCREWS = ""+FIRST_DIGIT_DAO.get(6).getFirstChar();//"S";
	public static final int SCREW 	= 45;
	public static final int WASHER 	= 46;
	public static final int SPACER 	= 47;
	public static final int NUT 	= 51;
	public static final int SCR_OTHER=54;

//	public static final String TOP				= ""+FIRST_DIGIT_DAO.get(7).getFirstChar();//"T"
	public static final int		UP_CONVERTER	= 19;
	public static final int		DOWN_CONVERTER	= 15;
	public static final int		FREQUENCY_CONVERTER	= 9;
	public static final int		REDUNDANT		= 33;
	public static final int		SSPA			= 29;
	public static final int		SSPB			= 43;


	public static final String[] SIZE = { "0201","0402","0603","0805",
		"1008","1206","1806","1812","2010","2512","2920", "EIA","SMT","THRU Hole"};
	public static final String CAP_TYPE = "cap_type";
	public static final String[] CAP_PACKAGE = { "SMT", "Radial"};
	public static final String[] PRECISION = { "1", "3", "5" };
	public static final String[] PACKAGE = {"LFCSP_VQ", "SMT", "CJ725", "FV1206", "SOT89"};

	private static Error error = new Error();

	public static boolean isDigit(String value) {
		boolean isDigit = true;
		
		if( value != null && value.length()!=0){
			for(int i=0; i<value.length(); i++)
				if(!Character.isDigit(value.charAt(i))){
					isDigit = false;
					break;
				}
		}
		else
			isDigit = false;
		
		return isDigit;
	}

	public static String getPartNumber(String partNumber, int first, int second, int third, int length) {
		if(partNumber.length()>length)
			partNumber = partNumber.substring(0,length);
		
		String returnStr = "";
		if (partNumber.length() <= first)//first three digits
			returnStr = partNumber;
		else {
			returnStr = partNumber.substring(0, first);
			returnStr += '-';
			if (partNumber.length() <= second)//next six digits(3+6=9)
				returnStr += partNumber.substring(first);
			else {
				returnStr += partNumber.substring(first, second);
				returnStr += '-';
				if (partNumber.length() <= third)
					returnStr += partNumber.substring(second);
				else {
					returnStr += partNumber.substring(second, third);
					returnStr += '-';
					returnStr += partNumber.substring(third);
				}
			}
		}

		return returnStr;
	}

	public static String[] getFirstLetter(String[] capType) {
		String[] firstLetters = new String[capType.length];
		int index = 0;
		
		if(capType!=null)
			for(String type:capType)
				firstLetters[index++] =""+type.charAt(0);
				
		return firstLetters;
	}

	public static String addZeroInFront(String str, int minLength) {

		if (str != null) {
			StringBuffer sb = new StringBuffer(str);
			while(minLength>sb.length())
				sb.insert(0, '0');
			str = sb.toString();
		}

		return str;
	}

	public static String getByFirstChar(char firstChar, String[] arrayStr){
		String returnStr = "";
		
		for(int i=0 ;i<arrayStr.length; i++){
			if(arrayStr[i].charAt(0) == firstChar){
				returnStr = arrayStr[i];
				break;
			}
		}
		
		return returnStr;
	}

	public static String getFirst(int numberOfChar, String str) {
		return (str!=null && numberOfChar<str.length())
				? str.substring(0,numberOfChar)
						: str;
	}

	public static String[] getFirst(int numberOfChar, String[] arrayStr) {
		String[] returnArray = new String[arrayStr.length];
		
		for (int i = 0; i < arrayStr.length; i++) {
			returnArray[i] = (arrayStr[i] != null && arrayStr[i].length() > numberOfChar) 
												? arrayStr[i].substring(0, numberOfChar) 
														: arrayStr[i];
		}
		
		return returnArray;
	}

	public static String pnValidation(String partNumberStr) {
		String returnStr = "";

		if (partNumberStr != null)
			for (char ch : partNumberStr.toCharArray())
				if (ch != '-')
					returnStr += (ch != '*') ? (ch != '?') ? ch : '_' : '%';

		if (returnStr.length() <= 3)
			returnStr += '%';

		return returnStr;
	}

	public static boolean isValid(String str){
		boolean bool = true;
		
		if(str != null && !str.isEmpty()){
			for(char ch:str.toCharArray())
				if(!((ch>='0' && ch<='9') || (ch>='A' && ch<='z'))){
					if(!(ch=='?' || ch =='*' || ch=='_' || ch=='%'))
						error.setErrorMessage("Input data is not correct");
					
					bool = false;
					break;
				}
		}else
			bool = false;

		return bool;
	}

	public static String addToEnd(String classId, char c, int length) {
		if(classId==null)
			classId = "";
		else if (classId.length()>length)
			classId = classId.substring(0,length);

		for(int i=0; i<length-classId.length(); i++)
			classId += '_';
			
		return classId;
	}

	public static int toInt(String valueStr) {
		int returnInt = 0;
		
		if( valueStr != null && !valueStr.isEmpty()){

			String tmpStr = "";

			for(int i=0; i<valueStr.length(); i++)
				if(Character.isDigit(valueStr.charAt(i))){
					tmpStr += valueStr.charAt(i);
				}

			if(!tmpStr.isEmpty())
				returnInt = Integer.parseInt(tmpStr);
		}

		return returnInt;
	}

	public static String getButton(String buttonName) {
		return "<input type=\"submit\" name=\""+buttonName+"\" id=\""+buttonName+"\" value=\""+buttonName+"\" />";
	}

	public static String getTextField(String fieldName, String value) {
		return "<input type=\"text\" name=\""+fieldName+"\" id=\""+fieldName+"\" value=\""+value+"\" />";
	}

	public static String getPasswordField(String fieldName, String value) {
		return "<input type=\"password\" name=\""+fieldName+"\" id=\""+fieldName+"\" value=\""+value+"\" />";
	}

	/**
	 * 
	 * @param string
	 * @return First letters(ex. CH788MK -> CH)
	 */
	public static String getFirstLetters(String string) {
		String returnStr = "";
		for(char ch:string.toCharArray())
			if(!Character.isDigit(ch))
				returnStr += ch;
			else
				break;
		return returnStr;
	}

	public static String arrayToString(Object[] array) {
		String str = null;
		for(Object o:array)
			if(str!=null)
				str += ","+o.toString();
			else
				str = o.toString();
		return str;
	}

	public static boolean validateEMail(String eMail) {
		boolean isValid = false;
		if(eMail!=null && !eMail.isEmpty()){
			String[] split = eMail.split("@");
			if(split.length==2 && split[0].length()>0){
				split = split[1].split("\\.");
				if(split.length==2 && split[0].length()>0 && split[1].length()>0)
					isValid = true;
			}
		}
		return isValid ;
	}

	public static String getYearMonth(){
		return new SimpleDateFormat("yyMM").format(Calendar.getInstance().getTime());
	}
}
