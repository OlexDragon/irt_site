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
public class CompaniesAddressEntityPK implements Serializable {
    private static final long serialVersionUID = 1L;

    @Basic(optional = false)
    @NotNull
    @Column(name = "id_companies")
    private int idCompanies;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "address")
    private String address;

    public CompaniesAddressEntityPK() {
    }

    public CompaniesAddressEntityPK(int idCompanies, String address) {
        this.idCompanies = idCompanies;
        this.address = address;
    }

    public int getIdCompanies() {
        return idCompanies;
    }

    public void setIdCompanies(int idCompanies) {
        this.idCompanies = idCompanies;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) idCompanies;
        hash += (address != null ? address.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CompaniesAddressEntityPK)) {
            return false;
        }
        CompaniesAddressEntityPK other = (CompaniesAddressEntityPK) object;
        if (this.idCompanies != other.idCompanies) {
            return false;
        }
        if ((this.address == null && other.address != null) || (this.address != null && !this.address.equals(other.address))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "irt.web.entities.CompaniesAddressEntityPK[ idCompanies=" + idCompanies + ", address=" + address + " ]";
    }
    
}
