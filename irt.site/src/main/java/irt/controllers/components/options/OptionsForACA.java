package irt.controllers.components.options;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("session")
public class OptionsForACA extends OptionsForA{

	public OptionsForACA() {
		super("CA");
	}
}
