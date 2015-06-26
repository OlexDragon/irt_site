package irt.web.entities.part_number;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Embeddable
public class HtmlOptionsViewPK implements Serializable{
	private static final long serialVersionUID = 1L;

	@Basic(optional = false)
	@NotNull
	@Column(name = "class_id")
	private int classId;

	@Basic(optional = false)
	@NotNull
	@Column(name = "array_sequence")
	private short arraySequence;

	@Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "id")
    private String id;

	public HtmlOptionsViewPK() {
	}

	public HtmlOptionsViewPK(int classId, short arraySequence) {
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

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "HtmlOptionsViewPK [classId=" + classId + ", arraySequence="
				+ arraySequence + ", id=" + id + "]";
	}
}