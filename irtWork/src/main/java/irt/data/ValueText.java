package irt.data;

import irt.work.ComboBoxField;

public class ValueText implements ComboBoxField{
	private String value;
	private String text;

	public ValueText(String value, String text) {
		this.value = value;
		this.text = text;
	}

	public ValueText(int componentId, String partNumber) {
		this(""+componentId, partNumber);
	}

	public String getValue() {
		return value;
	}
	public String getText() {
		return text;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public void setText(String text) {
		this.text = text;
	}

}
