package irt.entities.builders;

import irt.controllers.components.PartNumberForm;
import irt.entities.IrtComponentEntity;

public interface EntityBuilder {

	IrtComponentEntity build(PartNumberForm form);
	void fillForm(PartNumberForm partNumberForm, IrtComponentEntity componentEntity);
	void fillForm(PartNumberForm partNumberForm);
	void fillForm(PartNumberForm partNumberForm, String partNumber);
	IrtComponentEntity updateEntity(PartNumberForm partNumberForm);
	String getMfrId(PartNumberForm partNumberForm);
	String getMfrPN(PartNumberForm partNumberForm);
	String getDesctiption(PartNumberForm partNumberForm);
}
