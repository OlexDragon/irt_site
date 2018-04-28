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

	@Column(name="id_top_comp")
	private Long topComponentId;
	@Column(name="id_components")
	private Long componentId;

	public Long getTopComponentId() { return topComponentId; }
	public Long getComponentId() { return componentId; }
}
