package irt.data.components.movement.interfaces;

public interface ComponentQuantity {

	public int getId();
	public int getStockQuantity();
	public void addQuantity(int qty);
	public String getColor();
	public int getQuantityToMove();
}
