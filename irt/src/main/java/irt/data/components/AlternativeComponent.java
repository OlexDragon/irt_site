package irt.data.components;

public class AlternativeComponent {

	private int id;
	private int componentId;
	private String mfrId;
	private String mfrPN;
	private String mfrName;

	public AlternativeComponent(int id, int componentId, String mfrId, String mfrPN) {
		this.id = id;
		this.componentId = componentId;
		this.mfrId = mfrId;
		this.mfrPN = mfrPN;
	}

	public int getId() {
		return id;
	}
	public int getComponentId() {
		return componentId;
	}
	public String getMfrId() {
		return mfrId;
	}
	public String getMfrPN() {
		return mfrPN;
	}
}
