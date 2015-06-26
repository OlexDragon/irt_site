/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package irt.web.entities.part_number;

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
public class SecondAndThirdDigitPK implements Serializable {
    private static final long serialVersionUID = 1L;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2)
    @Column(name = "id")
    private String id;

    @Basic(optional = false)
    @NotNull
    @Column(name = "id_first_digits")
    private Integer idFirstDigit;

    public SecondAndThirdDigitPK() {
    }

    public SecondAndThirdDigitPK(String id, Integer idFirstDigits) {
        this.id = id;
        this.idFirstDigit = idFirstDigits;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getIdFirstDigit() {
        return idFirstDigit;
    }

    public void setIdFirstDigit(Integer idFirstDigits) {
        this.idFirstDigit = idFirstDigits;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        hash += (int) idFirstDigit;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SecondAndThirdDigitPK)) {
            return false;
        }
        SecondAndThirdDigitPK other = (SecondAndThirdDigitPK) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        if (this.idFirstDigit != other.idFirstDigit) {
            return false;
        }
        return true;
    }

    @Override
	public String toString() {
		return "SecondAndThirdDigitEntityPK [id=" + id + ", idFirstDigits="
				+ idFirstDigit + "]";
	}
    
}
