package irt.work;

public class InputTitle {
	
	private String name;
	private String inputType;
	
	public InputTitle(String name, String inputType) {
		setInput(name, inputType);
	}

	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getInputType() {
		return inputType;
	}
	
	public void setInputType(String inputType) {
		this.inputType = inputType;
	}

	public void setInput(String name, String inputType) {
		setName(name);
		setInputType(inputType);
	}

	public String getDbColumnName(){
		String columnName = null;
		switch(name){
		case "Description":
		case "<br />Description":
			columnName = "description";
			break;
		case "Mfr P/N":
			columnName = "manuf_part_number";
			break;
		case "Value(pF)":
		case "Value(nH)":
		case "Value(Ohm)":
			columnName = "value";
//			break;
//		default:
//			irt.work.Error.setErrorMessage(name+" : "+name.length());
		}
		return columnName;
	}
}
