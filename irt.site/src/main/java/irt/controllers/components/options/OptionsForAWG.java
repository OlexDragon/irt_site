package irt.controllers.components.options;

import javax.annotation.PostConstruct;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("session")
public class OptionsForAWG extends OptionsForA{

	public OptionsForAWG() {
		super("WG");
	}

	@PostConstruct
	private void setOptions(){
	}
}
