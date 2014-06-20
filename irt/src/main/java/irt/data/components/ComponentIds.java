package irt.data.components;

public class ComponentIds {
	private int id;
	private String partNumber;
	private String mfrPartNumber;
	private int qty;

	public ComponentIds(int id, String partNumber, String mfrPartNumber) {
		this.id = id;
		this.partNumber = partNumber;
		this.mfrPartNumber= mfrPartNumber;
	}

	public ComponentIds(int componentId, String partNumber, String mfrPartNumber, int qty) {
		this(componentId, partNumber, mfrPartNumber);
		this.qty = qty;
	}

	public int 	getId() 	{ return id;	}
	public int 	getQuantity()	{ return qty;	}
	public String getPartNumber() 	{ return partNumber;	}
	public String getMfrPartNumber(){ return mfrPartNumber;	}

	public void setQuantity	(int qty){ this.qty = qty;	}
	public void setId	(int id) { 	this.id = id;	}
	public void setPartNumber	(String partNumber) 	{ this.partNumber = partNumber;			}
	public void setMfrPartNumber(String mfrPartNumber) 	{ this.mfrPartNumber = mfrPartNumber;	}

	public boolean hasBom() { return mfrPartNumber!=null && mfrPartNumber.contains("IRT BOM") ? true : false;	}

	@Override
	public boolean equals(Object obj) {
		return obj!=null && obj.hashCode()>0 ? hashCode()==obj.hashCode() : false;
	}

	@Override
	public int hashCode() {
		return id;
	}

	@Override
	public String toString() {
		return  partNumber;
	}

	public void setQuantity(String quantityStr) {
		if(quantityStr!=null){
			quantityStr = quantityStr.replaceAll("\\D", "");
			if(!quantityStr.isEmpty())
				qty = Integer.parseInt(quantityStr);
			else
				qty = 0;
		}else
			qty = 0;

	}
}
