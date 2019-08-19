package irt.controllers.components.options;

import javax.annotation.PostConstruct;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("session")
public class OptionsForAKT extends OptionsForA{

	public OptionsForAKT() {
		super("KT");
	}

	@PostConstruct
	private void setOptions(){
	}
}
