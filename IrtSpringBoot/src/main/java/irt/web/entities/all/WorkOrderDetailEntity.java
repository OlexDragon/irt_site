/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package irt.web.entities.all;

import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Oleksandr
 */
@Entity
@Table(name = "work_order_detail", catalog = "irt", schema = "")
@XmlRootElement
public class WorkOrderDetailEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @EmbeddedId
    protected WorkOrderDetailEntityPK workOrderDetailEntityPK;

    @JoinColumn(name = "work_ordesr_id", referencedColumnName = "id_work_order", insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private WorkOrdersEntity workOrdersEntity;

    public WorkOrderDetailEntity() {
    }

    public WorkOrderDetailEntity(WorkOrderDetailEntityPK workOrderDetailEntityPK) {
        this.workOrderDetailEntityPK = workOrderDetailEntityPK;
    }

    public WorkOrderDetailEntity(int workOrdesrId, int componentsId) {
        this.workOrderDetailEntityPK = new WorkOrderDetailEntityPK(workOrdesrId, componentsId);
    }

    public WorkOrderDetailEntityPK getWorkOrderDetailEntityPK() {
        return workOrderDetailEntityPK;
    }

    public void setWorkOrderDetailEntityPK(WorkOrderDetailEntityPK workOrderDetailEntityPK) {
        this.workOrderDetailEntityPK = workOrderDetailEntityPK;
    }

    public WorkOrdersEntity getWorkOrdersEntity() {
        return workOrdersEntity;
    }

    public void setWorkOrdersEntity(WorkOrdersEntity workOrdersEntity) {
        this.workOrdersEntity = workOrdersEntity;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (workOrderDetailEntityPK != null ? workOrderDetailEntityPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof WorkOrderDetailEntity)) {
            return false;
        }
        WorkOrderDetailEntity other = (WorkOrderDetailEntity) object;
        if ((this.workOrderDetailEntityPK == null && other.workOrderDetailEntityPK != null) || (this.workOrderDetailEntityPK != null && !this.workOrderDetailEntityPK.equals(other.workOrderDetailEntityPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "irt.web.entities.WorkOrderDetailEntity[ workOrderDetailEntityPK=" + workOrderDetailEntityPK + " ]";
    }
    
}
