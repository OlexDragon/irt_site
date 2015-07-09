/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package irt.web.entities.part_number;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Oleksandr
 */
@Entity
@Table(name = "part_number_details_view", catalog = "irt", schema = "")
@XmlRootElement
public class PartNumberDetailsView implements Serializable {
    private static final long serialVersionUID = 1L;

    @EmbeddedId
    private PartNumberDetailsPK key;

     @Column(name = "first_digits_id")
    private int firstDigitsId;

    @Column(name = "class_id")
    private Long classId;

    @Size(min = 1, max = 20)
    @Column(name = "array_name", length = 20)
    private String arrayName;

    @Size(max = 45)
    @Column(name = "detail_description", length = 45)
    private String detailDescription;

    private Integer size;

    private Integer position;

    public PartNumberDetailsView() {
    }

    public PartNumberDetailsPK getKey() {
		return key;
	}

	public void setKey(PartNumberDetailsPK key) {
		this.key = key;
	}

	public int getFirstDigitsId() {
        return firstDigitsId;
    }

    public void setFirstDigitsId(int firstDigitsId) {
        this.firstDigitsId = firstDigitsId;
    }

    public Long getClassId() {
        return classId;
    }

    public void setClassId(Long classId) {
        this.classId = classId;
    }

    public String getArrayName() {
        return arrayName;
    }

    public void setArrayName(String arrayName) {
        this.arrayName = arrayName;
    }

    public String getDetailDescription() {
        return detailDescription;
    }

    public void setDetailDescription(String detailDescription) {
        this.detailDescription = detailDescription;
    }

	public Integer getSize() {
		return size;
	}

	public void setSize(Integer size) {
		this.size = size;
	}

	public Integer getPosition() {
		return position;
	}

	public void setPosition(Integer position) {
		this.position = position;
	}

	@Override
	public String toString() {
		return "\n\tPartNumberDetailsView [key=" + key + ", firstDigitsId="
				+ firstDigitsId + ", classId=" + classId + ", arrayName="
				+ arrayName + ", detailDescription=" + detailDescription
				+ ", size=" + size + ", position=" + position + "]";
	}
    
}
