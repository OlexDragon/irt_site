package irt.data.components.movement;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

import irt.data.components.movement.interfaces.ComponentQuantity;


public class ComponentQty implements ComponentQuantity{
	int id;
	@JsonProperty("qty")
	int quantity;

	public ComponentQty(int componentId, int qty) {
		this.id = componentId;
		quantity = qty;
	}

	public int getId() { return id;	}

	public int getQuantity() {
		return quantity;
	}
	@JsonIgnore
	public int getStockQuantity() 	{ return quantity;		}

	public void settId	(int componentId) 	{ this.id	= componentId;	}
	public void setQuantity		(int quantity) 		{ this.quantity 	= quantity;		}

	@Override
	public boolean equals(Object obj) {
		return obj!=null ? obj.hashCode()==hashCode() : false;
	}

	@Override
	public int hashCode() {
		return id;
	}

	public void addQuantity(int quantity) {
		this.quantity += quantity;
	}

	@Override
	@JsonIgnore
	public String getColor() {
		return null;
	}

	@Override
	@JsonIgnore
	public int getQuantityToMove() {
		return quantity;
	}

	@Override
	public String toString() {
		return "ComponentQty [componentId=" + id + ", quantity="+ quantity + "]";
	}
}