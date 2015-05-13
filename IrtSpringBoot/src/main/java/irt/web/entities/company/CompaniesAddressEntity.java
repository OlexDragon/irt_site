/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
@Table(name = "companies_address", catalog = "irt", schema = "")
@XmlRootElement
public class CompaniesAddressEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected CompaniesAddressEntityPK companiesAddressEntityPK;

    public CompaniesAddressEntity() {
    }

    public CompaniesAddressEntity(CompaniesAddressEntityPK companiesAddressEntityPK) {
        this.companiesAddressEntityPK = companiesAddressEntityPK;
    }

    public CompaniesAddressEntity(int idCompanies, String address) {
        this.companiesAddressEntityPK = new CompaniesAddressEntityPK(idCompanies, address);
    }

    public CompaniesAddressEntityPK getCompaniesAddressEntityPK() {
        return companiesAddressEntityPK;
    }

    public void setCompaniesAddressEntityPK(CompaniesAddressEntityPK companiesAddressEntityPK) {
        this.companiesAddressEntityPK = companiesAddressEntityPK;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (companiesAddressEntityPK != null ? companiesAddressEntityPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CompaniesAddressEntity)) {
            return false;
        }
        CompaniesAddressEntity other = (CompaniesAddressEntity) object;
        if ((this.companiesAddressEntityPK == null && other.companiesAddressEntityPK != null) || (this.companiesAddressEntityPK != null && !this.companiesAddressEntityPK.equals(other.companiesAddressEntityPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "irt.web.entities.CompaniesAddressEntity[ companiesAddressEntityPK=" + companiesAddressEntityPK + " ]";
    }
    
}
