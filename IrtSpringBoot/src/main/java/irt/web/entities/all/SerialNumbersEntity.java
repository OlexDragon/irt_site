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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
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
@Table(name = "serial_numbers", catalog = "irt", schema = "")
@XmlRootElement
public class SerialNumbersEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "serial_number")
    private String serialNumber;
    @Basic(optional = false)
    @NotNull
    @Column(name = "date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;
    @Lob
    @Size(max = 65535)
    @Column(name = "profile")
    private String profile;
    @Column(name = "profile_change_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date profileChangeDate;
    @Column(name = "id_software")
    private Integer idSoftware;

    public SerialNumbersEntity() {
    }

    public SerialNumbersEntity(Integer id) {
        this.id = id;
    }

    public SerialNumbersEntity(Integer id, String serialNumber, Date date) {
        this.id = id;
        this.serialNumber = serialNumber;
        this.date = date;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public Date getProfileChangeDate() {
        return profileChangeDate;
    }

    public void setProfileChangeDate(Date profileChangeDate) {
        this.profileChangeDate = profileChangeDate;
    }

    public Integer getIdSoftware() {
        return idSoftware;
    }

    public void setIdSoftware(Integer idSoftware) {
        this.idSoftware = idSoftware;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SerialNumbersEntity)) {
            return false;
        }
        SerialNumbersEntity other = (SerialNumbersEntity) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "irt.web.entities.SerialNumbersEntity[ id=" + id + " ]";
    }
    
}
