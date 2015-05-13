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
@Table(name = "purchase_components", catalog = "irt", schema = "")
@XmlRootElement
public class PurchaseComponentsEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected PurchaseComponentsEntityPK purchaseComponentsEntityPK;
    @Basic(optional = false)
    @NotNull
    @Column(name = "id_components_alternative")
    private int idComponentsAlternative;
    @Basic(optional = false)
    @NotNull
    @Column(name = "price")
    private long price;
    @Basic(optional = false)
    @NotNull
    @Column(name = "qty")
    private int qty;

    public PurchaseComponentsEntity() {
    }

    public PurchaseComponentsEntity(PurchaseComponentsEntityPK purchaseComponentsEntityPK) {
        this.purchaseComponentsEntityPK = purchaseComponentsEntityPK;
    }

    public PurchaseComponentsEntity(PurchaseComponentsEntityPK purchaseComponentsEntityPK, int idComponentsAlternative, long price, int qty) {
        this.purchaseComponentsEntityPK = purchaseComponentsEntityPK;
        this.idComponentsAlternative = idComponentsAlternative;
        this.price = price;
        this.qty = qty;
    }

    public PurchaseComponentsEntity(int idPurchase, int idComponents) {
        this.purchaseComponentsEntityPK = new PurchaseComponentsEntityPK(idPurchase, idComponents);
    }

    public PurchaseComponentsEntityPK getPurchaseComponentsEntityPK() {
        return purchaseComponentsEntityPK;
    }

    public void setPurchaseComponentsEntityPK(PurchaseComponentsEntityPK purchaseComponentsEntityPK) {
        this.purchaseComponentsEntityPK = purchaseComponentsEntityPK;
    }

    public int getIdComponentsAlternative() {
        return idComponentsAlternative;
    }

    public void setIdComponentsAlternative(int idComponentsAlternative) {
        this.idComponentsAlternative = idComponentsAlternative;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
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
        hash += (purchaseComponentsEntityPK != null ? purchaseComponentsEntityPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PurchaseComponentsEntity)) {
            return false;
        }
        PurchaseComponentsEntity other = (PurchaseComponentsEntity) object;
        if ((this.purchaseComponentsEntityPK == null && other.purchaseComponentsEntityPK != null) || (this.purchaseComponentsEntityPK != null && !this.purchaseComponentsEntityPK.equals(other.purchaseComponentsEntityPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "irt.web.entities.PurchaseComponentsEntity[ purchaseComponentsEntityPK=" + purchaseComponentsEntityPK + " ]";
    }
    
}
