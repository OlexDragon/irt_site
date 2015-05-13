/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package irt.web.entities.bom;

import irt.web.entities.component.ComponentEntity;

import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author Oleksandr
 */
@Entity
@Table(name = "bom", catalog = "irt", schema = "")
@NamedQueries({
    @NamedQuery(name = "BomEntity.findAll", query = "SELECT b FROM BomEntity b")})
public class BomEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @EmbeddedId
    protected BomEntityPK bomEntityPK;

    @JoinColumn(name = "id_top_comp", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private ComponentEntity topComponentEntity;

    @JoinColumn(name = "id_components", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private ComponentEntity componentEntity;

    @JoinColumn(name = "id_bom_ref", referencedColumnName = "id")
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private BomRefEntity bomRef;

    public BomEntity() {
    }

    public BomEntity(BomEntityPK bomEntityPK) {
        this.bomEntityPK = bomEntityPK;
    }

    public BomEntityPK getBomEntityPK() {
        return bomEntityPK;
    }

    public void setBomEntityPK(BomEntityPK bomEntityPK) {
        this.bomEntityPK = bomEntityPK;
    }

    public ComponentEntity getComponentEntity() {
        return componentEntity;
    }

    public void setComponentEntity(ComponentEntity componentEntity) {
        this.componentEntity = componentEntity;
    }

    public ComponentEntity getTopComponentEntity() {
        return topComponentEntity;
    }

    public void setTopComponentEntity(ComponentEntity topComponentEntity) {
        this.topComponentEntity = topComponentEntity;
    }

    public BomRefEntity getBomRef() {
        return bomRef;
    }

    public void setBomRef(BomRefEntity BomRef) {
        this.bomRef = BomRef;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (bomEntityPK != null ? bomEntityPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof BomEntity)) {
            return false;
        }
        BomEntity other = (BomEntity) object;
        if ((this.bomEntityPK == null && other.bomEntityPK != null) || (this.bomEntityPK != null && !this.bomEntityPK.equals(other.bomEntityPK))) {
            return false;
        }
        return true;
    }

	@Override
	public String toString() {
		return "\n\tBomEntity [\n\t\tbomEntityPK=" + bomEntityPK
				+ ",\n\t\ttopComponentEntity=" + topComponentEntity
				+ ",\n\t\tcomponentEntity=" + componentEntity
				+ ",\n\t\tidBomRef=" + bomRef + "]";
	}
}
