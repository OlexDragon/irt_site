/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package irt.web.entities.component;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

/**
 *
 * @author Oleksandr
 */
@Embeddable
public class MovementDetailsEntityPK implements Serializable {
    private static final long serialVersionUID = 1L;

    @Basic(optional = false)
    @NotNull
    private int idMovement;

    @Basic(optional = false)
    @NotNull
    private int idComponents;

    public MovementDetailsEntityPK() {
    }

    public MovementDetailsEntityPK(int idMovement, int idComponents) {
        this.idMovement = idMovement;
        this.idComponents = idComponents;
    }

    public int getIdMovement() {
        return idMovement;
    }

    public void setIdMovement(int idMovement) {
        this.idMovement = idMovement;
    }

    public int getIdComponents() {
        return idComponents;
    }

    public void setIdComponents(int idComponents) {
        this.idComponents = idComponents;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) idMovement;
        hash += (int) idComponents;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MovementDetailsEntityPK)) {
            return false;
        }
        MovementDetailsEntityPK other = (MovementDetailsEntityPK) object;
        if (this.idMovement != other.idMovement) {
            return false;
        }
        if (this.idComponents != other.idComponents) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "irt.web.entities.MovementDetailsEntityPK[ idMovement=" + idMovement + ", idComponents=" + idComponents + " ]";
    }
    
}
