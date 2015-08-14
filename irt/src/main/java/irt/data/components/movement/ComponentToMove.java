package irt.data.components.movement;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

import irt.data.components.Component;
import irt.data.components.movement.interfaces.ComponentQuantity;


public class ComponentToMove implements ComponentQuantity{

	private int id = -1;
	@JsonProperty("pn")
	private String partNumber;
	private String mfrPN;
	@JsonProperty("dscr")
	private String description;
	@JsonProperty("sQty")
	private int stockQuantity;
	@JsonProperty("qtyToM")
	private	int	quantityToMove;
	@JsonProperty("lctn")
	private String location;

	private String		color;

	public ComponentToMove(int id, String partNumber, String mfrPN,	String description, int stockQuantity, int quantityToMove, String location) {
		this(id);
		this.partNumber = partNumber;
		this.mfrPN = mfrPN;
		this.description = description;
		this.stockQuantity = stockQuantity;
		this.quantityToMove = quantityToMove;
		this.location = location;
	}

	public ComponentToMove(int componentId) {
		id = componentId;
	}

	public ComponentToMove(Component component) {
		id = component.getId();
		partNumber = component.getPartNumberF();
		mfrPN = component.getMfrPN();
		description = component.getDescription();
		stockQuantity = component.getQuantity();
		location = component.getLocation();
	}

	@JsonIgnore
	public boolean setQuantityToMove(int quantityToMove, boolean addToStock) {
		boolean wasSet = false;

		int q = id>0 ? stockQuantity : -1;

		if((q >= quantityToMove || addToStock) && quantityToMove>=0){
			if(q == quantityToMove)
				setColor("_blue");
			else
				setColor("_green");
			wasSet = true;
			this.quantityToMove = quantityToMove;
		}else{
				setColor("_red");
				this.quantityToMove = q;
		}
	return wasSet;
	}

	public String	getColor()			{ return color!=null ? color : "";	}
	public String	getPartNumber()		{ return partNumber!=null ? partNumber : "";		}
	public String	getDescription()	{ return description!=null ? description : "";		}
	public String	getMfrPN()			{ return mfrPN!=null ? mfrPN : "";	}
	public String	getLocation()		{ return location!=null ? location : "";		}
	@Override
	public int getId()				{ return id;	}
	@Override
	public int getStockQuantity()		{ return stockQuantity;	}
	@Override
	public int getQuantityToMove()	{ return quantityToMove;	}

	/**
	 * Use only for Json
	 * @param quantityToMove
	 */
	@Deprecated
	public void setQuantityToMove(int quantityToMove) {
		this.quantityToMove = quantityToMove;
	}

	public void setColor(String color) { this.color = color;	}

	@Override
	public boolean equals(Object obj) {
		return obj!=null ? obj.hashCode() == hashCode() : false;
	}

	@Override
	public int hashCode() {
		return id>0 ? id : super.hashCode();
	}

	@Override
	public void addQuantity(int qty) {
		this.quantityToMove += qty;
	}

	public void setStockQuantity(int stockQuantity) {
		this.stockQuantity = stockQuantity;
	}

	public void resetQuantityToMove(int oldQuantityToMove) {
		stockQuantity += oldQuantityToMove;
	}

	@Override
	public String toString() {
		return "ComponentToMove [id=" + id + ", partNumber=" + partNumber
				+ ", mfrPN=" + mfrPN + ", description=" + description
				+ ", stockQuantity=" + stockQuantity + ", quantityToMove="
				+ quantityToMove + ", location=" + location + ", color="
				+ color + "]";
	}
}
