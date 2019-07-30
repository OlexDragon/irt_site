package irt.controllers.components.options;

import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import irt.controllers.components.interfaces.ValueText;
import irt.entities.ArrayEntity;
import irt.entities.builders.EntityBuilderAbstract;

@Component
@Scope("session")
public class OptionsFor00A extends OptionsFor0{

	public OptionsFor00A(EntityBuilderAbstract builder) {
		super("0A", builder.FIELDS_COUNT, builder.MFR_INDEX);
	}

	@Override protected void setFieldOptions(){

		super.setFieldOptions();

		//Connecter type
		List<ArrayEntity> entities = arrayEntityRepository.findByKeyNameOrderByDescriptionAsc("ic_package");
		if(entities!=null)
			options[2] = entities.toArray(new ValueText[0]);
	}
}
