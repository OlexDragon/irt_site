package irt.controllers.components.validators;

public class Validator0CO extends Validator0{

	private final static String[] VALIDATE_FOR = new String[]{"Mfr P/N", "Numb.of pin", "M/F", "Type", "Seq.Num - not required", "Description", "Mfr - not required"};

	public Validator0CO() {
		super(VALIDATE_FOR);
	}
}
