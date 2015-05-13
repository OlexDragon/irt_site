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
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Oleksandr
 */
@Entity
@Table(name = "backup", catalog = "irt", schema = "")
@XmlRootElement
public class BackupEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @EmbeddedId
    protected BackupEntityPK backupEntityPK;
    @Basic(optional = false)
    @NotNull
    @Column(name = "qty")
    private int qty;

    @Basic(optional = false)
    @NotNull
    @Column(name = "user")
    private int user;

    @Basic(optional = false)
    @NotNull
    @Column(name = "date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;

    public BackupEntity() {
    }

    public BackupEntity(BackupEntityPK backupEntityPK) {
        this.backupEntityPK = backupEntityPK;
    }

    public BackupEntity(BackupEntityPK backupEntityPK, int qty, int user, Date date) {
        this.backupEntityPK = backupEntityPK;
        this.qty = qty;
        this.user = user;
        this.date = date;
    }

    public BackupEntity(int id, int idComponents) {
        this.backupEntityPK = new BackupEntityPK(id, idComponents);
    }

    public BackupEntityPK getBackupEntityPK() {
        return backupEntityPK;
    }

    public void setBackupEntityPK(BackupEntityPK backupEntityPK) {
        this.backupEntityPK = backupEntityPK;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public int getUser() {
        return user;
    }

    public void setUser(int user) {
        this.user = user;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (backupEntityPK != null ? backupEntityPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof BackupEntity)) {
            return false;
        }
        BackupEntity other = (BackupEntity) object;
        if ((this.backupEntityPK == null && other.backupEntityPK != null) || (this.backupEntityPK != null && !this.backupEntityPK.equals(other.backupEntityPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "irt.web.entities.BackupEntity[ backupEntityPK=" + backupEntityPK + " ]";
    }
    
}
