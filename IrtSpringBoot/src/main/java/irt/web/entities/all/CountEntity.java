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
@Table(name = "counts", catalog = "irt", schema = "")
@XmlRootElement
public class CountEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected CountEntityPK countEntityPK;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "description")
    private String description;

    public CountEntity() {
    }

    public CountEntity(CountEntityPK countEntityPK) {
        this.countEntityPK = countEntityPK;
    }

    public CountEntity(CountEntityPK countEntityPK, String description) {
        this.countEntityPK = countEntityPK;
        this.description = description;
    }

    public CountEntity(int id, int classId) {
        this.countEntityPK = new CountEntityPK(id, classId);
    }

    public CountEntityPK getCountEntityPK() {
        return countEntityPK;
    }

    public void setCountEntityPK(CountEntityPK countEntityPK) {
        this.countEntityPK = countEntityPK;
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
        hash += (countEntityPK != null ? countEntityPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CountEntity)) {
            return false;
        }
        CountEntity other = (CountEntity) object;
        if ((this.countEntityPK == null && other.countEntityPK != null) || (this.countEntityPK != null && !this.countEntityPK.equals(other.countEntityPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "irt.web.entities.CountEntity[ countEntityPK=" + countEntityPK + " ]";
    }
    
}
