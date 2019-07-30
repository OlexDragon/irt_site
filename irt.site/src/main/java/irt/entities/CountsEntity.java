/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package irt.entities;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Oleksandr
 */
@Entity
@Table(name = "counts")
@NamedQueries({
    @NamedQuery(name = "CountsEntity.findAll", query = "SELECT c FROM CountsEntity c")})
public class CountsEntity implements Serializable {
	private static final long serialVersionUID = 3568485781096653868L;

	@EmbeddedId
    protected CountsEntityPK key;

	@Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "description")
    private String description;

	public CountsEntity() {
    }

    public CountsEntity(CountsEntityPK countsEntityPK) {
        this.key = countsEntityPK;
    }

    public CountsEntity(CountsEntityPK countsEntityPK, String description) {
        this.key = countsEntityPK;
        this.description = description;
    }

    public CountsEntity(int id, int classId) {
        this.key = new CountsEntityPK(id, classId);
    }

    public CountsEntityPK getKey() {
        return key;
    }

    public void setKey(CountsEntityPK countsEntityPK) {
        this.key = countsEntityPK;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

	@Override
	public int hashCode() {
		return 31 + ((key == null) ? 0 : key.hashCode());
	}

    @Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CountsEntity other = (CountsEntity) obj;
		if (key == null) {
			if (other.key != null)
				return false;
		} else if (!key.equals(other.key))
			return false;
		return true;
	}

    @Override
    public String toString() {
        return "irt.entities.CountsEntity[ countsEntityPK=" + key + " ]";
    }
    
}
