/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package irt.web.entities.all;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Oleksandr
 */
@Entity
@Table(name = "second_and_third_digit", catalog = "irt", schema = "")
@XmlRootElement
public class SecondAndThirdDigitEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected SecondAndThirdDigitEntityPK secondAndThirdDigitEntityPK;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "description")
    private String description;
    @Basic(optional = false)
    @Column(name = "class_id")
    private int classId;

    public SecondAndThirdDigitEntity() {
    }

    public SecondAndThirdDigitEntity(SecondAndThirdDigitEntityPK secondAndThirdDigitEntityPK) {
        this.secondAndThirdDigitEntityPK = secondAndThirdDigitEntityPK;
    }

    public SecondAndThirdDigitEntity(SecondAndThirdDigitEntityPK secondAndThirdDigitEntityPK, String description, int classId) {
        this.secondAndThirdDigitEntityPK = secondAndThirdDigitEntityPK;
        this.description = description;
        this.classId = classId;
    }

    public SecondAndThirdDigitEntity(String id, int idFirstDigits) {
        this.secondAndThirdDigitEntityPK = new SecondAndThirdDigitEntityPK(id, idFirstDigits);
    }

    public SecondAndThirdDigitEntityPK getSecondAndThirdDigitEntityPK() {
        return secondAndThirdDigitEntityPK;
    }

    public void setSecondAndThirdDigitEntityPK(SecondAndThirdDigitEntityPK secondAndThirdDigitEntityPK) {
        this.secondAndThirdDigitEntityPK = secondAndThirdDigitEntityPK;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getClassId() {
        return classId;
    }

    public void setClassId(int classId) {
        this.classId = classId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (secondAndThirdDigitEntityPK != null ? secondAndThirdDigitEntityPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SecondAndThirdDigitEntity)) {
            return false;
        }
        SecondAndThirdDigitEntity other = (SecondAndThirdDigitEntity) object;
        if ((this.secondAndThirdDigitEntityPK == null && other.secondAndThirdDigitEntityPK != null) || (this.secondAndThirdDigitEntityPK != null && !this.secondAndThirdDigitEntityPK.equals(other.secondAndThirdDigitEntityPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "irt.web.entities.SecondAndThirdDigitEntity[ secondAndThirdDigitEntityPK=" + secondAndThirdDigitEntityPK + " ]";
    }
    
}
