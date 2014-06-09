package irt.product;

import irt.work.TextWork;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

//import org.apache.log4j.Logger;

public class BomRef {
	private long refId = -1;
	private List<Integer> refNumber = new ArrayList<>();
	private List<String> errors = new ArrayList<>();

//    private Logger logger = Logger.getLogger(this.getClass());

	protected BomRef(){}

	public BomRef(String reference){
		addReferences(reference);
	}

	public BomRef(ResultSet resultSet) throws SQLException {
		addReferences(resultSet.getString("ref"));
	}

	public List<String> getPartReferences(String refLetters) {
		List<String> partReferences = null;

		if(!refNumber.isEmpty()){
			partReferences = new ArrayList<>();
			for(int i:refNumber)
				partReferences.add(refLetters+i);
		}

		return partReferences;
	}

	/**
	 * @return number of components
	 */
	public int size(){
		return refNumber!=null ? refNumber.size() : 0;
	}

	public String getEachPartReferencesStr(String refLetters) {
		String partReferences = "";

		if(refNumber!=null)
			for(Object rn:refNumber.toArray())
				partReferences += partReferences.isEmpty() ? refLetters+rn : " "+refLetters+rn;

		return partReferences;
	}

	public String getRefStrShort(String refLetters) {
		String partReferences = "";

		if (!refNumber.isEmpty()) {
			boolean isNear = false;
			boolean needDash = false;
			int intTmp = -2;
			int index = 0;

			for (; index < refNumber.size(); index++) {
				if (intTmp + 1 == refNumber.get(index)) {
					intTmp = refNumber.get(index);
					if (!isNear)
						isNear = true;
					else
						if (!needDash)
							needDash =true;
				} else {
					if (isNear) {
						partReferences += (needDash ?'-':' ')+refLetters + intTmp;
						needDash = false;
						isNear = false;
					}
					intTmp = refNumber.get(index);
					partReferences += (partReferences.isEmpty() ? "" : " ") + refLetters + intTmp;
				}
			}
			if (isNear)
				partReferences += (needDash ?'-':' ')+refLetters + intTmp;
		}

		return partReferences;
	}

	public String getRefNumbersShort() {
		String partReferences = "";

		if (!refNumber.isEmpty()) {
			boolean isNear = false;
			boolean needDash = false;
			int intTmp = -2;
			int index = 0;

			for (; index < refNumber.size(); index++) {
				if (intTmp + 1 == refNumber.get(index)) {
					intTmp = refNumber.get(index);
					if (!isNear)
						isNear = true;
					else
						if (!needDash)
							needDash =true;
				} else {
					if (isNear) {
						partReferences += (needDash ?"-":" ")+intTmp;
						needDash = isNear = false;
					}
					intTmp = refNumber.get(index);
					partReferences += (partReferences.isEmpty() ? "" : " ") + intTmp;
				}
			}

			if (isNear)
				partReferences += (needDash ?"-":" ")+intTmp;
		}

		return partReferences;
	}

	public List<String> getErrors() {
		return errors;
	}

	public void setErrors(List<String> errors) {
		this.errors = errors;
	}

	/**
	 * @param componentId database id in `irt`.`bomunit`(MySQL)
	 * @param references String with references separated by spaces or dashes
	 */
	public void addReferences(String references) {
		addReferencesNumber(TextWork.getFirstLetters(references), getIntArray(references));
	}

	/**
	 * add references to existing references
	 */
	protected void addReferencesNumber(String refLetters, Integer[] partReferencesInt) {
		for(int i:partReferencesInt)
			if(refNumber.contains(i))
				errors.add(refLetters+i+" - reference is repeated");
			else
				refNumber.add(i);

		Collections.sort(refNumber);
	}

	
	private Integer[] getIntArray(String reference) {

		String[] ref;
		if(reference.contains(","))
			ref = reference.split(",");
		else
			ref = reference.split(" ");

		List<Integer> returnRef = new ArrayList<>();

		for (String s : ref)
			if (!s.isEmpty())
				if (s.contains("-"))
					returnRef.addAll(getList(s.split("-")));
				else
					returnRef.add(getInt(s));

		return returnRef.toArray(new Integer[returnRef.size()]);
	}

	private List<Integer> getList(String[] splitDash) {
		List<Integer> returnRef = null;

		if(splitDash!=null && splitDash.length>1){
			returnRef = new ArrayList<>();
			int firstNumber = getInt(splitDash[0]);
			int secondNumber = getInt(splitDash[1]);

			while(firstNumber<=secondNumber)
				returnRef.add(firstNumber++);
		}else if(splitDash.length==1)
			returnRef = new ArrayList<>(getInt(splitDash[0]));

		return returnRef;
	}

	/**
	 * @param value 
	 * @return unsigned integer; remove all non digit from the 'value' if no digit return '-1'
	 */
	private int getInt(String value) {
		String returnStr = "";	

		if (value != null)
			for (char ch : value.toCharArray())
				if (Character.isDigit(ch))
					returnStr += ch;

		if(returnStr.isEmpty())
			returnStr += -1;

		return Integer.parseInt(returnStr);
	}

	public List<Integer> getRefNumber() {
		return refNumber;
	}

	public Integer[] getRefNumberArray() {
		return refNumber.toArray(new Integer[refNumber.size()]);
	}

	public boolean isError(){
		return errors.size()>0;
	}

	public void setRefNumber(List<Integer> refNumber) {
		this.refNumber = refNumber;
	}

	public long getRefId() {
		return refId;
	}

	public void setRefId(long id) {
		this.refId = id;
	}

	public int getQuantity() {
		return refNumber.size();
	}
}
