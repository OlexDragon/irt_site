package irt.product;

import irt.data.components.Component;
import irt.data.components.Data;
import irt.data.dao.ComponentDAO;
import irt.work.TextWork;

public class BomUnit extends BomRef implements BomUnitInterface {

	private Data component;

	public static BomUnitInterface getBomUnit(String partNumber, String reference){
		BomUnit bu = new BomUnit(partNumber, reference);
		return bu.component!=null ? bu : null;
	}

	protected BomUnit(){
		super("");
	}

	private BomUnit(String partNumber, String reference) {
		super(reference);
		setComponent(partNumber);
		setRefLetters(reference);
	}

	public BomUnit(String partNumber, String mnfPartNumber, String mnfId, String location, String qty, String reference) {
		super(reference);
		setComponent(partNumber, mnfPartNumber, mnfId, location, qty);
		setRefLetters(reference);
	}

	private void setComponent(String partNumber, String mnfPartNumber, String mnfId, String location, String qty) {
		component = new ComponentDAO().getData(partNumber);
		component.setManufPartNumber(mnfPartNumber);
		component.setManufId(mnfId);
		component.setLocation(location);
		component.setQuantityStr(qty);
	}

	public void setRefLetters(String reference) {
		if(reference!=null && !reference.isEmpty() && component!=null){
			String refLetter = TextWork.getFirstLetters(reference);
			if(component.getSchematicLetter()==null)
				new ComponentDAO().setRefLetters(component.getId(), refLetter);
			else if(!refLetter.equals(component.getSchematicLetter()))
				addError(component.getPartNumberF()+" - reference Letter"+(refLetter.length()>1 ? "s "+refLetter+" are " : " "+refLetter+" is ")+"not correct." +
							"<br />Have to use "+component.getSchematicLetter()+". <small>(E049)</small>");
		}
	}

	public Component getComponent() {
		return (Component) component;
	}

	public void setComponent(Component component) {
		this.component = component;
	}

	private void setComponent(String partNumber) {
		component = new ComponentDAO().getData(partNumber);
	}

	public boolean isSet() {
		return component!=null;
	}

	public void setFootprint(String footprintStr) {
		if(footprintStr!=null && component!=null){
			if(component.getFootprint().isEmpty())
				component.setFootprint(footprintStr);
			else if(!footprintStr.equalsIgnoreCase(component.getFootprint()))
				addError("Footprint "+footprintStr+" for P/N:"+component.getPartNumberF()+" is not correct." +
						"<br />Have to use "+component.getFootprint()+".");
		}
	}

	public void addError(String errorStr) {
		getErrors().add(errorStr);
	}

	public String getClassId() {
		return component!=null ? component.getClassId() : "";
	}

	public void set(BomUnitInterface bomUnitInterface) {
		if(bomUnitInterface!=null){
			if(component==null){
				component = bomUnitInterface.getComponent();
				addReferencesNumber(bomUnitInterface.getRefLetters(), bomUnitInterface.getRefNumberArray());
			}else if(component.equals(bomUnitInterface))
				addReferencesNumber(bomUnitInterface.getRefLetters(), bomUnitInterface.getRefNumberArray());
			else
				addError("<br />Error <small>(E050)</small>.");
		}
	}

	public String getRefLetters() {
		return component.getSchematicLetter();
	}

	public String getPartNumberF() {
		return component!=null ? component.getPartNumberF() : "";
	}

	public String getRefStrShort() {
		return getRefStrShort(component.getSchematicLetter());
	}

	public String getEachPartReferencesStr() {
		return getEachPartReferencesStr(component.getSchematicLetter());
	}

	@Override
	public String toString() {
		return component!=null ? component.toString() : null;
	}

	@Override
	public boolean equals(Object obj) {
		return component.equals(obj);
	}

	@Override
	public int hashCode() {
		return component.hashCode();
	}

	@Override
	public long getRefId() {
		// TODO Auto-generated method stub
		return super.getRefId();
	}
}
