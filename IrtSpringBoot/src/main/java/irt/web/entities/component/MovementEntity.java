/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package irt.web.entities.component;

import irt.web.entities.all.UsersEntity;
import irt.web.entities.company.CompanyEntity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

/**
 * @author Oleksandr
 */
@Entity
@Table(name = "movement", catalog = "irt", schema = "")
@XmlRootElement
public class MovementEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;

    @JoinColumn(name = "who", referencedColumnName = "id", insertable=false, updatable=false)
    @ManyToOne(fetch = FetchType.EAGER)
    private UsersEntity who;

    @Size(max = 100)
    @Column(name = "description")
    private String description;

    @Basic(optional = false)
    @NotNull
    @Column(name = "date_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateTime;

    @Basic(optional = false)
    @NotNull
    @Column(name = "status")
    private short status;

    @JoinColumn(name = "from", referencedColumnName = "place_id")
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private PlaceEntity from;

	@NotFound(action = NotFoundAction.IGNORE)
    @JoinColumn(name = "from_detail", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.EAGER)
    private CompanyEntity fromDetail;

    @JoinColumn(name = "to", referencedColumnName = "place_id")
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private PlaceEntity to;

	@NotFound(action = NotFoundAction.IGNORE)
    @JoinColumn(name = "to_detail", referencedColumnName = "id")
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private CompanyEntity toDetail;

    public MovementEntity() {
    }

    public MovementEntity(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public UsersEntity getWho() {
        return who;
    }

    public void setWho(UsersEntity who) {
        this.who = who;
    }

    public PlaceEntity getFrom() {
        return from;
    }

    public void setFrom(PlaceEntity from) {
        this.from = from;
    }

    public PlaceEntity getTo() {
        return to;
    }

    public void setTo(PlaceEntity to) {
        this.to = to;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }

    public short getStatus() {
        return status;
    }

    public void setStatus(short status) {
        this.status = status;
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
        if (!(object instanceof MovementEntity)) {
            return false;
        }
        MovementEntity other = (MovementEntity) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "irt.web.entities.MovementEntity[ id=" + id + " ]";
    }

    public CompanyEntity getFromDetail() {
        return fromDetail;
    }

    public void setFromDetail(CompanyEntity fromDetail) {
        this.fromDetail = fromDetail;
    }

	public CompanyEntity getToDetail() {
        return toDetail;
    }

    public void setToDetail(CompanyEntity toDetail) {
        this.toDetail = toDetail;
    }
    
}
