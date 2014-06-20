package irt.data;


public class SecondAndThirdDigits {

	private String id;
	private FirstDigit firstDigit;
	private String description;
	private int classId;

//	private Logger logger = Logger.getLogger(this.getClass());

	public SecondAndThirdDigits(String id, FirstDigit firstDigit, String description, int classId) {

		this.id = id;
		this.firstDigit = firstDigit;
		this.description = description;
		this.classId = classId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public FirstDigit getFirstDigit() {
		return firstDigit;
	}

	public void setFirstDigit(FirstDigit firstDigit) {
		this.firstDigit = firstDigit;
	}

	public String getDescription() {
//		logger.debug("getDescription() = " + description);
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getClassId() {
		return classId;
	}

	public void setClassId(int classId) {
		this.classId = classId;
	}

	public String getClassIdStr(){
		return firstDigit.getFirstChar()+id;
	}

	@Override
	public String toString() {
		return "SecondAndThirdDigits [id=" + id + ", idFirstDigit="
				+ firstDigit + ", description=" + description +"]";
	}
}
