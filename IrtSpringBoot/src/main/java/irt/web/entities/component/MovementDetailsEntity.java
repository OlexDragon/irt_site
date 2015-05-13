/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package irt.web.entities.component;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Oleksandr
 */
@Entity
@Table(name = "movement_details", catalog = "irt", schema = "")
@XmlRootElement
public class MovementDetailsEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @EmbeddedId
    protected MovementDetailsEntityPK movementDetailsEntityPK;

    @Basic(optional = false)
    @NotNull
    private int qty;

    private Integer oldQty;

    @JoinColumn(name = "idComponents", referencedColumnName = "id", insertable=false, updatable=false)
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private ComponentEntity componentEntity;

    @JoinColumn(name = "idMovement", referencedColumnName = "id", insertable=false, updatable=false)
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private MovementEntity movementEntity;

    public MovementDetailsEntity() {
    }

    public MovementDetailsEntity(MovementDetailsEntityPK movementDetailsEntityPK) {
        this.movementDetailsEntityPK = movementDetailsEntityPK;
    }

    public MovementDetailsEntity(MovementDetailsEntityPK movementDetailsEntityPK, int qty) {
        this.movementDetailsEntityPK = movementDetailsEntityPK;
        this.qty = qty;
    }

    public MovementDetailsEntity(int idMovement, int idComponents) {
        this.movementDetailsEntityPK = new MovementDetailsEntityPK(idMovement, idComponents);
    }

    public MovementDetailsEntityPK getMovementDetailsEntityPK() {
        return movementDetailsEntityPK;
    }

    public void setMovementDetailsEntityPK(MovementDetailsEntityPK movementDetailsEntityPK) {
        this.movementDetailsEntityPK = movementDetailsEntityPK;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public Integer getOldQty() {
        return oldQty;
    }

    public void setOldQty(Integer oldQty) {
        this.oldQty = oldQty;
    }

    public ComponentEntity getComponentEntity() {
		return componentEntity;
	}

	public void setComponentEntity(ComponentEntity componentEntity) {
		this.componentEntity = componentEntity;
	}

	public MovementEntity getMovementEntity() {
		return movementEntity;
	}

	public void setMovementEntity(MovementEntity movementEntity) {
		this.movementEntity = movementEntity;
	}

	@Override
    public int hashCode() {
        int hash = 0;
        hash += (movementDetailsEntityPK != null ? movementDetailsEntityPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MovementDetailsEntity)) {
            return false;
        }
        MovementDetailsEntity other = (MovementDetailsEntity) object;
        if ((this.movementDetailsEntityPK == null && other.movementDetailsEntityPK != null) || (this.movementDetailsEntityPK != null && !this.movementDetailsEntityPK.equals(other.movementDetailsEntityPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "irt.web.entities.MovementDetailsEntity[ movementDetailsEntityPK=" + movementDetailsEntityPK + " ]";
    }
    
}
