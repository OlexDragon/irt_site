package irt.data.manufacture;

public class ManufacturePartNumber {

	private int ComponentId;
	private String mfrID;
	private String mfrPN;
	private String mfr;

	public ManufacturePartNumber(int id, String mfrID, String mfrPN, String mfr) {
		this.ComponentId = id;
		this.mfrID = mfrID;
		this.mfrPN = mfrPN;
		this.mfr = mfr;
	}

	public ManufacturePartNumber(int id) {
		this.ComponentId = id;
	}

	public String getMfrPN() {
		return mfrPN;
	}

	public String getMfr() {
		return mfr;
	}

	public void setMfrPN(String mfrPN) {
		this.mfrPN = mfrPN;
	}

	public void setMfr(String mfr) {
		this.mfr = mfr;
	}

	/**
	 * @return component ID
	 */
	public int getComponentId() {
		return ComponentId;
	}

	public void setComponentId(int ComponentId) {
		this.ComponentId = ComponentId;
	}

	public String getMfrID() {
		return mfrID;
	}

	public void setMfrID(String mfrID) {
		this.mfrID = mfrID;
	}

	@Override
	public String toString() {
		return "ManufacturePartNumber [id=" + ComponentId + ", mfrPN=" + mfrPN
				+ ", mfr=" + mfr + "]";
	}

	@Override
	public boolean equals(Object obj) {
		return obj!=null ? obj.hashCode()==hashCode() : false;
	}

	@Override
	public int hashCode() {
		return ComponentId>=0 ? ComponentId : super.hashCode();
	}
}
