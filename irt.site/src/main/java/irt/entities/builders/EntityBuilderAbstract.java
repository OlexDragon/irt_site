
package irt.entities.builders;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import irt.controllers.components.PartNumberForm;
import irt.entities.CountsRepository;

public abstract class EntityBuilderAbstract implements EntityBuilder {

	protected final Logger logger = LogManager.getLogger(getClass());

	public final int FIRST_CHAR_ID;
	public final char FIRST_PARTNUMBER_CHAR;
	public final String NEXT_TWO_CHARS;

	public final int MFR_INDEX, MFR_PN_INDEX,  DESCRIPTION_INDEX, FIELDS_COUNT, VALUE_TO_PN_BASE_LENGTH;

	protected EntityBuilderAbstract(int firstPartnumberCharId, char firstPartnumberChar, String nextTwoPartnumberChars, int mfrIndex, int mfrPnIndex, int descriptionIndex, int fieldsCount, int valueToPnBaseLength) {

		FIRST_PARTNUMBER_CHAR = firstPartnumberChar;
		this.NEXT_TWO_CHARS = nextTwoPartnumberChars;
		this.FIRST_CHAR_ID = firstPartnumberCharId;
		this.MFR_INDEX = mfrIndex;
		this.MFR_PN_INDEX = mfrPnIndex;
		this.DESCRIPTION_INDEX = descriptionIndex;
		this.FIELDS_COUNT = fieldsCount;
		VALUE_TO_PN_BASE_LENGTH = valueToPnBaseLength;
	}


	public String toPartNumber(String value) {

		String exp;
		if(value!=null && value.matches(".*\\d+.*")){
			value = value.toUpperCase();
			if(value.contains("E")){
				String[] split = value.split( "E", 2);
				split[0] = getValue(split[0].replaceAll("\\D",	""), VALUE_TO_PN_BASE_LENGTH, '0', true);
				split[1] = getValue(split[1].replaceAll("\\D",	""), 1, '0', true);
				exp = split[0] + 'E' + split[1];
			}else{
				int m = getMultiplier(value);
				String[] split = value.split( "\\D", 2);

				long l = Math.round( Double.parseDouble( split[0].replaceAll( "\\D",	"") + '.' + (split.length>1 ? split[1].replaceAll( "\\D", "") : "0")) * m * 100);

				if(l==0)
					exp = "00E0";
				else if(l<100)
					exp = getValue(Long.toString(l), VALUE_TO_PN_BASE_LENGTH, '0', false) + "EA";
				else{
					value = Long.toString(l);
					l = value.length();
					exp = getValue(value, VALUE_TO_PN_BASE_LENGTH, '0', true) + 'E' + (l-3);
				}
			}
		}else
			exp = "____";

		return  exp;
	}

	protected abstract String toField(String exponent);
	protected abstract int getMultiplier(String value);
	protected abstract int getExp(String[] split);

	@Override
	public String getDesctiption(PartNumberForm partNumberForm) {
		return getField(partNumberForm, DESCRIPTION_INDEX);
	}

	@Override
	public String getMfrId(PartNumberForm partNumberForm) {
		return getField(partNumberForm, MFR_INDEX);
	}


	@Override
	public String getMfrPN(PartNumberForm partNumberForm) {
		return getField(partNumberForm, MFR_PN_INDEX);
	}

	public long toLong(String string) {
		long base;

		if(string!=null){
			String baseStr = string.replaceAll("\\D",	"");

			if(baseStr.isEmpty())
				base = 0;
			else
				base = Long.parseLong(baseStr);
		}else
			base = 0;

		return base;
	}

	public String getValue(String value, int size, char ch, boolean addToRight) {

		if(value==null || value.isEmpty())
			value = new String(new char[size]).replaceAll("\0", Character.toString(ch));
		else if(value.length()!=size){
			if(value.length()<size){
				if(addToRight)
					value += new String(new char[size-value.length()]).replaceAll("\0", Character.toString(ch));
				else
					value = new String(new char[size-value.length()]).replaceAll("\0", Character.toString(ch)) + value;
			}else 
				value = value.substring(0, size);
		}
		return value;
	}

	protected String getField(PartNumberForm partNumberForm, int index) {
		String[] fields = partNumberForm.getFields();
		return index<0 || fields==null || fields.length<=index || fields[index]==null || fields[index].isEmpty() ? null : fields[index];
	}

	@Autowired CountsRepository countsRepository;
	protected Long getSequatialNumber() {
		List<String> pns = countsRepository.getPartNubersToCount(FIRST_CHAR_ID, NEXT_TWO_CHARS);
		long sn = 0;
		for(String pn:pns){
			long l = 0;
			switch(pn.substring(0, 3)){
			case "0CB":
				l = toLong(pn.substring(13));
				break;
			case "000":
			case "00A":
			case "0IC":
			case "0MC":
			case "0RQ":
			case "0VV":
				l = toLong( pn.substring(5, 9));
				break;
			case "00D":
			case "00T":
				l = toLong( pn.substring(3, 7));
				break;
			case "0CO":
				l = toLong( pn.substring(9));
			}

			if(l>sn) sn = l;
		}
		return ++sn;
	}
}
