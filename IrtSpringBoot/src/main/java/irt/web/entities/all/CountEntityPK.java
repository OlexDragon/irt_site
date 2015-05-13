/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package irt.web.entities.all;

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
public class CountEntityPK implements Serializable {
    private static final long serialVersionUID = 1L;

    @Basic(optional = false)
    @NotNull
    @Column(name = "id")
    private int id;

    @Basic(optional = false)
    @NotNull
    @Column(name = "class_Id")
    private int classId;

    public CountEntityPK() {
    }

    public CountEntityPK(int id, int classId) {
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
        int hash = 0;
        hash += (int) id;
        hash += (int) classId;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CountEntityPK)) {
            return false;
        }
        CountEntityPK other = (CountEntityPK) object;
        if (this.id != other.id) {
            return false;
        }
        if (this.classId != other.classId) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "irt.web.entities.CountEntityPK[ id=" + id + ", classId=" + classId + " ]";
    }
    
}
