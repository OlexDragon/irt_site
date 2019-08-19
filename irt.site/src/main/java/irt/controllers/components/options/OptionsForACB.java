package irt.controllers.components.options;

import javax.annotation.PostConstruct;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("session")
public class OptionsForACB extends OptionsForA{

	public OptionsForACB() {
		super("CB");
	}

	@PostConstruct
	private void setOptions(){
	}
}
