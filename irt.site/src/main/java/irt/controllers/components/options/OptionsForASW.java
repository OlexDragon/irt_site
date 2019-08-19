package irt.controllers.components.options;

import javax.annotation.PostConstruct;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("session")
public class OptionsForASW extends OptionsForA{

	public OptionsForASW() {
		super("SW");
	}

	@PostConstruct
	private void setOptions(){
	}
}
