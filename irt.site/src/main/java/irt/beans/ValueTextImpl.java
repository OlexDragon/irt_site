package irt.beans;

import irt.controllers.components.interfaces.ValueText;

public class ValueTextImpl implements ValueText {

	private String value;
	private String text;

	public ValueTextImpl(Object value, Object text) {
		this.value = value.toString();
		this.text = text.toString();
	}

	public ValueTextImpl(Object value) {
		this.text = this.value = value.toString();
	}

	@Override
	public String getValue() {
		return value;
	}

	@Override
	public String getText() {
		return text;
	}

}
