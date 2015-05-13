package irt.web.entities.company;

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
public class CompaniesComponentsEntityPK implements Serializable {
    private static final long serialVersionUID = 1L;

    @Basic(optional = false)
    @NotNull
    @Column(name = "id_companies")
    private int idCompanies;

    @Basic(optional = false)
    @NotNull
    @Column(name = "id_components")
    private int idComponents;

    public CompaniesComponentsEntityPK() {
    }

    public CompaniesComponentsEntityPK(int idCompanies, int idComponents) {
        this.idCompanies = idCompanies;
        this.idComponents = idComponents;
    }

    public int getIdCompanies() {
        return idCompanies;
    }

    public void setIdCompanies(int idCompanies) {
        this.idCompanies = idCompanies;
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
        hash += (int) idCompanies;
        hash += (int) idComponents;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CompaniesComponentsEntityPK)) {
            return false;
        }
        CompaniesComponentsEntityPK other = (CompaniesComponentsEntityPK) object;
        if (this.idCompanies != other.idCompanies) {
            return false;
        }
        if (this.idComponents != other.idComponents) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "irt.web.entities.CompaniesComponentsEntityPK[ idCompanies=" + idCompanies + ", idComponents=" + idComponents + " ]";
    }
    
}
