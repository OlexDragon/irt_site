package irt.web.entities.part_number;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Oleksandr
 */
@Entity
@Table(name = "sequential_number", catalog = "irt", schema = "")
@XmlRootElement
public class SequentialNumberEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "class_id", nullable = false)
    private Long classId;

    @Basic(optional = false)
    @NotNull
    @Column(name = "start", nullable = false)
    private int start;

    @Column(name = "size")
    private Integer size;

    @JoinColumn(name = "class_id", referencedColumnName = "class_id", nullable = false, insertable = false, updatable = false)
    @OneToOne(optional = false, fetch = FetchType.LAZY)
    private ClassIdHasArrayEntity hasArrayEntity;

    public SequentialNumberEntity() {
    }

    public SequentialNumberEntity(Long classId) {
        this.classId = classId;
    }

    public SequentialNumberEntity(Long classId, int start) {
        this.classId = classId;
        this.start = start;
    }

    public Long getClassId() {
        return classId;
    }

    public void setClassId(Long classId) {
        this.classId = classId;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public ClassIdHasArrayEntity getfHasArrayEntity() {
        return hasArrayEntity;
    }

    public void setHasArrayEntity(ClassIdHasArrayEntity classIdHasArrayEntity) {
        this.hasArrayEntity = classIdHasArrayEntity;
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
        if (!(object instanceof SequentialNumberEntity)) {
            return false;
        }
        SequentialNumberEntity other = (SequentialNumberEntity) object;
        if ((this.classId == null && other.classId != null) || (this.classId != null && !this.classId.equals(other.classId))) {
            return false;
        }
        return true;
    }

    @Override
	public String toString() {
		return "\n\tSequentialNumberEntity [classId=" + classId + ", start="
				+ start + ", size=" + size + "]";
	}
    
}
