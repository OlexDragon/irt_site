
package irt.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

@Entity
@Table(name="components")
public class IrtComponentEntity implements IrtComponent{

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id; @Override public Long getId() { return id; } @Override public void setId(Long id) { this.id = id; }

	@Column(unique=true)
	private String partNumber; @Override public String getPartNumber() { return partNumber; } @Override public void setPartNumber(String partNumber) { this.partNumber = partNumber; }

	@Column(unique=true)
	private String manufPartNumber; @Override public String getManufPartNumber() { return manufPartNumber; } @Override public void setManufPartNumber(String value) { manufPartNumber = value; }

	@ManyToOne
	@NotFound(action=NotFoundAction.IGNORE)
	@JoinColumn(name="manuf_id", referencedColumnName="id")
	private ManufactureEntity manufacture; @Override public ManufactureEntity getManufacture() { return manufacture; } @Override public void setManufacture(ManufactureEntity manufactureEntity) { manufacture = manufactureEntity; }

	@ManyToOne
	@JoinColumn(name="link", referencedColumnName="id")
	@NotFound(action=NotFoundAction.IGNORE)
	private LinkEntity link; @Override public LinkEntity getLink() { return link; } @Override public void setLink(LinkEntity value) { link = value; }

	private String	description;	@Override public String getDescription()	{ return description; }		@Override public void setDescription(String value)	{ description = value; }
	private String	location;		@Override public String getLocation()		{ return location; }		@Override public void setLocation(String value)		{ location = value; }
	private String	footprint;		@Override public String getFootprint()		{ return footprint; }		@Override public void setFootprint(String value)	{ footprint = value; }
	private String	schematicLetter;@Override public String getSchematicLetter(){ return schematicLetter; }	@Override public void setSchematicLetter(String value){ schematicLetter = value; }
	private String	partType;		@Override public String getPartType()		{ return partType; }		@Override public void setPartType(String value)		{ partType = value; }
	private String	schematicPart;	@Override public String getSchematicPart()	{ return schematicPart; }	@Override public void setSchematicPart(String value){ schematicPart = value; }
	private Integer qty;			@Override public Integer getQty()			{ return qty; }				@Override public void setQty(Integer value)			{ qty = value; }
	private String	value;			@Override public String getValue()			{ return value; }			@Override public void setValue(String value)		{ this.value = value; }
	private String	voltage;		@Override public String getVoltage()		{ return voltage; }			@Override public void setVoltage(String voltage)	{ this.voltage = voltage; 	}

	@Override
	public String toString() {
		return "\n\t IrtComponentEntity [id=" + id + ", partNumber=" + partNumber + ", manufPartNumber=" + manufPartNumber
				+ ", description=" + description + ", manufacture=" + manufacture + ", qty=" + qty + ", location="
				+ location + ", link=" + link + ", footprint=" + footprint + ", schematicLetter=" + schematicLetter
				+ ", partType=" + partType + ", schematicPart=" + schematicPart + "]";
	}
}
