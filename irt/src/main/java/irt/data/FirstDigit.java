package irt.data;


public class FirstDigit {
	private int id;
	private char firstChar;
	private String description;

//	private Logger logger = Logger.getLogger();

	public FirstDigit(int id, char firstChar, String description) {

		this.id = id;
		this.firstChar = firstChar;
		this.description = description;
//		logger.debug("ID = " + id + "; Description = " + description);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public char getFirstChar() {
		return firstChar;
	}

	public void setFirstChar(char firstChar) {
		this.firstChar = firstChar;
	}

	public String getDescription() {
//		logger.debug("getDescription() = " + description);
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return "FirstDigits [id=" + id + ", description=" + description + "]";
	}

}
