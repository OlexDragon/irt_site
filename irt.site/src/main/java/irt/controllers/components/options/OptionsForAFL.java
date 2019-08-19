package irt.controllers.components.options;

import javax.annotation.PostConstruct;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("session")
public class OptionsForAFL extends OptionsForA{

	public OptionsForAFL() {
		super("FL");
	}

	@PostConstruct
	private void setOptions(){
	}
}
