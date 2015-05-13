/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package irt.web.entities.bom;

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
public class BomEntityPK implements Serializable {
	private static final long serialVersionUID = 1L;

	@Basic(optional = false)
    @NotNull
    @Column(name = "id_top_comp")
    private Long idTopComp;

	@Basic(optional = false)
    @NotNull
    @Column(name = "id_components")
    private Long idComponents;

    public BomEntityPK() {
    }

    public BomEntityPK(Long idTopComp, Long idComponents) {
        this.idTopComp = idTopComp;
        this.idComponents = idComponents;
    }

    public Long getIdTopComp() {
        return idTopComp;
    }

    public void setIdTopComp(Long idTopComp) {
        this.idTopComp = idTopComp;
    }

    public Long getIdComponents() {
        return idComponents;
    }

    public void setIdComponents(Long idComponents) {
        this.idComponents = idComponents;
    }

    @Override
    public int hashCode() {
        int hash = idTopComp!=null ? idTopComp.hashCode() : 0;
        hash += idComponents!=null ? idComponents.hashCode() : 0;
        return hash;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof BomEntityPK ? o.hashCode()==hashCode() : false;
    }

    @Override
    public String toString() {
        return "\n\tBomEntityPK[\n\t\tidTopComp=" + idTopComp + ",\n\t\tidComponents=" + idComponents + " ]";
    }
    
}
