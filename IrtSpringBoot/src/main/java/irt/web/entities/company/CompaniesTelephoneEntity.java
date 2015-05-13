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
@Table(name = "companies_telephone", catalog = "irt", schema = "")
@XmlRootElement
public class CompaniesTelephoneEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected CompaniesTelephoneEntityPK companiesTelephoneEntityPK;

    public CompaniesTelephoneEntity() {
    }

    public CompaniesTelephoneEntity(CompaniesTelephoneEntityPK companiesTelephoneEntityPK) {
        this.companiesTelephoneEntityPK = companiesTelephoneEntityPK;
    }

    public CompaniesTelephoneEntity(int idCompanies, String telephone) {
        this.companiesTelephoneEntityPK = new CompaniesTelephoneEntityPK(idCompanies, telephone);
    }

    public CompaniesTelephoneEntityPK getCompaniesTelephoneEntityPK() {
        return companiesTelephoneEntityPK;
    }

    public void setCompaniesTelephoneEntityPK(CompaniesTelephoneEntityPK companiesTelephoneEntityPK) {
        this.companiesTelephoneEntityPK = companiesTelephoneEntityPK;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (companiesTelephoneEntityPK != null ? companiesTelephoneEntityPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CompaniesTelephoneEntity)) {
            return false;
        }
        CompaniesTelephoneEntity other = (CompaniesTelephoneEntity) object;
        if ((this.companiesTelephoneEntityPK == null && other.companiesTelephoneEntityPK != null) || (this.companiesTelephoneEntityPK != null && !this.companiesTelephoneEntityPK.equals(other.companiesTelephoneEntityPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "irt.web.entities.CompaniesTelephoneEntity[ companiesTelephoneEntityPK=" + companiesTelephoneEntityPK + " ]";
    }
    
}
