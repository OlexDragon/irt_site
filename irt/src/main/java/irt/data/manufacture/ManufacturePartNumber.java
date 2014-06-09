package irt.data.manufacture;

public class ManufacturePartNumber {

	private int id;
	private String mfrPN;
	private String mfr;

	public ManufacturePartNumber(int id, String mfrPN, String mfr) {
		this.id = id;
		this.mfrPN = mfrPN;
		this.mfr = mfr;
	}

	public ManufacturePartNumber(int id) {
		this.id = id;
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

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "ManufacturePartNumber [id=" + id + ", mfrPN=" + mfrPN
				+ ", mfr=" + mfr + "]";
	}

	@Override
	public boolean equals(Object obj) {
		return obj!=null ? obj.hashCode()==hashCode() : false;
	}

	@Override
	public int hashCode() {
		return id>=0 ? id : super.hashCode();
	}
}
