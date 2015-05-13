/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package irt.web.entities.all;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Oleksandr
 */
@Entity
@Table(name = "cost_sets", catalog = "irt", schema = "")
@XmlRootElement
public class CostSetEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected CostSetEntityPK costSetEntityPK;
    @Column(name = "mfr_part_number")
    private Integer mfrPartNumber;
    @Column(name = "vendor")
    private Integer vendor;
    @Column(name = "moq")
    private Integer moq;

    public CostSetEntity() {
    }

    public CostSetEntity(CostSetEntityPK costSetEntityPK) {
        this.costSetEntityPK = costSetEntityPK;
    }

    public CostSetEntity(int setId, int idTopComponent, int idComponents) {
        this.costSetEntityPK = new CostSetEntityPK(setId, idTopComponent, idComponents);
    }

    public CostSetEntityPK getCostSetEntityPK() {
        return costSetEntityPK;
    }

    public void setCostSetEntityPK(CostSetEntityPK costSetEntityPK) {
        this.costSetEntityPK = costSetEntityPK;
    }

    public Integer getMfrPartNumber() {
        return mfrPartNumber;
    }

    public void setMfrPartNumber(Integer mfrPartNumber) {
        this.mfrPartNumber = mfrPartNumber;
    }

    public Integer getVendor() {
        return vendor;
    }

    public void setVendor(Integer vendor) {
        this.vendor = vendor;
    }

    public Integer getMoq() {
        return moq;
    }

    public void setMoq(Integer moq) {
        this.moq = moq;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (costSetEntityPK != null ? costSetEntityPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CostSetEntity)) {
            return false;
        }
        CostSetEntity other = (CostSetEntity) object;
        if ((this.costSetEntityPK == null && other.costSetEntityPK != null) || (this.costSetEntityPK != null && !this.costSetEntityPK.equals(other.costSetEntityPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "irt.web.entities.CostSetEntity[ costSetEntityPK=" + costSetEntityPK + " ]";
    }
    
}
