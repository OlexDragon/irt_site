/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package irt.web.entities.component;

import irt.web.workers.beans.interfaces.ValueText;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 *
 * @author Oleksandr
 */
@Entity
@Table(name = "place", catalog = "irt", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PlaceEntity.findAll", query = "SELECT p FROM PlaceEntity p"),
    @NamedQuery(name = "PlaceEntity.findByPlaceId", query = "SELECT p FROM PlaceEntity p WHERE p.placeId = :placeId"),
    @NamedQuery(name = "PlaceEntity.findByPlaceName", query = "SELECT p FROM PlaceEntity p WHERE p.placeName = :placeName")})
public class PlaceEntity implements Serializable, ValueText {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "place_id")
    private Long placeId;
 
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "place_name")
    private String placeName;

    @Basic(optional = true)
    @Size(max = 45)
    @Column(name = "table_name")
    private String tableName;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "to", fetch = FetchType.EAGER)
    private List<MovementEntity> movementEntityList;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "who", fetch = FetchType.EAGER)
    private List<MovementEntity> movementEntityList1;

    public PlaceEntity() {
    }

    public PlaceEntity(Long placeId) {
        this.placeId = placeId;
    }

    public PlaceEntity(Long placeId, String placeName) {
        this.placeId = placeId;
        this.placeName = placeName;
    }

    public Long getPlaceId() {
        return placeId;
    }

    public void setPlaceId(Long placeId) {
        this.placeId = placeId;
    }

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	@XmlTransient
    public List<MovementEntity> getMovementEntityList() {
        return movementEntityList;
    }

    public void setMovementEntityList(List<MovementEntity> movementEntityList) {
        this.movementEntityList = movementEntityList;
    }

    @XmlTransient
    public List<MovementEntity> getMovementEntityList1() {
        return movementEntityList1;
    }

    public void setMovementEntityList1(List<MovementEntity> movementEntityList1) {
        this.movementEntityList1 = movementEntityList1;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (placeId != null ? placeId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PlaceEntity)) {
            return false;
        }
        PlaceEntity other = (PlaceEntity) object;
        if ((this.placeId == null && other.placeId != null) || (this.placeId != null && !this.placeId.equals(other.placeId))) {
            return false;
        }
        return true;
    }

	@Override
	public String toString() {
		return "\n\tPlaceEntity [\n\t\t"
				+ "placeId=" + placeId + ",\n\t\t"
				+ "placeName=" + placeName + ",\n\t\t"
				+ "tableName=" + tableName + ",\n\t\t"
				+ "movementEntityList=" + movementEntityList + ",\n\t\t"
				+ "movementEntityList1=" + movementEntityList1 + "]";
	}

	@Override
	public String getValue() {
		return placeId.toString();
	}

	@Override
	public String getText() {
		return placeName;
	}
}
