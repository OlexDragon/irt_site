/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package irt.web.entities.all;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Oleksandr
 */
@Entity
@Table(name = "work_orders", catalog = "irt", schema = "")
@XmlRootElement
public class WorkOrdersEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_work_order")
    private Integer idWorkOrder;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "work_order_name")
    private String workOrderName;

    @Basic(optional = false)
    @NotNull
    @Column(name = "date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "workOrdersEntity", fetch = FetchType.EAGER)
    private List<WorkOrderDetailEntity> workOrderDetailEntityList;

    public WorkOrdersEntity() {
    }

    public WorkOrdersEntity(Integer idWorkOrder) {
        this.idWorkOrder = idWorkOrder;
    }

    public WorkOrdersEntity(Integer idWorkOrder, String workOrderName, Date date) {
        this.idWorkOrder = idWorkOrder;
        this.workOrderName = workOrderName;
        this.date = date;
    }

    public Integer getIdWorkOrder() {
        return idWorkOrder;
    }

    public void setIdWorkOrder(Integer idWorkOrder) {
        this.idWorkOrder = idWorkOrder;
    }

    public String getWorkOrderName() {
        return workOrderName;
    }

    public void setWorkOrderName(String workOrderName) {
        this.workOrderName = workOrderName;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @XmlTransient
    public List<WorkOrderDetailEntity> getWorkOrderDetailEntityList() {
        return workOrderDetailEntityList;
    }

    public void setWorkOrderDetailEntityList(List<WorkOrderDetailEntity> workOrderDetailEntityList) {
        this.workOrderDetailEntityList = workOrderDetailEntityList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idWorkOrder != null ? idWorkOrder.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof WorkOrdersEntity)) {
            return false;
        }
        WorkOrdersEntity other = (WorkOrdersEntity) object;
        if ((this.idWorkOrder == null && other.idWorkOrder != null) || (this.idWorkOrder != null && !this.idWorkOrder.equals(other.idWorkOrder))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "irt.web.entities.WorkOrdersEntity[ idWorkOrder=" + idWorkOrder + " ]";
    }
    
}
