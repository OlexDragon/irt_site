/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package irt.web.entities.part_number;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

/**
 *
 * @author Oleksandr
 */
@Embeddable
public class HtmOptionEntityPK implements Serializable {
	private static final long serialVersionUID = 1L;

	@Basic(optional = false)
    @NotNull
    @Column(name = "class_id")
    private Integer classId;

	@Basic(optional = false)
    @NotNull
    @Column(name = "array_sequence")
    private Short arraySequence;

    public HtmOptionEntityPK() {
    }

    public HtmOptionEntityPK(int classId, short arraySequence) {
        this.classId = classId;
        this.arraySequence = arraySequence;
    }

    public int getClassId() {
        return classId;
    }

    public void setClassId(int classId) {
        this.classId = classId;
    }

    public short getArraySequence() {
        return arraySequence;
    }

    public void setArraySequence(short arraySequence) {
        this.arraySequence = arraySequence;
    }

    @Override
    public int hashCode() {
        return arraySequence*31 + classId;
    }

    @Override
    public boolean equals(Object object) {
        return object instanceof HtmOptionEntityPK ? object.hashCode() == hashCode() : false;
    }

    @Override
	public String toString() {
		return "\n\t\tHtmOptionEntityPK [classId=" + classId + ", arraySequence="
				+ arraySequence + "]\n\t";
	}
    
}
