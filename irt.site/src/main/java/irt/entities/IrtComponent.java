package irt.entities;

public interface IrtComponent {

	Long getId();
	String getPartNumber();
	String getManufPartNumber();
	String getDescription();
	ManufactureEntity getManufacture();
	Integer getQty();
	String getLocation();
	LinkEntity getLink();
	String getFootprint();
	String getSchematicLetter();
	String getPartType();
	String getSchematicPart();
	String getValue();
	String getVoltage();

	void setId(Long id);
	void setPartNumber(String value);
	void setManufPartNumber(String value);
	void setDescription(String value);
	void setManufacture(ManufactureEntity manufactureEntity);
	void setQty(Integer value);
	void setLocation(String value);
	void setLink(LinkEntity value);
	void setFootprint(String value);
	void setSchematicLetter(String value);
	void setPartType(String value);
	void setSchematicPart(String value);
	void setValue(String value);
	void setVoltage(String value);
}
