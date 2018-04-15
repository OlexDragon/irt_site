package irt.stock.data.jpa.beans;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.Table;

@Entity
@Table(name="movement_details")
public class ComponentMovementDetail {

	protected ComponentMovementDetail() {}
	public ComponentMovementDetail(ComponentMovement componentMovement, Component component, Long qty, Long oldQty) {
		this.id = new ComponentMovementDetailId(componentMovement, component);
		this.qty = qty;
		this.oldQty = oldQty;
	}

	@EmbeddedId private ComponentMovementDetailId id;

	private Long qty;
	@JoinColumn(nullable=true)
	private Long oldQty;

	public ComponentMovementDetailId getId() 				{ return id; }
	public Long 					getQty() 				{ return qty; }
	public Long 					getOldQty() 			{ return oldQty; }

	@Override
	public String toString() {
		return "ComponentMovementDetail [id=" + id + ", qty=" + qty + ", oldQty=" + oldQty + "]";
	}
}
