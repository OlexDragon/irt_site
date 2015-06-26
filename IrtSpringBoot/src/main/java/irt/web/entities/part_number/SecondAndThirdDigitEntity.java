/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package irt.web.entities.part_number;

import irt.web.entities.all.ClassIdHasArrayEntity;
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
    protected SecondAndThirdDigitPK key;

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
    private ClassIdHasArrayEntity hasArrayEntity;

    public SecondAndThirdDigitEntity() {
    }

    public SecondAndThirdDigitEntity(SecondAndThirdDigitPK secondAndThirdDigitEntityPK) {
        this.key = secondAndThirdDigitEntityPK;
    }

    public SecondAndThirdDigitEntity(String id, int idFirstDigits) {
        this.key = new SecondAndThirdDigitPK(id, idFirstDigits);
    }

    public SecondAndThirdDigitPK getKet() {
        return key;
    }

    public void setKey(SecondAndThirdDigitPK secondAndThirdDigitEntityPK) {
        this.key = secondAndThirdDigitEntityPK;
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
        hash += (key != null ? key.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SecondAndThirdDigitEntity)) {
            return false;
        }
        SecondAndThirdDigitEntity other = (SecondAndThirdDigitEntity) object;
        if ((this.key == null && other.key != null) || (this.key != null && !this.key.equals(other.key))) {
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

    public ClassIdHasArrayEntity getHasArrayEntity() {
        return hasArrayEntity;
    }

    public void setHasArrayEntity(ClassIdHasArrayEntity classIdHasArrayEntity) {
        this.hasArrayEntity = classIdHasArrayEntity;
    }

	@Override
	public String getValue() {
		return key.getId();
	}

	@Override
	public String getText() {
		return description;
	}

    @Override
	public String toString() {
		return "\n\tSecondAndThirdDigitEntity [key="
				+ key + ", description=" + description
				+ ", classIdHasArrayEntity=" + hasArrayEntity + "]";
	}
}
