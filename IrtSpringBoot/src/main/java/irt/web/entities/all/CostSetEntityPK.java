/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package irt.web.entities.all;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

/**
 *
 * @author Oleksandr
 */
@Embeddable
public class CostSetEntityPK implements Serializable {
    private static final long serialVersionUID = 1L;

    @Basic(optional = false)
    @NotNull
    @Column(name = "set_id")
    private int setId;

    @Basic(optional = false)
    @NotNull
    @Column(name = "id_top_component")
    private int idTopComponent;

    @Basic(optional = false)
    @NotNull
    @Column(name = "id_components")
    private int idComponents;

    public CostSetEntityPK() {
    }

    public CostSetEntityPK(int setId, int idTopComponent, int idComponents) {
        this.setId = setId;
        this.idTopComponent = idTopComponent;
        this.idComponents = idComponents;
    }

    public int getSetId() {
        return setId;
    }

    public void setSetId(int setId) {
        this.setId = setId;
    }

    public int getIdTopComponent() {
        return idTopComponent;
    }

    public void setIdTopComponent(int idTopComponent) {
        this.idTopComponent = idTopComponent;
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
        hash += (int) setId;
        hash += (int) idTopComponent;
        hash += (int) idComponents;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CostSetEntityPK)) {
            return false;
        }
        CostSetEntityPK other = (CostSetEntityPK) object;
        if (this.setId != other.setId) {
            return false;
        }
        if (this.idTopComponent != other.idTopComponent) {
            return false;
        }
        if (this.idComponents != other.idComponents) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "irt.web.entities.CostSetEntityPK[ setId=" + setId + ", idTopComponent=" + idTopComponent + ", idComponents=" + idComponents + " ]";
    }
    
}
