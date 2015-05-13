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
@Table(name = "usets_history", catalog = "irt", schema = "")
@XmlRootElement
public class UsetsHistoryEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;
    @Basic(optional = false)
    @NotNull
    @Column(name = "by")
    private int by;
    @Basic(optional = false)
    @NotNull
    @Column(name = "id_users")
    private int idUsers;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 245)
    @Column(name = "detil")
    private String detil;

    public UsetsHistoryEntity() {
    }

    public UsetsHistoryEntity(Date date) {
        this.date = date;
    }

    public UsetsHistoryEntity(Date date, int by, int idUsers, String detil) {
        this.date = date;
        this.by = by;
        this.idUsers = idUsers;
        this.detil = detil;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getBy() {
        return by;
    }

    public void setBy(int by) {
        this.by = by;
    }

    public int getIdUsers() {
        return idUsers;
    }

    public void setIdUsers(int idUsers) {
        this.idUsers = idUsers;
    }

    public String getDetil() {
        return detil;
    }

    public void setDetil(String detil) {
        this.detil = detil;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (date != null ? date.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof UsetsHistoryEntity)) {
            return false;
        }
        UsetsHistoryEntity other = (UsetsHistoryEntity) object;
        if ((this.date == null && other.date != null) || (this.date != null && !this.date.equals(other.date))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "irt.web.entities.UsetsHistoryEntity[ date=" + date + " ]";
    }
    
}
