package irt.controllers.components.options;

import javax.annotation.PostConstruct;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("session")
public class OptionsForAEN extends OptionsForA{

	public OptionsForAEN() {
		super("CN");
	}

	@PostConstruct
	private void setOptions(){
	}
}
