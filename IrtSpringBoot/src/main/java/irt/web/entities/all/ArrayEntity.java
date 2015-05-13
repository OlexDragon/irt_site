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
@Table(name = "arrays", catalog = "irt", schema = "")
@XmlRootElement
public class ArrayEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected ArrayEntityPK arrayEntityPK;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "description")
    private String description;
    @Column(name = "sequence")
    private Short sequence;

    public ArrayEntity() {
    }

    public ArrayEntity(ArrayEntityPK arrayEntityPK) {
        this.arrayEntityPK = arrayEntityPK;
    }

    public ArrayEntity(ArrayEntityPK arrayEntityPK, String description) {
        this.arrayEntityPK = arrayEntityPK;
        this.description = description;
    }

    public ArrayEntity(String name, String id) {
        this.arrayEntityPK = new ArrayEntityPK(name, id);
    }

    public ArrayEntityPK getArrayEntityPK() {
        return arrayEntityPK;
    }

    public void setArrayEntityPK(ArrayEntityPK arrayEntityPK) {
        this.arrayEntityPK = arrayEntityPK;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Short getSequence() {
        return sequence;
    }

    public void setSequence(Short sequence) {
        this.sequence = sequence;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (arrayEntityPK != null ? arrayEntityPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ArrayEntity)) {
            return false;
        }
        ArrayEntity other = (ArrayEntity) object;
        if ((this.arrayEntityPK == null && other.arrayEntityPK != null) || (this.arrayEntityPK != null && !this.arrayEntityPK.equals(other.arrayEntityPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "irt.web.entities.ArrayEntity[ arrayEntityPK=" + arrayEntityPK + " ]";
    }
    
}
