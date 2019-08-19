package irt.entities.builders;

import irt.controllers.components.PartNumberForm;
import irt.entities.IrtComponentEntity;

public interface EntityBuilder {

	IrtComponentEntity 	build(PartNumberForm form);
	IrtComponentEntity 	updateEntity(PartNumberForm partNumberForm);
	String				getMfrId(PartNumberForm partNumberForm);
	String				getMfrPN(PartNumberForm partNumberForm);
	String 				getDesctiption(PartNumberForm partNumberForm);

	void fillForm(PartNumberForm partNumberForm);
	void fillForm(PartNumberForm partNumberForm, String partNumber);
	void fillForm(PartNumberForm partNumberForm, IrtComponentEntity componentEntity);
}
