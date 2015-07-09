/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package irt.web.entities.component;

import irt.web.entities.all.ManufactureEntity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

/**
 *
 * @author Oleksandr
 */
@Entity
@Table(name = "components", catalog = "irt", schema = "")
@XmlRootElement
public class ComponentEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    private Long id;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 25)
    private String partNumber;

    @Size(max = 35)
    private String manufPartNumber;

    @Size(max = 50)
    private String description;

    @Size(max = 45)
    @Column(name = "value")
    private String componentValue;

    @Size(max = 45)
    private String voltage;

    @Size(max = 45)
    private String partType;

    @Size(max = 10)
    private String schematicLetter;

    @Size(max = 255)
    private String schematicPart;

    @Size(max = 150)
    private String footprint;

    @Size(max = 30)
    private String location;

    private Integer qty;

	@NotFound(action = NotFoundAction.IGNORE)
    @JoinColumn(name = "manuf_id", referencedColumnName = "id", nullable = true)
    @ManyToOne(optional = true, fetch = FetchType.EAGER)
    private ManufactureEntity manufacture;

	@NotFound(action = NotFoundAction.IGNORE)
    @JoinColumn(name = "link", referencedColumnName = "id", nullable = true)
    @ManyToOne(optional = true, fetch = FetchType.EAGER)
    private LinkEntity link;

	@NotFound(action = NotFoundAction.IGNORE)
    @OneToMany(mappedBy = "componentEntity", fetch = FetchType.EAGER)
    private List<ComponentsOldSchematicLetterEntity> oldSchematicLetterEntities;

	@NotFound(action = NotFoundAction.IGNORE)
    @OneToMany(mappedBy = "componentEntity", fetch = FetchType.EAGER)
    private List<MovementDetailsEntity> movementDetailsEntities;

    public ComponentEntity() {
    }

    public ComponentEntity(Long id) {
        this.id = id;
    }

    public ComponentEntity(Long id, String partNumber) {
        this.id = id;
        this.partNumber = partNumber;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPartNumber() {
        return partNumber;
    }

    public void setPartNumber(String partNumber) {
        this.partNumber = partNumber;
    }

    public String getManufPartNumber() {
        return manufPartNumber;
    }

    public void setManufPartNumber(String manufPartNumber) {
        this.manufPartNumber = manufPartNumber;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ManufactureEntity getManufacture() {
        return manufacture;
    }

    public void setManufacture(ManufactureEntity mfr) {
        this.manufacture = mfr;
    }

    public String getComponentValue() {
        return componentValue;
    }

    public void setComponentValue(String componentValue) {
        this.componentValue = componentValue;
    }

    public String getVoltage() {
        return voltage;
    }

    public void setVoltage(String voltage) {
        this.voltage = voltage;
    }

    public String getPartType() {
        return partType;
    }

    public void setPartType(String partType) {
        this.partType = partType;
    }

    public String getSchematicLetter() {
        return schematicLetter;
    }

    public void setSchematicLetter(String schematicLetter) {
        this.schematicLetter = schematicLetter;
    }

    public String getSchematicPart() {
        return schematicPart;
    }

    public void setSchematicPart(String schematicPart) {
        this.schematicPart = schematicPart;
    }

    public String getFootprint() {
        return footprint;
    }

    public void setFootprint(String footprint) {
        this.footprint = footprint;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }

    public LinkEntity getLink() {
        return link;
    }

    public void setLink(LinkEntity link) {
        this.link = link;
    }

	public List<ComponentsOldSchematicLetterEntity> getOldSchematicLetterEntities() {
		return oldSchematicLetterEntities;
	}

	public void setOldSchematicLetterEntities(
			List<ComponentsOldSchematicLetterEntity> oldSchematicLetterEntities) {
		this.oldSchematicLetterEntities = oldSchematicLetterEntities;
	}


	public List<MovementDetailsEntity> getMovementDetailsEntities() {
		return movementDetailsEntities;
	}

	public void setMovementDetailsEntities(
			List<MovementDetailsEntity> movementDetailsEntities) {
		this.movementDetailsEntities = movementDetailsEntities;
	}

	@Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public boolean equals(Object object) {
       return object instanceof ComponentEntity ? object.hashCode()==hashCode() : false;
    }

    @Override
    public String toString() {
        return "\n\n\tComponentEntity{" + "\n\t\tid=" + id +
        		",partNumber=" + partNumber +
        		",tmfrPartNumber=" + manufPartNumber +
        		",description=" + description +
        		",\n\t\tmanufacture=" + manufacture +
        		",\n\t\tcomponentValue=" + componentValue +
        		",\n\tvoltage=" + voltage +
        		",partType=" + partType +
        		",schematicLetter=" + schematicLetter +
        		",schematicPart=" + schematicPart +
        		",footprint=" + footprint +
        		",\n\t\tlocation=" + location +
        		",\n\tqty=" + qty +
        		",\n\t\tlink=" + link +
        		"\n\t\tmovementDetailsEntities=" + movementDetailsEntities + "}";
    }
}
