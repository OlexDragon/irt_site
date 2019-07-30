package irt.controllers.components.options;

import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import irt.controllers.components.interfaces.ValueText;
import irt.entities.ArrayEntity;
import irt.entities.builders.EntityBuilderAbstract;

@Component
@Scope("session")
public class OptionsFor0CO extends OptionsFor0{

	private final int MALE_FEMALE	= 2;
	private final int TYPE			= 3;

	public OptionsFor0CO(EntityBuilderAbstract builder) {
		super("CO", builder.FIELDS_COUNT, builder.MFR_INDEX);
	}

	@Override protected void setFieldOptions(){

		super.setFieldOptions();

		//Male or Female
		List<ArrayEntity> entities = arrayEntityRepository.findByKeyNameOrderByDescriptionAsc("M_F");
		if(entities!=null)
			options[MALE_FEMALE] = entities.toArray(new ValueText[0]);

		//Connector type
		entities = arrayEntityRepository.findByKeyNameOrderByDescriptionAsc("con_type");
		if(entities!=null)
			options[TYPE] = entities.toArray(new ValueText[0]);
	}
}
