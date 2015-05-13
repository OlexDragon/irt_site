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
import javax.validation.constraints.Size;

/**
 *
 * @author Oleksandr
 */
@Embeddable
public class ComponentsAlternativeEntityPK implements Serializable {
    private static final long serialVersionUID = 1L;

    @Basic(optional = false)
    @NotNull
    @Column(name = "id_components")
    private int idComponents;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 25)
    @Column(name = "alt_mfr_part_number")
    private String altMfrPartNumber;

    public ComponentsAlternativeEntityPK() {
    }

    public ComponentsAlternativeEntityPK(int idComponents, String altMfrPartNumber) {
        this.idComponents = idComponents;
        this.altMfrPartNumber = altMfrPartNumber;
    }

    public int getIdComponents() {
        return idComponents;
    }

    public void setIdComponents(int idComponents) {
        this.idComponents = idComponents;
    }

    public String getAltMfrPartNumber() {
        return altMfrPartNumber;
    }

    public void setAltMfrPartNumber(String altMfrPartNumber) {
        this.altMfrPartNumber = altMfrPartNumber;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) idComponents;
        hash += (altMfrPartNumber != null ? altMfrPartNumber.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ComponentsAlternativeEntityPK)) {
            return false;
        }
        ComponentsAlternativeEntityPK other = (ComponentsAlternativeEntityPK) object;
        if (this.idComponents != other.idComponents) {
            return false;
        }
        if ((this.altMfrPartNumber == null && other.altMfrPartNumber != null) || (this.altMfrPartNumber != null && !this.altMfrPartNumber.equals(other.altMfrPartNumber))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "irt.web.entities.ComponentsAlternativeEntityPK[ idComponents=" + idComponents + ", altMfrPartNumber=" + altMfrPartNumber + " ]";
    }
    
}
