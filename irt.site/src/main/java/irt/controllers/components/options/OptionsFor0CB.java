package irt.controllers.components.options;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import irt.controllers.components.interfaces.ValueText;
import irt.entities.builders.EntityBuilderAbstract;

@Component
@Scope("session")
public class OptionsFor0CB extends OptionsFor0{

	public OptionsFor0CB(EntityBuilderAbstract builder) {
		super("CB", builder.FIELDS_COUNT, builder.MFR_INDEX);
	}

	@PostConstruct
	private void setOptions(){

		//Connecter type
		List<?> entities = arrayEntityRepository.findByKeyNameOrderByDescriptionAsc("cab_con_type");
		if(entities!=null)
			options[0] = options[1] = entities.toArray(new ValueText[0]);

		//Cable type
		entities = arrayEntityRepository.findByKeyNameOrderByDescriptionAsc("cable_type");
		if(entities!=null)
			options[2] = entities.toArray(new ValueText[0]);
	}
}
