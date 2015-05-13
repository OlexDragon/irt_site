package irt.web.view.beans;

import irt.web.view.workers.component.PartNumbers;

public class ComponentBean {

	public enum Status{
		ERROR,
		SUCCESS
	}

	private String partNumber;
	private Status status;
	private String msg;
	private boolean bom;

	public String getPartNumber() {
		return partNumber;
	}

	public void setPartNumber(String partNumber) {
		if(!PartNumbers.equals(partNumber, this.partNumber))
			this.partNumber = partNumber;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public boolean isBom() {
		return bom;
	}

	public void setBom(boolean bom) {
		this.bom = bom;
	}

	@Override
	public String toString() {
		return "\n\tComponentBean [\n\t\tpartNumber=" + partNumber +
				",\n\t\tstatus=" + status +
				",\n\t\tmsg=" + msg +
				",\n\t\tbom=" + bom + "]";
	}
}
