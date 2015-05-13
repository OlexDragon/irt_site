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
@Table(name = "errors", catalog = "irt", schema = "")
@XmlRootElement
public class ErrorEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "errorCod")
    private String errorCod;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 3072)
    @Column(name = "error")
    private String error;

    public ErrorEntity() {
    }

    public ErrorEntity(Date date) {
        this.date = date;
    }

    public ErrorEntity(Date date, String errorCod, String error) {
        this.date = date;
        this.errorCod = errorCod;
        this.error = error;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getErrorCod() {
        return errorCod;
    }

    public void setErrorCod(String errorCod) {
        this.errorCod = errorCod;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
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
        if (!(object instanceof ErrorEntity)) {
            return false;
        }
        ErrorEntity other = (ErrorEntity) object;
        if ((this.date == null && other.date != null) || (this.date != null && !this.date.equals(other.date))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "irt.web.entities.ErrorEntity[ date=" + date + " ]";
    }
    
}
