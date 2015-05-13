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
public class CostEntityPK implements Serializable {
    private static final long serialVersionUID = 1L;

    @Basic(optional = false)
    @NotNull
    @Column(name = "id_components")
    private int idComponents;

    @Basic(optional = false)
    @NotNull
    @Column(name = "id_components_alternative")
    private int idComponentsAlternative;

    @Basic(optional = false)
    @NotNull
    @Column(name = "id_companies")
    private int idCompanies;

    @Basic(optional = false)
    @NotNull
    @Column(name = "for")
    private int for1;

    public CostEntityPK() {
    }

    public CostEntityPK(int idComponents, int idComponentsAlternative, int idCompanies, int for1) {
        this.idComponents = idComponents;
        this.idComponentsAlternative = idComponentsAlternative;
        this.idCompanies = idCompanies;
        this.for1 = for1;
    }

    public int getIdComponents() {
        return idComponents;
    }

    public void setIdComponents(int idComponents) {
        this.idComponents = idComponents;
    }

    public int getIdComponentsAlternative() {
        return idComponentsAlternative;
    }

    public void setIdComponentsAlternative(int idComponentsAlternative) {
        this.idComponentsAlternative = idComponentsAlternative;
    }

    public int getIdCompanies() {
        return idCompanies;
    }

    public void setIdCompanies(int idCompanies) {
        this.idCompanies = idCompanies;
    }

    public int getFor1() {
        return for1;
    }

    public void setFor1(int for1) {
        this.for1 = for1;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) idComponents;
        hash += (int) idComponentsAlternative;
        hash += (int) idCompanies;
        hash += (int) for1;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CostEntityPK)) {
            return false;
        }
        CostEntityPK other = (CostEntityPK) object;
        if (this.idComponents != other.idComponents) {
            return false;
        }
        if (this.idComponentsAlternative != other.idComponentsAlternative) {
            return false;
        }
        if (this.idCompanies != other.idCompanies) {
            return false;
        }
        if (this.for1 != other.for1) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "irt.web.entities.CostEntityPK[ idComponents=" + idComponents + ", idComponentsAlternative=" + idComponentsAlternative + ", idCompanies=" + idCompanies + ", for1=" + for1 + " ]";
    }
    
}
