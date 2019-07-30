/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package irt.entities;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

/**
 *
 * @author Oleksandr
 */
@Entity
@Table(name = "second_and_third_digit", catalog = "irt", schema = "")
@XmlRootElement
public class SecondAndThirdDigitEntity implements Serializable{
    private static final long serialVersionUID = 1L;

    @EmbeddedId
    protected SecondAndThirdDigitPK key;
	public SecondAndThirdDigitPK getKey() {  return key; }
    public void setKey(SecondAndThirdDigitPK secondAndThirdDigitEntityPK) { this.key = secondAndThirdDigitEntityPK; }

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "description")
    private String description;
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    @Column(name = "class_id", insertable=false, updatable=false)
    private Integer classId;
    public Integer getClassId() { return classId; }
	public void setClassId(Integer classId) { this.classId = classId; }

    @JoinColumn(name = "class_id", referencedColumnName = "class_id", insertable=false, updatable=false)
    @OneToOne(optional = true, fetch=FetchType.LAZY)
    @NotFound(action = NotFoundAction.IGNORE)
    private ClassIdHasArrayEntity hasArrayEntity;
	public ClassIdHasArrayEntity getHasArrayEntity() { return hasArrayEntity; }
	public void setHasArrayEntity(ClassIdHasArrayEntity hasArrayEntity) { this.hasArrayEntity = hasArrayEntity; }

    public SecondAndThirdDigitEntity() { }
    public SecondAndThirdDigitEntity(String id, Integer idFirstDigits) {
		key = new SecondAndThirdDigitPK(id, idFirstDigits);
	}
	@Override
    public int hashCode() {
        return 31 + (key != null ? key.hashCode() : 0);
    }

    @Override
    public boolean equals(Object object) {
        return this==object || object instanceof SecondAndThirdDigitEntity && (key==((SecondAndThirdDigitEntity)object).key || key!=null && key.equals(((SecondAndThirdDigitEntity)object).key));
    }

    @Override
	public String toString() {
		return "\n\tSecondAndThirdDigitEntity [key="
				+ key + ", description=" + description + "]";
	}
}
