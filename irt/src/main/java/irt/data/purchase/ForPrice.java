package irt.data.purchase;

import irt.work.ComboBoxField;

public class ForPrice implements ComboBoxField{

	public static int INSERT = 0;
	public static int UPDATE = 1;
	public static int SAVED = 3;

	private int forUnits;
	private Price price;
	private Price newPrice;

	public ForPrice(Price price, int forUnits) {
		this.price = price;
		this.forUnits = forUnits;
	}

	public ForPrice(Price price, int forUnits, int status) {
		if(status==SAVED)
			this.price = price;
		else
			this.newPrice = price;
		this.forUnits = forUnits;
	}

	public int getForUnits() {
		return forUnits;
	}

	public Price getPrice() {
		return newPrice==null ? price : newPrice;
	}

	@Override
	public String getValue() {
		return ""+forUnits;
	}

	@Override
	public String getText() {
		return ""+forUnits;
	}

	public int getStatus() {
		return price==null ? INSERT : newPrice==null ? SAVED : UPDATE;
	}

	public void setPrice(Price newPrice) {
		if(newPrice!=null && !newPrice.equals(price))
			this.newPrice = newPrice;
		else
			newPrice = null;
	}

	public void setPrice() {
		price = newPrice;
		newPrice = null;
	}

	@Override
	public String toString() {
		return "ForPrice [forUnit=" + forUnits + ", price=" + price
				+ ", newPrice=" + newPrice + ", getStatus()=" + getStatus()
				+ "]";
	}

	@Override
	public boolean equals(Object obj) {
		return obj!=null ? obj.hashCode()==hashCode() : false;
	}

	@Override
	public int hashCode() {
		return forUnits;
	}

	public boolean isSet() {
		return (newPrice!=null && newPrice.getValueLong()>0 || price!=null && price.getValueLong()>0) && forUnits >0;
	}

	public void setForUnits(int forUnits) {
		this.forUnits = forUnits;
	}

	public boolean isChanged() {
		return newPrice!=null;
	}
}
