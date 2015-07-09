/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package irt.web.entities.part_number;

import irt.web.entities.all.ArrayEntity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Oleksandr
 */
@Entity
@Table(name = "class_id_has_arrays")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ClassIdHasArrayEntity.findAll", query = "SELECT c FROM ClassIdHasArrayEntity c")})
public class ClassIdHasArrayEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "class_id")
    private Long classId;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "name")
    private String name;

    @OneToOne(cascade = CascadeType.PERSIST, mappedBy = "hasArrayEntity", fetch = FetchType.LAZY)
    private SecondAndThirdDigitEntity secondAndThirdDigitEntity;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "hasArrayEntity", fetch = FetchType.LAZY)
    private List<ArrayEntity> arrayEntityList;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "hasArrayEntity", fetch = FetchType.LAZY)
    private List<HtmlOptionEntity> htmOptionsList;

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "hasArrayEntity", fetch = FetchType.LAZY)
    private SequentialNumberEntity sequentialNamberEntity;

    public ClassIdHasArrayEntity() {
    }

    public ClassIdHasArrayEntity(Long classId) {
        this.classId = classId;
    }

    public ClassIdHasArrayEntity(Long classId, String name) {
        this.classId = classId;
        this.name = name;
    }

    public Long getClassId() {
        return classId;
    }

    public void setClassId(Long classId) {
        this.classId = classId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public SecondAndThirdDigitEntity getSecondAndThirdDigitEntity() {
        return secondAndThirdDigitEntity;
    }

    public void setSecondAndThirdDigitEntity(SecondAndThirdDigitEntity secondAndThirdDigitEntity) {
        this.secondAndThirdDigitEntity = secondAndThirdDigitEntity;
    }

    @XmlTransient
    public List<ArrayEntity> getArrayEntityList() {
        return arrayEntityList;
    }

    public void setArrayEntityList(List<ArrayEntity> arrayEntityList) {
        this.arrayEntityList = arrayEntityList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (classId != null ? classId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ClassIdHasArrayEntity)) {
            return false;
        }
        ClassIdHasArrayEntity other = (ClassIdHasArrayEntity) object;
        if ((this.classId == null && other.classId != null) || (this.classId != null && !this.classId.equals(other.classId))) {
            return false;
        }
        return true;
    }


    @XmlTransient
    public List<HtmlOptionEntity> getHtmOptionsList() {
        return htmOptionsList;
    }

    public void setHtmOptionsList(List<HtmlOptionEntity> htmOptionsList) {
        this.htmOptionsList = htmOptionsList;
    }

    public SequentialNumberEntity getSequentialNamberEntity() {
        return sequentialNamberEntity;
    }

    public void setSequentialNamberEntity(SequentialNumberEntity sequentialNamberEntity) {
        this.sequentialNamberEntity = sequentialNamberEntity;
    }

    @Override
	public String toString() {
		return "\n\tClassIdHasArrayEntity [classId=" + classId + ", name=" + name
				+ ", arrayEntityList=" + arrayEntityList + "]";
	}
}
