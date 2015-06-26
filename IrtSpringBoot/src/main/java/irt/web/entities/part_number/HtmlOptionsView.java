/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package irt.web.entities.part_number;

import irt.web.workers.beans.interfaces.ValueText;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Oleksandr
 */
@Entity
@Table(name = "html_options_view")
@XmlRootElement
public class HtmlOptionsView implements Serializable, ValueText {
    private static final long serialVersionUID = 1L;

    @EmbeddedId
	private HtmlOptionsViewPK key;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "description")
    private String description;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "array_name")
    private String arrayName;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

	public HtmlOptionsViewPK getKey() {
		return key;
	}

	public void setKey(HtmlOptionsViewPK key) {
		this.key = key;
	}

	@Override
	public String getValue() {
		return key.getId();
	}

	@Override
	public String getText() {
		return description;
	}

	@Override
	public String toString() {
		return "HtmlOptionsView [key=" + key + ", description="
				+ description + "]";
	}

	public String getArrayName() {
		return arrayName;
	}

	public void setArrayName(String arrayName) {
		this.arrayName = arrayName;
	}    
}
