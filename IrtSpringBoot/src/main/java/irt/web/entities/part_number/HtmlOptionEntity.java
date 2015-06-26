/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package irt.web.entities.part_number;

import irt.web.entities.all.ClassIdHasArrayEntity;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Oleksandr
 */
@Entity
@Table(name = "html_options")
@XmlRootElement
public class HtmlOptionEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @EmbeddedId
    protected HtmOptionEntityPK htmOptionsPK;

	@Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "array_name")
    private String arrayName;

	private Integer size;

	private Integer position;

	@JoinColumn(name = "class_id", referencedColumnName = "class_id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private ClassIdHasArrayEntity hasArrayEntity;

    public HtmlOptionEntity() {
    }

    public HtmlOptionEntity(HtmOptionEntityPK htmOptionsPK) {
        this.htmOptionsPK = htmOptionsPK;
    }

    public HtmlOptionEntity(int classId, short arraySequence) {
        this.htmOptionsPK = new HtmOptionEntityPK(classId, arraySequence);
    }

    public HtmOptionEntityPK getHtmOptionsPK() {
        return htmOptionsPK;
    }

    public String getArrayName() {
        return arrayName;
    }

    public void setArrayName(String arrayName) {
        this.arrayName = arrayName;
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

	public void setHtmOptionsPK(HtmOptionEntityPK htmOptionsPK) {
        this.htmOptionsPK = htmOptionsPK;
    }

    public ClassIdHasArrayEntity getHasArrayEntity() {
        return hasArrayEntity;
    }

    public void setHasArrayEntity(ClassIdHasArrayEntity classIdHasArrayEntity) {
        this.hasArrayEntity = classIdHasArrayEntity;
    }

    @Override
    public int hashCode() {
        return htmOptionsPK != null ? htmOptionsPK.hashCode() : 0;
    }

    @Override
    public boolean equals(Object object) {
        return object instanceof HtmlOptionEntity ? object.hashCode()==hashCode() : false;
    }

    @Override
	public String toString() {
		return "\n\tHtmlOptionEntity [htmOptionsPK=" + htmOptionsPK
				+ ", arrayName=" + arrayName + ", size=" + size + ", position="
				+ position + ", hasArrayEntity=" + hasArrayEntity + "]";
	}
    
}
