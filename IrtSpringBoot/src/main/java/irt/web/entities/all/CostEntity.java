/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package irt.web.entities.all;

import java.io.Serializable;
import java.math.BigDecimal;

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
@Table(name = "cost", catalog = "irt", schema = "")
@XmlRootElement
public class CostEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @EmbeddedId
    protected CostEntityPK costEntityPK;

    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "cost")
    private BigDecimal cost;

    public CostEntity() {
    }

    public CostEntity(CostEntityPK costEntityPK) {
        this.costEntityPK = costEntityPK;
    }

    public CostEntity(CostEntityPK costEntityPK, BigDecimal cost) {
        this.costEntityPK = costEntityPK;
        this.cost = cost;
    }

    public CostEntity(int idComponents, int idComponentsAlternative, int idCompanies, int for1) {
        this.costEntityPK = new CostEntityPK(idComponents, idComponentsAlternative, idCompanies, for1);
    }

    public CostEntityPK getCostEntityPK() {
        return costEntityPK;
    }

    public void setCostEntityPK(CostEntityPK costEntityPK) {
        this.costEntityPK = costEntityPK;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (costEntityPK != null ? costEntityPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CostEntity)) {
            return false;
        }
        CostEntity other = (CostEntity) object;
        if ((this.costEntityPK == null && other.costEntityPK != null) || (this.costEntityPK != null && !this.costEntityPK.equals(other.costEntityPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "irt.web.entities.CostEntity[ costEntityPK=" + costEntityPK + " ]";
    }
    
}
