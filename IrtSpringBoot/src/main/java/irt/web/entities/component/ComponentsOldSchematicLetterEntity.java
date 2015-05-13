/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package irt.web.entities.component;

import java.io.Serializable;
import java.util.Date;

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
@Table(name = "components_old_schematic_letters", catalog = "irt", schema = "")
@XmlRootElement
public class ComponentsOldSchematicLetterEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @EmbeddedId
    protected ComponentsOldSchematicLetterEntityPK componentsOldSchematicLetterEntityPK;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "schematic_letter")
    private String schematicLetter;

    @Basic(optional = false)
    @NotNull
    @Column(name = "userid")
    private int userid;

    @JoinColumn(name = "id_components", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private ComponentEntity componentEntity;

    public ComponentsOldSchematicLetterEntity() {
    }

    public ComponentsOldSchematicLetterEntity(ComponentsOldSchematicLetterEntityPK componentsOldSchematicLetterEntityPK) {
        this.componentsOldSchematicLetterEntityPK = componentsOldSchematicLetterEntityPK;
    }

    public ComponentsOldSchematicLetterEntity(ComponentsOldSchematicLetterEntityPK componentsOldSchematicLetterEntityPK, String schematicLetter, int userId) {
        this.componentsOldSchematicLetterEntityPK = componentsOldSchematicLetterEntityPK;
        this.schematicLetter = schematicLetter;
        this.userid = userId;
    }

    public ComponentsOldSchematicLetterEntity(Date lastDate, int idComponents) {
        this.componentsOldSchematicLetterEntityPK = new ComponentsOldSchematicLetterEntityPK(lastDate, idComponents);
    }

    public ComponentsOldSchematicLetterEntityPK getComponentsOldSchematicLetterEntityPK() {
        return componentsOldSchematicLetterEntityPK;
    }

    public void setComponentsOldSchematicLetterEntityPK(ComponentsOldSchematicLetterEntityPK componentsOldSchematicLetterEntityPK) {
        this.componentsOldSchematicLetterEntityPK = componentsOldSchematicLetterEntityPK;
    }

    public String getSchematicLetter() {
        return schematicLetter;
    }

    public void setSchematicLetter(String schematicLetter) {
        this.schematicLetter = schematicLetter;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userId) {
        this.userid = userId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (componentsOldSchematicLetterEntityPK != null ? componentsOldSchematicLetterEntityPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ComponentsOldSchematicLetterEntity)) {
            return false;
        }
        ComponentsOldSchematicLetterEntity other = (ComponentsOldSchematicLetterEntity) object;
        if ((this.componentsOldSchematicLetterEntityPK == null && other.componentsOldSchematicLetterEntityPK != null) || (this.componentsOldSchematicLetterEntityPK != null && !this.componentsOldSchematicLetterEntityPK.equals(other.componentsOldSchematicLetterEntityPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "irt.web.entities.ComponentsOldSchematicLetterEntity[ componentsOldSchematicLetterEntityPK=" + componentsOldSchematicLetterEntityPK + " ]";
    }
    
}
