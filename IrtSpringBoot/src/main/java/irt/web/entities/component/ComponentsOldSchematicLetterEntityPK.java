/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package irt.web.entities.component;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

/**
 *
 * @author Oleksandr
 */
@Embeddable
public class ComponentsOldSchematicLetterEntityPK implements Serializable {
    private static final long serialVersionUID = 1L;

    @Basic(optional = false)
    @NotNull
    @Column(name = "last_date")
    @Temporal(TemporalType.DATE)
    private Date lastDate;

    @Basic(optional = false)
    @NotNull
    @Column(name = "id_components")
    private int idComponents;

    public ComponentsOldSchematicLetterEntityPK() {
    }

    public ComponentsOldSchematicLetterEntityPK(Date lastDate, int idComponents) {
        this.lastDate = lastDate;
        this.idComponents = idComponents;
    }

    public Date getLastDate() {
        return lastDate;
    }

    public void setLastDate(Date lastDate) {
        this.lastDate = lastDate;
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
        hash += (lastDate != null ? lastDate.hashCode() : 0);
        hash += (int) idComponents;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ComponentsOldSchematicLetterEntityPK)) {
            return false;
        }
        ComponentsOldSchematicLetterEntityPK other = (ComponentsOldSchematicLetterEntityPK) object;
        if ((this.lastDate == null && other.lastDate != null) || (this.lastDate != null && !this.lastDate.equals(other.lastDate))) {
            return false;
        }
        if (this.idComponents != other.idComponents) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "irt.web.entities.ComponentsOldSchematicLetterEntityPK[ lastDate=" + lastDate + ", idComponents=" + idComponents + " ]";
    }
    
}
