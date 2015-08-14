package irt.data.components.movement;

import org.codehaus.jackson.annotate.JsonProperty;

public class ComponentQtyBean {

	@JsonProperty("id")
	private Integer componentId;
	@JsonProperty("aq")
	private int availableQty;
	@JsonProperty("mq")
	private int qtyToMove;

	public Integer getComponentId() {
		return componentId;
	}
	public ComponentQtyBean setComponentId(Integer componentId) {
		this.componentId = componentId;
		return this;
	}
	public int getAvailableQty() {
		return availableQty;
	}
	public void setAvailableQty(int availableQty) {
		this.availableQty = availableQty;
	}
	public int getQtyToMove() {
		return qtyToMove;
	}
	public ComponentQtyBean setQtyToMove(int qtyToMove) {
		this.qtyToMove = qtyToMove;
		return this;
	}
	@Override
	public boolean equals(Object obj) {
		return obj!=null ? obj.hashCode()==hashCode() : false;
	}
	@Override
	public int hashCode() {
		return componentId!=null ? componentId.hashCode() : super.hashCode();
	}
	@Override
	public String toString() {
		return "ComponentQtyBean [componentId=" + componentId
				+ ", availableQty=" + availableQty + ", qtyToMove=" + qtyToMove
				+ "]";
	}
}
