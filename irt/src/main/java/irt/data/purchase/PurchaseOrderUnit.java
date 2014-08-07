package irt.data.purchase;

import irt.data.manufacture.ManufacturePartNumber;

import java.util.ArrayList;
import java.util.List;


public class PurchaseOrderUnit {

	private int componentId;
	private String partNumberStr;
	private String description;
	private int mfrPNIndex;
	private List<ManufacturePartNumber> mfrPNs;
	private int orderQuantity;
	private int newOrderQuantity;
	private Price price;
	private Price newPrice;

	public PurchaseOrderUnit(int componentId, String partNumberStr, String description, ManufacturePartNumber mfrPN) {
		this.componentId = componentId;
		this.partNumberStr = partNumberStr;
		this.description = description;
		mfrPNs = new ArrayList<>();
		mfrPNs.add(mfrPN);
	}

	public PurchaseOrderUnit(int componentId) {
		this.componentId = componentId;
	}

	public int getComponentId() {
		return componentId;
	}

	public String getPartNumberStr() {
		return partNumberStr;
	}

	public String getDescription() {
		return description;
	}

	public void setComponentId(int componentId) {
		this.componentId = componentId;
	}

	public void setPartNumberStr(String partNumberStr) {
		this.partNumberStr = partNumberStr;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getOrderQuantity() {
		return newOrderQuantity;
	}

	public void setOrderQuantity() {
		orderQuantity = newOrderQuantity;
	}
	public void setOrderQuantity(int orderQuantity) {
		newOrderQuantity = orderQuantity;
	}

	public String getPrice() {
		return newPrice!=null ? newPrice.getValue(2, 6) : price!=null ? price.getValue(2, 6) : null;
	}

	public Long getPriceLong() {
		return newPrice!=null ? newPrice.getValueLong() : price!=null ? price.getValueLong() : -1;
	}

	public void setPrice() {
		price = newPrice;
		newPrice = null;
	}

	public void setPrice(Price price) {
		if( !price.equals(this.price))
			newPrice = price;
		else
			newPrice = null;
	}

	public void setPrice(String priceStr) {
		if(priceStr!=null)
			setPrice(new Price(priceStr));
	}

	public void addMfrPM(int id, String mfrPN, String mfr){
//TODO		mfrPNs.add(new ManufacturePartNumber(id, mfrPN, mfr));
	}

	public int getMfrPNIndex() {
		return mfrPNIndex;
	}

	public void setMfrPNIndex(int mfrPNIndex) {
		this.mfrPNIndex = mfrPNIndex;
	}

	public ManufacturePartNumber getMfrPN() {
		int index = mfrPNs.indexOf(new ManufacturePartNumber(mfrPNIndex));
		return mfrPNs.get(index<0 ? 0 : index);
	}

	@Override
	public String toString() {
		return "PurchaseOrderUnit [componentId=" + componentId
				+ ", partNumberStr=" + partNumberStr + ", description="
				+ description + ", mfrPNIndex=" + mfrPNIndex + ", mfrPNs="
				+ mfrPNs + ", orderQuantity=" + orderQuantity + ", price="
				+ price + "]";
	}

	public int size() {
		return mfrPNs!=null ? mfrPNs.size() : 0;
	}

	public List<ManufacturePartNumber> getMfrPNs() {
		return mfrPNs;
	}

	@Override
	public boolean equals(Object obj) {
		return obj!=null ? obj.hashCode()==hashCode() : false;
	}

	@Override
	public int hashCode() {
		return componentId<0 ? super.hashCode() : componentId;
	}

	public boolean isSet() {
		return componentId>0 && price!=null && price.getValueLong()>0 && orderQuantity>0;
	}

	public Price getTotalPrice() {
		return new Price(price.getValue(0, 6), orderQuantity);
	}

	public String getPartNumberLink() {
		return "<a href=\"/irt/part-numbers?pn="+partNumberStr+"\">"+partNumberStr+"</a>";
	}

	public void cancel(){
		newOrderQuantity = orderQuantity;
		newPrice = price;
	}
}
