package irt.stock.data.jpa.beans.engineering.eco;

public enum EcoOption {

	ALL			("All"					, EcoStatus.values()),
	RELEASED	("Released"				, EcoStatus.RELEASED),
	APPROVED	("Approved"				, EcoStatus.FINALLY_APPROVED),
	FOR_CONFIRM	("For Final Approval"	, EcoStatus.APPROVED),
	FOR_APPROVAL("For Approval"			, EcoStatus.CREATED),
	NOT_APPROVED("Not Approved"			, EcoStatus.CREATED, EcoStatus.APPROVED);

	private final String description;
	private final EcoStatus[] ecoStatus;

	private EcoOption(String description, EcoStatus...ecoStatus) {
		this.description = description;
		this.ecoStatus = ecoStatus;
	}

	public String getDescription() {
		return description;
	}

	public EcoStatus[] getEcoStatus() {
		return ecoStatus;
	}
}
