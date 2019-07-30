/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package irt.entities;

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
        return 31 + (id != null ? id.hashCode() : 0);
    }

    @Override
    public boolean equals(Object obj) {
        return this==obj || (obj instanceof SecondAndThirdDigitPK && obj.hashCode()==hashCode());
    }

    @Override
	public String toString() {
		return "\n\tSecondAndThirdDigitPK [id=" + id + ", idFirstDigit=" + idFirstDigit + "]";
	}
    
}
