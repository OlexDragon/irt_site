package irt.data.components;

import irt.work.TextWork;

import java.text.DecimalFormat;

public class Value {

	public static final int VOLTAGE 	= TextWork.VOLTAGE;
	public static final int RESISTOR 	= TextWork.RESISTOR;
	public static final int CAPACITOR 	= TextWork.CAPACITOR;
	public static final int INDUCTOR 	= TextWork.INDUCTOR;
	public static final int POWER 		= TextWork.POWER;
	public static final int CURRENT 	= TextWork.CURRENT;
	public static final int LENGTH_SM 	= TextWork.LENGTH_SM;
	public static final int NUMBER 		= TextWork.NEMBER;

	private int componentType;
	private long value;

	public Value(String valueStr, int componentType){
		if(valueStr==null || valueStr.replaceAll("\\D", "").isEmpty()){
			value = -1;
			return;
		}
		this.componentType = componentType;
		valueStr = valueStr.toUpperCase();
		int multiplier = getMultiplier(valueStr);
		valueStr = valueStr.replaceAll("[^\\d.AE]", "");
		String[] valueSplit;
		if(componentType!=VOLTAGE && multiplier==1 && valueStr.contains("E")){
			boolean containsA = valueStr.contains("A");
			valueSplit = valueStr.replaceAll("[^\\dE]", "").split("E");
			if(valueSplit.length>1 && valueSplit[1].length()>1)
				valueSplit[1] = valueSplit[1].substring(0,1);
			if(valueSplit[0].isEmpty())
				value = 0;
			else
				value = (long) (Integer.parseInt(valueSplit[0])*(containsA ? 1 : 10*(valueSplit.length>1 ? Math.pow(10, Integer.parseInt(valueSplit[1])) : 1)));
		}else{
			valueStr = valueStr.replaceAll("[^\\d.]", "");
			valueSplit = valueStr.split("\\.");
			double decimal = 0;
			if(valueSplit.length>1 && !valueSplit[1].isEmpty())
				decimal = Double.parseDouble("."+valueSplit[1]);

			int valueInt;
			if(valueSplit[0].isEmpty())
				valueInt = 0;
			else
				valueInt = Integer.parseInt(valueSplit[0]);
			decimal = valueInt+decimal;
			value = Math.round(decimal*(componentType==RESISTOR ? 1000 : 100)*multiplier);
		}
	}

	private int getLength(int componentType) {
		int length;
		switch(componentType){
		case RESISTOR:
			length = 3;
			break;
		default:
			length = 2;
		}
		return length;
	}

	private int getMultiplier(String valueStr) {
		valueStr = valueStr.replaceAll("[\\d. ]", "");
		return valueStr.isEmpty() ? 1 :
					valueStr.charAt(0)=='K' ||
						(valueStr.charAt(0)=='N' && componentType==CAPACITOR) ||
							(valueStr.charAt(0)=='U' && componentType==INDUCTOR) ||
							(valueStr.charAt(0)=='M' && componentType==LENGTH_SM) ||
								valueStr.charAt(0)=='A' ?
					1000 : 
						(valueStr.charAt(0)=='M' && componentType==RESISTOR) ||
							(valueStr.charAt(0)=='U' && componentType==CAPACITOR)||
								(valueStr.charAt(0)=='M' && (componentType==INDUCTOR || componentType==LENGTH_SM)) ?
						1000000 :
							1;
	}

	public String toValueString() {
		String letter = "";
		int divider = componentType==RESISTOR ? 1000000000 : 100000000;

		if (value >= divider) {
			switch(componentType){
			case CAPACITOR:
				letter = "uF";
				break;
			case RESISTOR:
				letter = "M";
				break;
			case INDUCTOR:
				letter = "mH";
				break;
			case POWER:
				letter = "MW";
				break;
			case CURRENT:
				letter = "KA";
				break;
			case VOLTAGE:
				letter = "MV";
			}
		} else if (value >= (divider/=1000)) {
			switch(componentType){
			case CAPACITOR:
				letter = "nF";
				break;
			case RESISTOR:
				letter = "K";
				break;
			case INDUCTOR:
				letter = "uH";
				break;
			case POWER:
				letter = "KW";
				break;
			case CURRENT:
				letter = "A";
				break;
			case VOLTAGE:
				letter = "KV";
				break;
			case LENGTH_SM:
				letter = "m";
			}
		} else {
			divider /= 1000;
			switch (componentType) {
			case CAPACITOR:
				letter = "pF";
				break;
			case RESISTOR:
				letter = "R";
				break;
			case INDUCTOR:
				letter = "nH";
				break;
			case POWER:
				letter = "W";
				break;
			case CURRENT:
				letter = "mA";
				break;
			case VOLTAGE:
				letter = "V";
				break;
			case LENGTH_SM:
				letter = "cm";
			}
		}

		DecimalFormat df;
		if (componentType == RESISTOR)
			df = new DecimalFormat("0.###");
		else
			df = new DecimalFormat("0.##");

		return df.format((double)value/divider) + letter;
	}

	@Override
	public String toString() {
		String returnStr = "";

		if (componentType != VOLTAGE) {
			int length = getLength(componentType);
			String tmpStr = "" + value;

			if (tmpStr.length() <= length) {
				if (tmpStr.length() >= 1
						&& tmpStr.charAt(tmpStr.length() - 1) != '0') {
					returnStr += String.format("%" + length + "s", tmpStr)
							.replaceAll(" ", "0") + "EA";
				} else {
					tmpStr = "" + value / 10;
					returnStr += String.format("%" + length + "s", tmpStr)
							.replaceAll(" ", "0") + "E0";
				}

			} else {
				tmpStr = "" + value / 10;
				returnStr = tmpStr.substring(0, length) + 'E'
						+ (tmpStr.length() - length);
			}
		}else{
			if(value>0)
				returnStr = String.format("%3s",getIntValue()).replaceAll(" ", "0");
		}
		return returnStr;
	}

	public int getIntValue() {
		return (int) (value/(componentType==RESISTOR ? 1000 : 100));
	}

	public String getIntStr(int minNumberOfDijits) {
		return String.format("%"+minNumberOfDijits+"s", getIntValue()).replaceAll(" ", "0");
	}
}

