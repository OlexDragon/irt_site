/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package irt.web.entities.all;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Oleksandr
 */
@Entity
@Table(name = "bomunit", catalog = "irt", schema = "")
@XmlRootElement
public class BomunitEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "idbomunit")
    private Integer idbomunit;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "part_number")
    private String partNumber;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 500)
    @Column(name = "part_ref")
    private String partRef;

    public BomunitEntity() {
    }

    public BomunitEntity(Integer idbomunit) {
        this.idbomunit = idbomunit;
    }

    public BomunitEntity(Integer idbomunit, String partNumber, String partRef) {
        this.idbomunit = idbomunit;
        this.partNumber = partNumber;
        this.partRef = partRef;
    }

    public Integer getIdbomunit() {
        return idbomunit;
    }

    public void setIdbomunit(Integer idbomunit) {
        this.idbomunit = idbomunit;
    }

    public String getPartNumber() {
        return partNumber;
    }

    public void setPartNumber(String partNumber) {
        this.partNumber = partNumber;
    }

    public String getPartRef() {
        return partRef;
    }

    public void setPartRef(String partRef) {
        this.partRef = partRef;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idbomunit != null ? idbomunit.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof BomunitEntity)) {
            return false;
        }
        BomunitEntity other = (BomunitEntity) object;
        if ((this.idbomunit == null && other.idbomunit != null) || (this.idbomunit != null && !this.idbomunit.equals(other.idbomunit))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "irt.web.entities.BomunitEntity[ idbomunit=" + idbomunit + " ]";
    }
    
}
