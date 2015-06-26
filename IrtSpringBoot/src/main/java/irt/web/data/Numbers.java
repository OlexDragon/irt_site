package irt.web.data;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Numbers {

	private static final Logger logger = LogManager.getLogger();

	public static String numberToExponential(String toConvert, int size){

		if(toConvert==null || (toConvert = toConvert.trim().toUpperCase().replaceFirst("^0+(?!$)", "")).isEmpty() || !toConvert.matches(".*\\d+.*"))
			toConvert = size == 4 ? "ERR " : "ERROR";

		else{
			if(toConvert.matches("[0-9]+")){
				toConvert = integerToExponential(toConvert, size);
			}else{
				toConvert = getNumberWithStringToExponential(toConvert, size);
			}
				
		}
		return toConvert;
	}

	private static String integerToExponential(String toConvert, int size) {

		String result;
		int length = toConvert.length();
		switch(length){
		case 1:
		case 2:
			result = stringFormat(toConvert, size, --length);
			break;
		case 3:
			if(size == 5)
				result = stringFormat(toConvert, size, --length);
			else
				result = mathFormat(toConvert, size);
			break;
		default:
			result = mathFormat(toConvert, size);
		}

		return result;
	}

	private static String mathFormat(String toConvert, int size) {
		logger.entry(toConvert, size);

		int length = toConvert.length();
		int exp = length - size + 2;
		Long parseInt = Math.round(Integer.parseInt(toConvert)/(Math.pow(10, exp)));
		return stringFormat(parseInt.toString(), size, length-1);
	}

	public static String stringFormat(String toConvert, int size, int exp) {
		return String.format("%1$-"+ (size-2) +"s", toConvert).replaceAll(" ", "0") + 'E' + exp;
	}

	private static String getNumberWithStringToExponential(String toConvert, int size) {
		if(toConvert.contains("E")){
			String[] split = toConvert.split("E");
		}
		return null;
	}
}
