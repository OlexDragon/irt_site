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
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
    protected ArrayEntityPK key;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "description")
    private String description;

    @Column(name = "sequence")
    private Short sequence;

	@JoinColumn(name = "name", referencedColumnName = "name", insertable = false, updatable = false)
    @ManyToOne(optional = true, fetch=FetchType.LAZY)
    private ClassIdHasArrayEntity hasArrayEntity;
 
    public ArrayEntity() {
    }

    public ArrayEntity(ArrayEntityPK arrayEntityPK) {
    	key = arrayEntityPK;
    }

    public ArrayEntity(ArrayEntityPK arrayEntityPK, String description) {
    	key = arrayEntityPK;
        this.description = description;
    }

    public ArrayEntity(String name, String id) {
    	key = new ArrayEntityPK(name, id);
    }

    public ArrayEntityPK getKey() {
        return key;
    }

    public void setKey(ArrayEntityPK arrayEntityPK) {
    	key = arrayEntityPK;
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

    public ClassIdHasArrayEntity getHasArrayEntity() {
        return hasArrayEntity;
    }

    public void setHasArrayEntity(ClassIdHasArrayEntity classIdHasArrayEntity) {
        this.hasArrayEntity = classIdHasArrayEntity;
    }

    @Override
    public int hashCode() {
        return key != null ? key.hashCode() : 0;
    }

    @Override
    public boolean equals(Object object) {
        return object instanceof ArrayEntity ? object.hashCode() == hashCode() : false;
    }

    @Override
	public String toString() {
		return "\n\tArrayEntity [key=" + key + ", description="
				+ description + ", sequence=" + sequence + "]";
	}
}
