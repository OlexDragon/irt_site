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
public class CompaniesFaxEntityPK implements Serializable {
    private static final long serialVersionUID = 1L;

    @Basic(optional = false)
    @NotNull
    @Column(name = "id_companies")
    private int idCompanies;

    // @Pattern(regexp="^\\(?(\\d{3})\\)?[- ]?(\\d{3})[- ]?(\\d{4})$", message="Invalid phone/fax format, should be as xxx-xxx-xxxx")//if the field contains phone or fax number consider using this annotation to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "fax")
    private String fax;

    public CompaniesFaxEntityPK() {
    }

    public CompaniesFaxEntityPK(int idCompanies, String fax) {
        this.idCompanies = idCompanies;
        this.fax = fax;
    }

    public int getIdCompanies() {
        return idCompanies;
    }

    public void setIdCompanies(int idCompanies) {
        this.idCompanies = idCompanies;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) idCompanies;
        hash += (fax != null ? fax.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CompaniesFaxEntityPK)) {
            return false;
        }
        CompaniesFaxEntityPK other = (CompaniesFaxEntityPK) object;
        if (this.idCompanies != other.idCompanies) {
            return false;
        }
        if ((this.fax == null && other.fax != null) || (this.fax != null && !this.fax.equals(other.fax))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "irt.web.entities.CompaniesFaxEntityPK[ idCompanies=" + idCompanies + ", fax=" + fax + " ]";
    }
    
}
