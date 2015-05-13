package irt.web.entities.company;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Oleksandr
 */
@Entity
@Table(name = "companies_components", catalog = "irt", schema = "")
@XmlRootElement
public class CompaniesComponentsEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected CompaniesComponentsEntityPK companiesComponentsEntityPK;
    @Basic(optional = false)
    @NotNull
    @Column(name = "qty")
    private int qty;

    public CompaniesComponentsEntity() {
    }

    public CompaniesComponentsEntity(CompaniesComponentsEntityPK companiesComponentsEntityPK) {
        this.companiesComponentsEntityPK = companiesComponentsEntityPK;
    }

    public CompaniesComponentsEntity(CompaniesComponentsEntityPK companiesComponentsEntityPK, int qty) {
        this.companiesComponentsEntityPK = companiesComponentsEntityPK;
        this.qty = qty;
    }

    public CompaniesComponentsEntity(int idCompanies, int idComponents) {
        this.companiesComponentsEntityPK = new CompaniesComponentsEntityPK(idCompanies, idComponents);
    }

    public CompaniesComponentsEntityPK getCompaniesComponentsEntityPK() {
        return companiesComponentsEntityPK;
    }

    public void setCompaniesComponentsEntityPK(CompaniesComponentsEntityPK companiesComponentsEntityPK) {
        this.companiesComponentsEntityPK = companiesComponentsEntityPK;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (companiesComponentsEntityPK != null ? companiesComponentsEntityPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CompaniesComponentsEntity)) {
            return false;
        }
        CompaniesComponentsEntity other = (CompaniesComponentsEntity) object;
        if ((this.companiesComponentsEntityPK == null && other.companiesComponentsEntityPK != null) || (this.companiesComponentsEntityPK != null && !this.companiesComponentsEntityPK.equals(other.companiesComponentsEntityPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "irt.web.entities.CompaniesComponentsEntity[ companiesComponentsEntityPK=" + companiesComponentsEntityPK + " ]";
    }
    
}
