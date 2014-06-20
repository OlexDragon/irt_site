package irt.data.components;

public class Unknown extends Component {

	@Override
	public int getPartNumberSize() {
		return 0;
	}

	@Override
	public String getDataId() {
		return null;
	}

	@Override
	public String getPartNumberF() {
		return "";
	}

	@Override
	public String getValue() {
		return null;
	}

	@Override
	protected String getPartType(String partNumber) {
		return null;
	}

	@Override
	public void setClassId() {
		setClassId("Unknown");
	}

	@Override
	public void setTitles() {
	}

}
