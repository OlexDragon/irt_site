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
	public String getValue() {
		return "";
	}

	@Override
	public void setClassId() {
		setClassId("___");
	}

	@Override
	public void setTitles() {
	}

}
