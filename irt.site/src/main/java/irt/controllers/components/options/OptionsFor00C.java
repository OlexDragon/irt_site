package irt.controllers.components.options;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import irt.controllers.components.interfaces.ValueText;
import irt.entities.builders.EntityBuilderAbstract;

@Component
@Scope("session")
public class OptionsFor00C extends OptionsFor0{

	public OptionsFor00C(EntityBuilderAbstract builder) {
		super("0C", builder.FIELDS_COUNT, builder.MFR_INDEX);
	}

	@PostConstruct
	private void setOptions(){

		//Capacitor type
		List<?> entities = arrayEntityRepository.findByKeyNameOrderByDescriptionAsc("cap_type");
		if(entities!=null)
			options[1] = entities.toArray(new ValueText[0]);

		//Mounting
		entities = arrayEntityRepository.findByKeyNameOrderByDescriptionAsc("cap_mounting");
		if(entities!=null)
			options[2] = entities.toArray(new ValueText[0]);

		//Size
		entities = arrayEntityRepository.findByKeyNameOrderByDescriptionAsc("size");
		if(entities!=null)
			options[4] = entities.toArray(new ValueText[0]);
	}
}
