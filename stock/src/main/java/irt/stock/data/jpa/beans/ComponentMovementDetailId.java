package irt.stock.data.jpa.beans;

import java.io.Serializable;
import java.util.Optional;

import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Embeddable
public class ComponentMovementDetailId implements Serializable{
	private static final long serialVersionUID = 4855489947388797625L;

	protected ComponentMovementDetailId(){}
	public ComponentMovementDetailId(ComponentMovement componentMovement, Component component) {
		Optional.ofNullable(componentMovement).filter(cm->cm.getId()!=null).orElseThrow(()->new RuntimeException("The object ComponentMovement and id can not be null: " + componentMovement));
		Optional.ofNullable(component).filter(c->c.getId()!=null).orElseThrow(()->new RuntimeException("The object Component and its id can not be null: " + component));

		this.componentMovement = componentMovement;
		this.component = component;
	}

	@ManyToOne
	@JoinColumn(name="idMovement")
	private ComponentMovement componentMovement;

	@JsonIgnore
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="idComponents")
	private Component component;

	public ComponentMovement getComponentMovement() {
		return componentMovement;
	}
	public Component getComponent() {
		return component;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((component == null) ? 0 : component.hashCode());
		result = prime * result + ((componentMovement == null) ? 0 : componentMovement.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ComponentMovementDetailId other = (ComponentMovementDetailId) obj;
		if (component == null) {
			if (other.component != null)
				return false;
		} else if (!component.equals(other.component))
			return false;
		if (componentMovement == null) {
			if (other.componentMovement != null)
				return false;
		} else if (!componentMovement.equals(other.componentMovement))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ComponentMovementDetailId [componentMovement=" + componentMovement + ", component=" + component + "]";
	}
}
