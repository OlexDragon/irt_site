package irt.web.entities.company;

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
public class CompaniesTelephoneEntityPK implements Serializable {
    private static final long serialVersionUID = 1L;

    @Basic(optional = false)
    @NotNull
    @Column(name = "id_companies")
    private int idCompanies;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 14)
    @Column(name = "telephone")
    private String telephone;

    public CompaniesTelephoneEntityPK() {
    }

    public CompaniesTelephoneEntityPK(int idCompanies, String telephone) {
        this.idCompanies = idCompanies;
        this.telephone = telephone;
    }

    public int getIdCompanies() {
        return idCompanies;
    }

    public void setIdCompanies(int idCompanies) {
        this.idCompanies = idCompanies;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) idCompanies;
        hash += (telephone != null ? telephone.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CompaniesTelephoneEntityPK)) {
            return false;
        }
        CompaniesTelephoneEntityPK other = (CompaniesTelephoneEntityPK) object;
        if (this.idCompanies != other.idCompanies) {
            return false;
        }
        if ((this.telephone == null && other.telephone != null) || (this.telephone != null && !this.telephone.equals(other.telephone))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "irt.web.entities.CompaniesTelephoneEntityPK[ idCompanies=" + idCompanies + ", telephone=" + telephone + " ]";
    }
    
}
