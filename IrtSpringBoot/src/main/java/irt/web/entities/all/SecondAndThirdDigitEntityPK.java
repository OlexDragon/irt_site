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
public class SecondAndThirdDigitEntityPK implements Serializable {
    private static final long serialVersionUID = 1L;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2)
    @Column(name = "id")
    private String id;

    @Basic(optional = false)
    @NotNull
    @Column(name = "id_first_digits")
    private int idFirstDigits;

    public SecondAndThirdDigitEntityPK() {
    }

    public SecondAndThirdDigitEntityPK(String id, int idFirstDigits) {
        this.id = id;
        this.idFirstDigits = idFirstDigits;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getIdFirstDigits() {
        return idFirstDigits;
    }

    public void setIdFirstDigits(int idFirstDigits) {
        this.idFirstDigits = idFirstDigits;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        hash += (int) idFirstDigits;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SecondAndThirdDigitEntityPK)) {
            return false;
        }
        SecondAndThirdDigitEntityPK other = (SecondAndThirdDigitEntityPK) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        if (this.idFirstDigits != other.idFirstDigits) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "irt.web.entities.SecondAndThirdDigitEntityPK[ id=" + id + ", idFirstDigits=" + idFirstDigits + " ]";
    }
    
}
