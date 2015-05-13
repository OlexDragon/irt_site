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
public class PurchaseComponentsEntityPK implements Serializable {
    private static final long serialVersionUID = 1L;

    @Basic(optional = false)
    @NotNull
    @Column(name = "id_purchase")
    private int idPurchase;

    @Basic(optional = false)
    @NotNull
    @Column(name = "id_components")
    private int idComponents;

    public PurchaseComponentsEntityPK() {
    }

    public PurchaseComponentsEntityPK(int idPurchase, int idComponents) {
        this.idPurchase = idPurchase;
        this.idComponents = idComponents;
    }

    public int getIdPurchase() {
        return idPurchase;
    }

    public void setIdPurchase(int idPurchase) {
        this.idPurchase = idPurchase;
    }

    public int getIdComponents() {
        return idComponents;
    }

    public void setIdComponents(int idComponents) {
        this.idComponents = idComponents;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) idPurchase;
        hash += (int) idComponents;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PurchaseComponentsEntityPK)) {
            return false;
        }
        PurchaseComponentsEntityPK other = (PurchaseComponentsEntityPK) object;
        if (this.idPurchase != other.idPurchase) {
            return false;
        }
        if (this.idComponents != other.idComponents) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "irt.web.entities.PurchaseComponentsEntityPK[ idPurchase=" + idPurchase + ", idComponents=" + idComponents + " ]";
    }
    
}
