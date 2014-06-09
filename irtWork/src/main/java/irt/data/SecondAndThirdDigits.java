package irt.data;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SecondAndThirdDigits {

	private String id;
	private char idFirstDigit;
	private String description;

//	private Logger logger = Logger.getLogger(this.getClass());

	public SecondAndThirdDigits(String id, char idFirstDigit, String description) {

		this.id = id;
		this.idFirstDigit = idFirstDigit;
		this.description = description;
	}

	public SecondAndThirdDigits(ResultSet resultSet) throws SQLException {
		this(resultSet.getString("id"), (resultSet.getString("id_first"))
				.charAt(0), resultSet.getString("description"));
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public char getIdFirstDigit() {
		return idFirstDigit;
	}

	public void setIdFirstDigit(char idFirstDigit) {
		this.idFirstDigit = idFirstDigit;
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
		return "SecondAndThirdDigits [id=" + id + ", idFirstDigit="
				+ idFirstDigit + ", description=" + description +"]";
	}
}
