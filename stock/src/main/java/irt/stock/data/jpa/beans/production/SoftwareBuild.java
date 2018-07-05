package irt.stock.data.jpa.beans.production;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import irt.stock.data.jpa.beans.production.ProductionUnit.UnitType;

@Entity
public class SoftwareBuild {

	protected SoftwareBuild(){}
	public SoftwareBuild(Date build, UnitType unitType) {
		this.build = build;
		this.unitType = unitType;
	}

	@Id @GeneratedValue @Column(name="id_software_build") private Long id;

	private Date build;

	@Enumerated(EnumType.ORDINAL)
	private UnitType unitType;

	public Long getId() { return id; } 
	public Date getBuild() { return build; } 
	public UnitType getUnitType() { return unitType; }

	@Override
	public String toString() {
		return "SoftwareBuild [id=" + id + ", build=" + build + ", unitType=" + unitType + "]";
	}
}
