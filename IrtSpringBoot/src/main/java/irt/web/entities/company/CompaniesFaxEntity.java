package irt.web.entities.company;

import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Oleksandr
 */
@Entity
@Table(name = "companies_fax", catalog = "irt", schema = "")
@XmlRootElement
public class CompaniesFaxEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected CompaniesFaxEntityPK companiesFaxEntityPK;

    public CompaniesFaxEntity() {
    }

    public CompaniesFaxEntity(CompaniesFaxEntityPK companiesFaxEntityPK) {
        this.companiesFaxEntityPK = companiesFaxEntityPK;
    }

    public CompaniesFaxEntity(int idCompanies, String fax) {
        this.companiesFaxEntityPK = new CompaniesFaxEntityPK(idCompanies, fax);
    }

    public CompaniesFaxEntityPK getCompaniesFaxEntityPK() {
        return companiesFaxEntityPK;
    }

    public void setCompaniesFaxEntityPK(CompaniesFaxEntityPK companiesFaxEntityPK) {
        this.companiesFaxEntityPK = companiesFaxEntityPK;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (companiesFaxEntityPK != null ? companiesFaxEntityPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CompaniesFaxEntity)) {
            return false;
        }
        CompaniesFaxEntity other = (CompaniesFaxEntity) object;
        if ((this.companiesFaxEntityPK == null && other.companiesFaxEntityPK != null) || (this.companiesFaxEntityPK != null && !this.companiesFaxEntityPK.equals(other.companiesFaxEntityPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "irt.web.entities.CompaniesFaxEntity[ companiesFaxEntityPK=" + companiesFaxEntityPK + " ]";
    }
    
}
