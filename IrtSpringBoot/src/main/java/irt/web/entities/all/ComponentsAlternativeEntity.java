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
@Table(name = "components_alternative", catalog = "irt", schema = "")
@XmlRootElement
public class ComponentsAlternativeEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected ComponentsAlternativeEntityPK componentsAlternativeEntityPK;
    @Basic(optional = false)
    @Column(name = "id")
    private int id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2)
    @Column(name = "manuf_id")
    private String manufId;
    @Column(name = "active")
    private Short active;

    public ComponentsAlternativeEntity() {
    }

    public ComponentsAlternativeEntity(ComponentsAlternativeEntityPK componentsAlternativeEntityPK) {
        this.componentsAlternativeEntityPK = componentsAlternativeEntityPK;
    }

    public ComponentsAlternativeEntity(ComponentsAlternativeEntityPK componentsAlternativeEntityPK, int id, String manufId) {
        this.componentsAlternativeEntityPK = componentsAlternativeEntityPK;
        this.id = id;
        this.manufId = manufId;
    }

    public ComponentsAlternativeEntity(int idComponents, String altMfrPartNumber) {
        this.componentsAlternativeEntityPK = new ComponentsAlternativeEntityPK(idComponents, altMfrPartNumber);
    }

    public ComponentsAlternativeEntityPK getComponentsAlternativeEntityPK() {
        return componentsAlternativeEntityPK;
    }

    public void setComponentsAlternativeEntityPK(ComponentsAlternativeEntityPK componentsAlternativeEntityPK) {
        this.componentsAlternativeEntityPK = componentsAlternativeEntityPK;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getManufId() {
        return manufId;
    }

    public void setManufId(String manufId) {
        this.manufId = manufId;
    }

    public Short getActive() {
        return active;
    }

    public void setActive(Short active) {
        this.active = active;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (componentsAlternativeEntityPK != null ? componentsAlternativeEntityPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ComponentsAlternativeEntity)) {
            return false;
        }
        ComponentsAlternativeEntity other = (ComponentsAlternativeEntity) object;
        if ((this.componentsAlternativeEntityPK == null && other.componentsAlternativeEntityPK != null) || (this.componentsAlternativeEntityPK != null && !this.componentsAlternativeEntityPK.equals(other.componentsAlternativeEntityPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "irt.web.entities.ComponentsAlternativeEntity[ componentsAlternativeEntityPK=" + componentsAlternativeEntityPK + " ]";
    }
    
}
