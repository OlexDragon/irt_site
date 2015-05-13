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
public class KitDetailsEntityPK implements Serializable {
    private static final long serialVersionUID = 1L;

    @Basic(optional = false)
    @NotNull
    @Column(name = "id_kit")
    private int idKit;

    @Basic(optional = false)
    @NotNull
    @Column(name = "id_components")
    private int idComponents;

    public KitDetailsEntityPK() {
    }

    public KitDetailsEntityPK(int idKit, int idComponents) {
        this.idKit = idKit;
        this.idComponents = idComponents;
    }

    public int getIdKit() {
        return idKit;
    }

    public void setIdKit(int idKit) {
        this.idKit = idKit;
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
        hash += (int) idKit;
        hash += (int) idComponents;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof KitDetailsEntityPK)) {
            return false;
        }
        KitDetailsEntityPK other = (KitDetailsEntityPK) object;
        if (this.idKit != other.idKit) {
            return false;
        }
        if (this.idComponents != other.idComponents) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "irt.web.entities.KitDetailsEntityPK[ idKit=" + idKit + ", idComponents=" + idComponents + " ]";
    }
    
}
