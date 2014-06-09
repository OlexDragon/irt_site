package irt.data.components;

import irt.data.Link;
import irt.data.dao.ComponentDAO;
import irt.data.dao.SecondAndThirdDigitsDAO;
import irt.work.InputTitles;
import irt.work.TextWork;

import java.util.Map;

import org.apache.commons.io.FilenameUtils;

public class Component extends Data{

	public static final Map<Integer, String> CLASS_ID_NAME = new SecondAndThirdDigitsDAO().getMapIdClass();
	public static final Map<String, Integer> CLASS_NAME_ID = new SecondAndThirdDigitsDAO().getMapClassId();

	public Component(){	}

	public Component(int id, String partNumberStr, String manufacturePartNumber, String manufId, String description, String quantityStr, String location, Link link, String footprint, String schematic_letter, String schematicPart) {

		set(id, partNumberStr, manufacturePartNumber, manufId, description, quantityStr, location, link, footprint, schematic_letter);
		setSchematicPart(schematicPart);
	}

	public Component(int componentId) {
		Component component = new ComponentDAO().getComponent(componentId);
		if(component!=null && !component.getPartNumber().isEmpty()){
			set(componentId, component.getPartNumber(), component.getManufPartNumber(),
					component.getManufId(), component.getDescription(), component.getQuantityStr(),
					component.getLocation(), component.getLink(), component.getFootprint(), component.getSchematicLetter());
		}
	}

	public void set(int id, String partNumberStr, String manufacturePartNumber,
			String manufId, String description, String quantityStr,
			String location, Link link, String footprint, String schematic_letter) {
		setId(id);
		setPartNumber(partNumberStr);
		setManufPartNumber(manufacturePartNumber);
		setManufId(manufId);
		setDescription(description);
		setQuantityStr(quantityStr);
		setLocation(location);
		setLink(link);
		setFootprint(footprint);
		setSchematicLetter(schematic_letter);
	}

	@Override
	public void setClassId(){
		setClassId(TextWork.COMPONENT);
	}

	@Override
	public int getPartNumberSize() {
		return 14;
	}

	@Override
	public void setTitles() {
		setTitles( new InputTitles(new String[] { "Description",	"Mfr",	"Mfr P/N" },
									new String[] { "text",			"select",		"text"}));
	}

	public String getSelectOptionHTML(int index) {
		return index==MANUFACTURE ? getManufactureOptionHTML() : "";
	}

	@Override
	public String getPartNumberF() {
		return TextWork.getPartNumber(getPartNumber(), 3, 9, (PART_NUMB_SIZE==14)?14:13, PART_NUMB_SIZE);
	}

	public String getPartNumber() {
		return (super.getPartNumber()!=null)
				? super.getPartNumber()
						: "";
	}

	public String getManufPartNumber() {
		return (super.getManufPartNumber()!=null)
				? super.getManufPartNumber()
						: "";
	}
	
	public boolean isSet() {
		String partNumber = getPartNumber();
		return partNumber!=null && partNumber.length()==PART_NUMB_SIZE && TextWork.isValid(partNumber);
	}

	@Override
	public String getDataId() {
		return "0";
	}

	public String getValue(int first, int and) {
		String returnStr = "";

		if(getPartNumber().length()>and)
			returnStr = getPartNumber().substring(first,and);
		
		return (!returnStr.contains("?"))
								? returnStr
										: "";
	}
	
	public String getInt(String valueStr, int minNumberOfDijits) {
		return String.format("%"+minNumberOfDijits+"s", new Value(valueStr, TextWork.CAPACITOR).getIntValue()).replaceAll(" ", "0");
	}

	public boolean isTheSameLink(String fileName) {
		return fileName.equals(FilenameUtils.getName(getLink().getLink()));
	}

	@Override
	protected String getPartType(String partNumber) {
		return null;
	}

	@Override
	public String getValue() {
		return null;
	}

	public void resetSequentialNunber() {
	}

	@Override
	public String getDbValue() {
		if(super.getDbValue()==null || super.getDbValue().isEmpty())
			setDbValue(getValue(getPartNumber()));
		return super.getDbValue();
	}

	protected String getValue(String partNumber) {
		return null;
	}

	@Override
	public String getDbVoltage() {
		if(super.getDbVoltage()==null || super.getDbVoltage().isEmpty())
			setDbVoltage(getDbVoltage(getPartNumber()));
		return super.getDbVoltage();
	}

	protected String getDbVoltage(String partNumber) {
		return null;
	}

	public String getSuperValue(int index){
		return super.getValue(index);
	}
}

