package irt.controllers.components.validators;

public class Validator00A extends Validator0{

	private final static String[] VALIDATE_FOR = new String[]{"Mfr P/N", "Mfr", "Package", "Leads Number", "Description", "SeqN"};

	public Validator00A() {
		super(VALIDATE_FOR);
	}
}
