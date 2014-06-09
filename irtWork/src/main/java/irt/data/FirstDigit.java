package irt.data;

import java.sql.ResultSet;
import java.sql.SQLException;

public class FirstDigit {
	private char id;
	private String description;

//	private Logger logger = Logger.getLogger(this.getClass());

	public FirstDigit(char id, String description) {

		this.id = id;
		this.description = description;
//		logger.debug("ID = " + id + "; Description = " + description);
	}

	public FirstDigit(ResultSet resultSet) throws SQLException {
		this((resultSet.getString("id")).charAt(0), resultSet
				.getString("description"));
	}

	public char getId() {
		return id;
	}

	public void setId(char id) {
		this.id = id;
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
