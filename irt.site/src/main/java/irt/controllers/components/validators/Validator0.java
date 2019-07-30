package irt.controllers.components.validators;

import irt.controllers.components.PartNumberForm;

public class Validator0 implements Validator{

	private final String[] VALIDATE_FOR;

	public Validator0() {
		this(new String[]{});
	}
	protected Validator0(String[] validateFor) {
		VALIDATE_FOR = validateFor;
	}

	@Override
	public String validatre(PartNumberForm form) {

		String[] fields = form.getFields();
		if(fields!=null){
			int length = fields.length>VALIDATE_FOR.length ? VALIDATE_FOR.length : fields.length;

			for(int i=0; i<length; i++){
				switch(VALIDATE_FOR[i]){

				case "Numb.of pin":
				case "Voltage":
				case "Value":
				case "Length":
				case "Leads Number":
					if(fields[i]!=null && !fields[i].matches(".*\\d+.*"))
						return "The Field '" + VALIDATE_FOR[i] + "' must contain numbers.";

				case "Mfr P/N":
				case "Mfr":
				case "Package":
				case "Con1 Type":
				case "Con2 Type":
				case "Cable type":
				case "Type":
				case "Mounting":
				case "Size":
				case "M/F":
					if(fields[i]==null || fields[i].isEmpty())
						return "Fill '" + VALIDATE_FOR[i] + "' field";
					break;

				case "Description":
					if(fields[i]==null || fields[i].isEmpty())
						return "Fill Description field";
					else if(fields[i].length()>50)
						return "Description max length is 50 characters. Remove " + (fields[i].length() - 50) + "characters";
				}
			}
		}
		return null;
	}
}
