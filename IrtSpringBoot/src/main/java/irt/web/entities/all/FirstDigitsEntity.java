/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package irt.web.entities.all;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Oleksandr
 */
@Entity
@Table(name = "first_digits", catalog = "irt", schema = "")
@XmlRootElement
public class FirstDigitsEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_first_digits")
    private Integer idFirstDigits;
    @Basic(optional = false)
    @NotNull
    @Column(name = "part_numbet_first_char")
    private Character partNumbetFirstChar;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "description")
    private String description;

    public FirstDigitsEntity() {
    }

    public FirstDigitsEntity(Integer idFirstDigits) {
        this.idFirstDigits = idFirstDigits;
    }

    public FirstDigitsEntity(Integer idFirstDigits, Character partNumbetFirstChar, String description) {
        this.idFirstDigits = idFirstDigits;
        this.partNumbetFirstChar = partNumbetFirstChar;
        this.description = description;
    }

    public Integer getIdFirstDigits() {
        return idFirstDigits;
    }

    public void setIdFirstDigits(Integer idFirstDigits) {
        this.idFirstDigits = idFirstDigits;
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
        hash += (idFirstDigits != null ? idFirstDigits.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FirstDigitsEntity)) {
            return false;
        }
        FirstDigitsEntity other = (FirstDigitsEntity) object;
        if ((this.idFirstDigits == null && other.idFirstDigits != null) || (this.idFirstDigits != null && !this.idFirstDigits.equals(other.idFirstDigits))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "irt.web.entities.FirstDigitsEntity[ idFirstDigits=" + idFirstDigits + " ]";
    }
    
}