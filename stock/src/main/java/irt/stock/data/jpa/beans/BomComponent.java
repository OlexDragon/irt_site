package irt.stock.data.jpa.beans;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="bom")
public class BomComponent {

	protected BomComponent() { }
	public BomComponent(Long topId, Component component, BomReference reference) {
		id = new BomComponentId(topId, component.getId());
		this.reference = reference;
		this.component = component;
	}

	@EmbeddedId private BomComponentId id;

	@ManyToOne(targetEntity=PartNumber.class)
	@JoinColumn(name="id_top_comp", insertable=false, updatable=false)
	private PartNumber topPartNumber;

	@ManyToOne
	@JoinColumn(name="id_components", insertable=false, updatable=false)
	private Component component;

	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="id_bom_ref")
	private BomReference reference;

	public BomComponentId getId() { return id; }
	public PartNumber getTopPartNumber() { return topPartNumber; }
	public Component getComponent() { return component; }
	public BomReference getReference() { return reference; }

	public void setReference(BomReference reference) { this.reference = reference; }

	@Override
	public String toString() {
		return "BomComponent [id=" + id + ", component=" + component + ", reference=" + reference + "]";
	}
}
