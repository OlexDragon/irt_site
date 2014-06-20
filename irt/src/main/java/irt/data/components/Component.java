package irt.data.components;

import irt.data.Link;
import irt.data.dao.ComponentDAO;
import irt.work.TextWorker;
import irt.work.TextWorker.PartNumberFirstChar;

import org.apache.commons.io.FilenameUtils;

public class Component extends Data{

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
		setClassId(""+PartNumberFirstChar.COMPONENT.getFirstDigit().getFirstChar());
	}

	@Override
	public int getPartNumberSize() {
		return 14;
	}

	@Override
	protected String getDatabaseNameForTitles() {
		return "comp_titles";
	}

	public String getSelectOptionHTML(int index) {
		return index==MANUFACTURE ? getManufactureOptionHTML() : "";
	}

	@Override
	public String getPartNumberF() {
		return TextWorker.getPartNumber(getPartNumber(), 3, 9, (PART_NUMB_SIZE==14)?14:13, PART_NUMB_SIZE);
	}

	public String getPartNumber() {
		String partNumber = super.getPartNumber();
		return (partNumber!=null)
				? partNumber
						: "";
	}

	public String getManufPartNumber() {
		String manufPartNumber = super.getManufPartNumber();
		return (manufPartNumber!=null)
				? manufPartNumber
						: "";
	}
	
	public boolean isSet() {
		String partNumber = getPartNumber();
		return partNumber!=null && partNumber.length()==PART_NUMB_SIZE && TextWorker.isValid(partNumber);
	}

	@Override
	public String getDataId() {
		return "0";
	}

	public String getValue(int first, int and) {
		String returnStr;
		int length = getPartNumber().length();

		if(length>and)
			returnStr = getPartNumber().substring(first,and);

		else if(length==and && length>first)
			returnStr = getPartNumber().substring(first);

		else
			returnStr = "";

		return returnStr.contains("?") ? "" : returnStr;
	}
	
	public String getInt(String valueStr, int minNumberOfDijits) {
		return String.format("%"+minNumberOfDijits+"s", new Value(valueStr, TextWorker.CAPACITOR).getIntValue()).replaceAll(" ", "0");
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

