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
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Oleksandr
 */
@Entity
@Table(name = "kit_details", catalog = "irt", schema = "")
@XmlRootElement
public class KitDetailsEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected KitDetailsEntityPK kitDetailsEntityPK;
    @Basic(optional = false)
    @NotNull
    @Column(name = "id_users")
    private int idUsers;
    @Basic(optional = false)
    @NotNull
    @Column(name = "qty")
    private int qty;

    public KitDetailsEntity() {
    }

    public KitDetailsEntity(KitDetailsEntityPK kitDetailsEntityPK) {
        this.kitDetailsEntityPK = kitDetailsEntityPK;
    }

    public KitDetailsEntity(KitDetailsEntityPK kitDetailsEntityPK, int idUsers, int qty) {
        this.kitDetailsEntityPK = kitDetailsEntityPK;
        this.idUsers = idUsers;
        this.qty = qty;
    }

    public KitDetailsEntity(int idKit, int idComponents) {
        this.kitDetailsEntityPK = new KitDetailsEntityPK(idKit, idComponents);
    }

    public KitDetailsEntityPK getKitDetailsEntityPK() {
        return kitDetailsEntityPK;
    }

    public void setKitDetailsEntityPK(KitDetailsEntityPK kitDetailsEntityPK) {
        this.kitDetailsEntityPK = kitDetailsEntityPK;
    }

    public int getIdUsers() {
        return idUsers;
    }

    public void setIdUsers(int idUsers) {
        this.idUsers = idUsers;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (kitDetailsEntityPK != null ? kitDetailsEntityPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof KitDetailsEntity)) {
            return false;
        }
        KitDetailsEntity other = (KitDetailsEntity) object;
        if ((this.kitDetailsEntityPK == null && other.kitDetailsEntityPK != null) || (this.kitDetailsEntityPK != null && !this.kitDetailsEntityPK.equals(other.kitDetailsEntityPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "irt.web.entities.KitDetailsEntity[ kitDetailsEntityPK=" + kitDetailsEntityPK + " ]";
    }
    
}
