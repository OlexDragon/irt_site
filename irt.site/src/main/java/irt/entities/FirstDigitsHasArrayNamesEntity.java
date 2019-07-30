/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package irt.entities;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Oleksandr
 */
@Entity
@Table(name = "first_digits_has_array_names")
@XmlRootElement
public class FirstDigitsHasArrayNamesEntity implements Serializable {
    private static final long serialVersionUID = 1L;
 
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_first_digits")
    private Integer id;
    public Integer getId() { return id; }
    public void setId(Integer idFirstDigits) { this.id = idFirstDigits; }

    @OneToOne(optional = false)
    @JoinColumn(name = "id_first_digits", referencedColumnName = "id_first_digits", insertable = false, updatable = false)
    private FirstDigitsEntity firstDigitsEntity;
    public FirstDigitsEntity getFirstDigitsEntity() {  return firstDigitsEntity; }
    public void setFirstDigitsEntity(FirstDigitsEntity firstDigitsEntity) { this.firstDigitsEntity = firstDigitsEntity; }

    @OneToOne(optional = false)
    @JoinColumn(name = "id_array_names", referencedColumnName = "id_array_names")
    private ArrayNameEntity arrayNameEntity;
    public ArrayNameEntity getArrayNameEntity() { return arrayNameEntity; }
	public void setArrayNameEntity(ArrayNameEntity arrayNameEntity) { this.arrayNameEntity = arrayNameEntity; }

	public FirstDigitsHasArrayNamesEntity() {
    }

    @Override
    public int hashCode() {
        return 31 + (id != null ? id.hashCode() : 0);
    }

    @Override
    public boolean equals(Object obj) {
        return this==obj || (obj instanceof FirstDigitsHasArrayNamesEntity && obj.hashCode()==hashCode());
    }    
}
