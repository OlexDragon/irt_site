package irt.controllers.components.options;

import javax.annotation.PostConstruct;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("session")
public class OptionsForACO extends OptionsForA{

	public OptionsForACO() {
		super("CO");
	}

	@PostConstruct
	private void setOptions(){
	}
}
