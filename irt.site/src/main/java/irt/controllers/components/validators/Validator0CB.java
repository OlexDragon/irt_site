package irt.controllers.components.validators;

public class Validator0CB extends Validator0{

	private final static String[] VALIDATE_FOR = new String[]{"Con1 Type", "Con2 Type", "Cable type", "Length", "SeqN - not required", "Description", "Mfr - not required", "Mfr P/N - not required"};

	public Validator0CB() {
		super(VALIDATE_FOR);
	}
}
