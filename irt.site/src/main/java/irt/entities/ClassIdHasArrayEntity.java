/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package irt.entities;

import java.io.Serializable;
import java.util.Optional;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Oleksandr
 */
@Entity
@Table(name = "class_id_has_array_names")
@XmlRootElement
public class ClassIdHasArrayEntity implements Serializable {
    private static final long serialVersionUID = 1L;

	
	@Id @Column(name = "class_id") @Basic(optional = false) @NotNull
    private Integer classId;  public Integer getClassId() { return classId; }  public void setClassId(Integer classId) { this.classId = classId; }

	@ManyToOne @JoinColumn(name = "id_array_names")
    private ArrayNameEntity arrayNameEntity;  public Optional<ArrayNameEntity> getArrayNameEntity() { return Optional.ofNullable(arrayNameEntity); } public void setArrayNameEntity(ArrayNameEntity arrayNameEntity) { this.arrayNameEntity = arrayNameEntity; }

	@Override
    public int hashCode() {
        return 31 + (classId != null ? classId.hashCode() : 0);
    }

    @Override
    public boolean equals(Object obj) {
        return this == obj || (obj instanceof ClassIdHasArrayEntity && obj.hashCode()==hashCode());
    }
}
