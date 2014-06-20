package irt.data.components.alternative;

public class AlternativeMfrPN {
	private int id;
	private int componentID;
	private String mfrPN;
	private String mfrID;

	public AlternativeMfrPN(int id, int idComponents, String mfrPN, String mfrId) {
		this.id = id;
		this.componentID = idComponents;
		this.mfrPN = mfrPN;
		this.mfrID = mfrId;
	}

	public int getId() {
		return id;
	}

	public int getComponentID() {
		return componentID;
	}

	public String getMfrPN() {
		return mfrPN;
	}

	public String getMfrId() {
		return mfrID;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setIdComponents(int idComponents) {
		this.componentID = idComponents;
	}

	public void setMfrPN(String mfrPN) {
		this.mfrPN = mfrPN;
	}

	public void setMfrId(String mfrID) {
		this.mfrID = mfrID;
	}

	public boolean isPrepared() {
		return componentID>0 && mfrPN!=null && !mfrPN.isEmpty() && mfrID!=null && mfrID.length()==2;
	}

	@Override
	public String toString() {
		return "AlternativeMfrPN [id=" + id + ", componentID=" + componentID
				+ ", mfrPN=" + mfrPN + ", mfrID=" + mfrID + "]";
	}
}
