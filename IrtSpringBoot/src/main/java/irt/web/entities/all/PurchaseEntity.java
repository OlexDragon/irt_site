/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package irt.web.entities.all;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Oleksandr
 */
@Entity
@Table(name = "purchase", catalog = "irt", schema = "")
@XmlRootElement
public class PurchaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    @Basic(optional = false)
    @Column(name = "id")
    private int id;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 12)
    @Column(name = "po_number")
    private String poNumber;
    @Basic(optional = false)
    @NotNull
    @Column(name = "id_users")
    private int idUsers;
    @Basic(optional = false)
    @NotNull
    @Column(name = "id_companies")
    private int idCompanies;
    @Column(name = "tax")
    private Integer tax;
    @Column(name = "extra")
    private Integer extra;
    @Size(max = 120)
    @Column(name = "comments")
    private String comments;
    @Basic(optional = false)
    @NotNull
    @Column(name = "date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;
    @Basic(optional = false)
    @NotNull
    @Column(name = "status")
    private short status;
    @Column(name = "invoice")
    private Integer invoice;

    public PurchaseEntity() {
    }

    public PurchaseEntity(String poNumber) {
        this.poNumber = poNumber;
    }

    public PurchaseEntity(String poNumber, int id, int idUsers, int idCompanies, Date date, short status) {
        this.poNumber = poNumber;
        this.id = id;
        this.idUsers = idUsers;
        this.idCompanies = idCompanies;
        this.date = date;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPoNumber() {
        return poNumber;
    }

    public void setPoNumber(String poNumber) {
        this.poNumber = poNumber;
    }

    public int getIdUsers() {
        return idUsers;
    }

    public void setIdUsers(int idUsers) {
        this.idUsers = idUsers;
    }

    public int getIdCompanies() {
        return idCompanies;
    }

    public void setIdCompanies(int idCompanies) {
        this.idCompanies = idCompanies;
    }

    public Integer getTax() {
        return tax;
    }

    public void setTax(Integer tax) {
        this.tax = tax;
    }

    public Integer getExtra() {
        return extra;
    }

    public void setExtra(Integer extra) {
        this.extra = extra;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public short getStatus() {
        return status;
    }

    public void setStatus(short status) {
        this.status = status;
    }

    public Integer getInvoice() {
        return invoice;
    }

    public void setInvoice(Integer invoice) {
        this.invoice = invoice;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (poNumber != null ? poNumber.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PurchaseEntity)) {
            return false;
        }
        PurchaseEntity other = (PurchaseEntity) object;
        if ((this.poNumber == null && other.poNumber != null) || (this.poNumber != null && !this.poNumber.equals(other.poNumber))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "irt.web.entities.PurchaseEntity[ poNumber=" + poNumber + " ]";
    }
    
}
