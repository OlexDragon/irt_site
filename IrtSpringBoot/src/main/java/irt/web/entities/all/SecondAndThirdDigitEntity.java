/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package irt.web.entities.all;

import irt.web.workers.beans.interfaces.ValueText;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

/**
 *
 * @author Oleksandr
 */
@Entity
@Table(name = "second_and_third_digit", catalog = "irt", schema = "")
@XmlRootElement
public class SecondAndThirdDigitEntity implements Serializable, ValueText {
    private static final long serialVersionUID = 1L;

    @EmbeddedId
    protected SecondAndThirdDigitEntityPK secondAndThirdDigitEntityPK;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "description")
    private String description;

    @JoinColumn(name = "id_first_digits", referencedColumnName = "id_first_digits", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private FirstDigitsEntity firstDigitsEntity;

    @JoinColumn(name = "class_id", referencedColumnName = "class_id")
    @OneToOne(optional = true, fetch=FetchType.EAGER)
    @NotFound(action = NotFoundAction.IGNORE)
    private ClassIdHasArrayEntity classIdHasArrayEntity;

    public SecondAndThirdDigitEntity() {
    }

    public SecondAndThirdDigitEntity(SecondAndThirdDigitEntityPK secondAndThirdDigitEntityPK) {
        this.secondAndThirdDigitEntityPK = secondAndThirdDigitEntityPK;
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

    public FirstDigitsEntity getFirstDigitsEntity() {
        return firstDigitsEntity;
    }

    public void setFirstDigitsEntity(FirstDigitsEntity firstDigitsEntity) {
        this.firstDigitsEntity = firstDigitsEntity;
    }    

    public ClassIdHasArrayEntity getClassIdHasArrayEntity() {
        return classIdHasArrayEntity;
    }

    public void setClassIdHasArrayEntity(ClassIdHasArrayEntity classIdHasArrayEntity) {
        this.classIdHasArrayEntity = classIdHasArrayEntity;
    }

	@Override
	public String getValue() {
		return secondAndThirdDigitEntityPK.getId();
	}

	@Override
	public String getText() {
		return description;
	}

    @Override
	public String toString() {
		return "SecondAndThirdDigitEntity [secondAndThirdDigitEntityPK="
				+ secondAndThirdDigitEntityPK + ", description=" + description
				+ ", classIdHasArrayEntity=" + classIdHasArrayEntity + "]";
	}
}
