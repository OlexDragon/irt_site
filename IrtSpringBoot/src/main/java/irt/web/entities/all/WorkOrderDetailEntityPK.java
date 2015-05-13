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
public class WorkOrderDetailEntityPK implements Serializable {
    private static final long serialVersionUID = 1L;

    @Basic(optional = false)
    @NotNull
    @Column(name = "work_ordesr_id")
    private int workOrdesrId;

    @Basic(optional = false)
    @NotNull
    @Column(name = "components_id")
    private int componentsId;

    public WorkOrderDetailEntityPK() {
    }

    public WorkOrderDetailEntityPK(int workOrdesrId, int componentsId) {
        this.workOrdesrId = workOrdesrId;
        this.componentsId = componentsId;
    }

    public int getWorkOrdesrId() {
        return workOrdesrId;
    }

    public void setWorkOrdesrId(int workOrdesrId) {
        this.workOrdesrId = workOrdesrId;
    }

    public int getComponentsId() {
        return componentsId;
    }

    public void setComponentsId(int componentsId) {
        this.componentsId = componentsId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) workOrdesrId;
        hash += (int) componentsId;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof WorkOrderDetailEntityPK)) {
            return false;
        }
        WorkOrderDetailEntityPK other = (WorkOrderDetailEntityPK) object;
        if (this.workOrdesrId != other.workOrdesrId) {
            return false;
        }
        if (this.componentsId != other.componentsId) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "irt.web.entities.WorkOrderDetailEntityPK[ workOrdesrId=" + workOrdesrId + ", componentsId=" + componentsId + " ]";
    }
    
}
