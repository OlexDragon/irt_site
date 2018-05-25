package irt.stock.data.jpa.beans;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class BomComponentId implements Serializable{
	private static final long serialVersionUID = 8498374466874205434L;

	protected BomComponentId() { }
	public BomComponentId(Long topComponentId, Long componentId) {
		this.topComponentId = topComponentId;
		this.componentId = componentId;
	}

	@Column(name="id_top_comp") 	private Long topComponentId;
	@Column(name="id_components") 	private Long componentId;

	public Long getTopComponentId() { return topComponentId; }
	public Long getComponentId() { return componentId; }

	public void setComponentId(Long componentId) {
		this.componentId = componentId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = prime + ((componentId == null) ? 0 : componentId.hashCode());
		return prime * result + ((topComponentId == null) ? 0 : topComponentId.hashCode());
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BomComponentId other = (BomComponentId) obj;
		if (componentId == null) {
			if (other.componentId != null)
				return false;
		} else if (!componentId.equals(other.componentId))
			return false;
		if (topComponentId == null) {
			if (other.topComponentId != null)
				return false;
		} else if (!topComponentId.equals(other.topComponentId))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "BomComponentId [topComponentId=" + topComponentId + ", componentId=" + componentId + "]";
	}
}
