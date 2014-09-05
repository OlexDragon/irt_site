package irt.data.html;

import irt.work.ComboBoxField;

public class HTMLComponentOption implements ComboBoxField {

	private static String selectedValue;
	private static String selectedText;
	private String value;
	private String text;

	public HTMLComponentOption(String value, String text) {
		this.value = value;
		this.text = text;
	}

	@Override
	public String getValue() {
		return value;
	}

	@Override
	public String getText() {
		return text;
	}

	public static String getSelectedValue() {
		return selectedValue;
	}

	public static void setSelectedValue(String selectedValue) {
		selectedText = null;
		HTMLComponentOption.selectedValue = selectedValue;
	}

	public static String getSelectedText() {
		return selectedText;
	}

	public static void setSelectedText(String selectedText) {
		selectedValue = null;
		HTMLComponentOption.selectedText = selectedText;
	}

	@Override
	public int hashCode() {
		return value!=null ? value.hashCode() : text.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		return obj!=null ? obj.hashCode()==hashCode() : false;
	}

	@Override
	public String toString() {
		StringBuilder option = new StringBuilder("<option");

		if(selectedValue!=null && selectedValue.equals(value) || selectedText!=null && selectedText.equals(text))
			option.append(" selected='selected'");

		if(value!=null){
			option.append(" value='");
			option.append(value);
			option.append("'");
		}

		option.append('>');
		option.append(text);
		option.append("</option>");

		return option.toString();
	}

}
