package irt.controllers.components.validators;

public class Validator00C extends Validator0{

	private final static String[] VALIDATE_FOR = new String[]{"Value", "Type", "Mounting", "Voltage", "Size", "Description", "Mfr - not required", "Mfr P/N - not required"};

	public Validator00C() {
		super(VALIDATE_FOR);
	}
}
