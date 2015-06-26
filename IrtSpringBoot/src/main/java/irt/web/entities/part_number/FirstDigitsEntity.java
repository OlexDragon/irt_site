/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package irt.web.entities.part_number;

import irt.web.workers.beans.interfaces.ValueText;

import java.io.Serializable;
import java.util.Collection;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 *
 * @author Oleksandr
 */
@Entity
@Table(name = "first_digits", catalog = "irt", schema = "")
@XmlRootElement
public class FirstDigitsEntity implements Serializable, ValueText {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_first_digits")
    private Integer firstDigitsId;

    @Basic(optional = false)
    @NotNull
    @Column(name = "part_numbet_first_char")
    private Character partNumbetFirstChar;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "description")
    private String description;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "firstDigitsEntity")
    private Collection<SecondAndThirdDigitEntity> secondAndThirdDigitEntityCollection;

    public FirstDigitsEntity() {
    }

    public FirstDigitsEntity(Integer idFirstDigits) {
        this.firstDigitsId = idFirstDigits;
    }

    public FirstDigitsEntity(Integer idFirstDigits, Character partNumbetFirstChar, String description) {
        this.firstDigitsId = idFirstDigits;
        this.partNumbetFirstChar = partNumbetFirstChar;
        this.description = description;
    }

    public Integer getFirstDigitId() {
        return firstDigitsId;
    }

    public void setFirstDigitsId(Integer idFirstDigits) {
        this.firstDigitsId = idFirstDigits;
    }

    public Character getPartNumbetFirstChar() {
        return partNumbetFirstChar;
    }

    public void setPartNumbetFirstChar(Character partNumbetFirstChar) {
        this.partNumbetFirstChar = partNumbetFirstChar;
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
        hash += (firstDigitsId != null ? firstDigitsId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FirstDigitsEntity)) {
            return false;
        }
        FirstDigitsEntity other = (FirstDigitsEntity) object;
        if ((this.firstDigitsId == null && other.firstDigitsId != null) || (this.firstDigitsId != null && !this.firstDigitsId.equals(other.firstDigitsId))) {
            return false;
        }
        return true;
    }

	@Override
	public String getValue() {
		return firstDigitsId.toString();
	}

	@Override
	public String getText() {
		return description;
	}

    @XmlTransient
    public Collection<SecondAndThirdDigitEntity> getSecondAndThirdDigitEntityCollection() {
        return secondAndThirdDigitEntityCollection;
    }

    public void setSecondAndThirdDigitEntityCollection(Collection<SecondAndThirdDigitEntity> secondAndThirdDigitEntityCollection) {
        this.secondAndThirdDigitEntityCollection = secondAndThirdDigitEntityCollection;
    }

    @Override
	public String toString() {
		return "\n\tFirstDigitsEntity [idFirstDigits=" + firstDigitsId
				+ ", partNumbetFirstChar=" + partNumbetFirstChar
				+ ", description=" + description
				+ ", secondAndThirdDigitEntityCollection="
				+ secondAndThirdDigitEntityCollection + "]";
	}
}
