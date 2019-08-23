package irt.beans.assemblies;

import irt.controllers.components.interfaces.ValueText;
import irt.entities.IrtComponentEntity;

public class AssembliesRevisionBean implements ValueText {

	private String value;

	public AssembliesRevisionBean(IrtComponentEntity componentEntity) {
		final String partNumber = componentEntity.getPartNumber();

		if(!partNumber.startsWith("A"))
			throw new IllegalArgumentException("It is not the Assemblies Entity: " + componentEntity);

		value = partNumber.substring(9);
	}

	public AssembliesRevisionBean(int value) {

		if(value<1 || value>99)
			throw new IllegalArgumentException("Value cannot be negative and greater than 99: value=" + value);

		this.value = String.format("R%02d", value);
	}

	@Override
	public String getValue() {
		return value;
	}

	@Override
	public String getText() {
		return value;
	}

}
