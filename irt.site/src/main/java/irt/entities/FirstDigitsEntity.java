/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package irt.entities;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
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
public class FirstDigitsEntity implements Serializable{
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_first_digits")
    private Integer id;
    public Integer getId() { return id;}
    public void setId(Integer idFirstDigits) { this.id = idFirstDigits; }

    @Basic(optional = false)
    @NotNull
    @Column(name = "part_numbet_first_char")
    private Character partNumbetFirstChar;
    public Character getPartNumbetFirstChar() {  return partNumbetFirstChar;  }
    public void setPartNumbetFirstChar(Character partNumbetFirstChar) {  this.partNumbetFirstChar = partNumbetFirstChar; }

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "description")
    private String description;
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "firstDigitsEntity")
    private FirstDigitsHasArrayNamesEntity firstDigitsHasArrayNames;
    public FirstDigitsHasArrayNamesEntity getFirstDigitsHasArrayNames() { return firstDigitsHasArrayNames; }
	public void setFirstDigitsHasArrayNames(irt.entities.FirstDigitsHasArrayNamesEntity firstDigitsHasArrayNames) { this.firstDigitsHasArrayNames = firstDigitsHasArrayNames; }

	public FirstDigitsEntity() {
    }

    public FirstDigitsEntity(Integer idFirstDigits) {
        this.id = idFirstDigits;
    }

    public FirstDigitsEntity(Integer idFirstDigits, Character partNumbetFirstChar, String description) {
        this.id = idFirstDigits;
        this.partNumbetFirstChar = partNumbetFirstChar;
        this.description = description;
    }

    @Override
    public int hashCode() {
        return 31 + (id != null ? id.hashCode() : 0);
    }

    @Override
    public boolean equals(Object object) {
         return this == object || (object instanceof FirstDigitsEntity && object.hashCode()==hashCode());
    }

    @Override
	public String toString() {
		return "\n\tFirstDigitsEntity [firstDigitsId=" + id + ", partNumbetFirstChar=" + partNumbetFirstChar + ", description=" + description + "]";
	}
}
