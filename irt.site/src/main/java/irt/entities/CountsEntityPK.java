/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package irt.entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

/**
 *
 * @author Oleksandr
 */
@Embeddable
public class CountsEntityPK implements Serializable {
	private static final long serialVersionUID = 8050576926696519921L;

	@Basic(optional = false)
    @NotNull
    @Column(name = "id")
    private int id;

	@Basic(optional = false)
    @NotNull
    @Column(name = "class_Id")
    private int classId;

    public CountsEntityPK() {
    }

    public CountsEntityPK(int id, int classId) {
        this.id = id;
        this.classId = classId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getClassId() {
        return classId;
    }

    public void setClassId(int classId) {
        this.classId = classId;
    }

    @Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + classId;
		result = prime * result + id;
		return result;
	}

    @Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CountsEntityPK other = (CountsEntityPK) obj;
		if (classId != other.classId)
			return false;
		if (id != other.id)
			return false;
		return true;
	}

    @Override
    public String toString() {
        return "irt.entities.CountsEntityPK[ id=" + id + ", classId=" + classId + " ]";
    }
    
}
