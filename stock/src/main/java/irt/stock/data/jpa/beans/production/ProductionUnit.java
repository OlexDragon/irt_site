package irt.stock.data.jpa.beans.production;

import java.util.Arrays;
import java.util.Optional;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import irt.stock.data.jpa.beans.PartNumber;

@Entity
public class ProductionUnit {

	protected ProductionUnit(){}

	public ProductionUnit(String serialNumber, String description, PartNumber partNumber, SoftwareBuild softwareBuild, UnitType unitType, String data, String included) {
		this.serialNumber = serialNumber;
		this.partNumber = partNumber;
		this.softwareBuild = softwareBuild;
		this.unitType = unitType;
		this.data = data;
		this.included = included;
		setDescription(description);
	}

	@Id @GeneratedValue @Column(name="id_production_unit") private Long id;

	private String serialNumber;
	private String description;

	@ManyToOne
	@JoinColumn(name="idComponents")
	private PartNumber partNumber;

	@ManyToOne
	@JoinColumn(name="id_software_build")
	private SoftwareBuild softwareBuild;

	@Enumerated(EnumType.ORDINAL)
	private UnitType unitType;

	@Column(columnDefinition = "LONGTEXT")
	private String data;

	private String included;

	public Long 			getId() 			{ return id; }
	public String 			getDescription() 	{ return description; }
	public String 			getSerialNumber() 	{ return serialNumber; }
	public PartNumber 		getPartNumber() 	{ return partNumber; }
	public SoftwareBuild 	getSoftwareBuild() 	{ return softwareBuild; }
	public UnitType 		getUnitType() 		{ return unitType; }
	public String 			getData() 			{ return data; }
	public String 			getIncluded() 		{ return included; }

	public void setPartNumber(PartNumber partNumber) {
		this.partNumber = partNumber;
	}

	public void setSoftwareBuild(SoftwareBuild softwareBuild) {
		this.softwareBuild = softwareBuild;
	}

	public void setDescription(String description) {
		this.description = Optional.ofNullable(description).filter(d->d.length()>255).map(d->d.substring(0, 252) + "...").orElse(description);
	}

	public void setIncluded(String included) {
		this.included = included;
	}

	public boolean addIncluded(ProductionUnit subUnit){
		Long productionUnitId = subUnit.getId();

		String pus = Optional
							.ofNullable(included)
							.map(str->str.split(","))
							.map(
									array->
									Arrays
									.stream(array)
									.filter(i->i.equals(productionUnitId.toString()))
									.findAny()
									.map(s->included)
									.orElse(included + ',' + productionUnitId))
							.orElse(productionUnitId.toString());

		if(included==null || !included.equals(pus)){
			setIncluded(pus);
			return true;
		}

		return false;
	}

	@Override
	public String toString() {
		return "ProductionUnit [id=" + id + ", serialNumber=" + serialNumber + ", partNumber=" + partNumber + ", softwareBuild=" + softwareBuild + ", unitType=" + unitType
				+ ", data=" + data + ", included=" + included + "]";
	}

	public enum UnitType{
		CONVERTER,
		BAIS_BOARD,
		CONTROLLER,
		NEW_CONVERTER,
		REFERENCE,
		MAIN_BOARD
	}
}
