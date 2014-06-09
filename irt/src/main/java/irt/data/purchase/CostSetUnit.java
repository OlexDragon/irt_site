package irt.data.purchase;

public class CostSetUnit {

	private int componentId;
	private int mfrPNInddex;
	private int companyIndex;
	private int moqIndex;

	public CostSetUnit(int componentId, int mfrPNInddex, int companyIndex,int moqIndex) {
		this.componentId = componentId;
		this.mfrPNInddex = mfrPNInddex;
		this.companyIndex = companyIndex;
		this.moqIndex = moqIndex;
	}

	public int getComponentId() {
		return componentId;
	}

	public int getMfrPNInddex() {
		return mfrPNInddex;
	}

	public int getCompanyIndex() {
		return companyIndex;
	}

	public int getMoqIndex() {
		return moqIndex;
	}

	@Override
	public boolean equals(Object obj) {
		return obj!=null ? obj.hashCode()==hashCode() : false;
	}

	@Override
	public int hashCode() {
		return componentId>0 ? componentId : super.hashCode();
	}

	@Override
	public String toString() {
		return "CostSetUnit [componentId=" + componentId + ", mfrPNInddex="
				+ mfrPNInddex + ", companyIndex=" + companyIndex
				+ ", moqIndex=" + moqIndex + "]";
	}
}
