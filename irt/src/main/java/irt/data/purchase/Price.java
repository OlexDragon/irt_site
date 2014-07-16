package irt.data.purchase;


import irt.data.dao.ErrorDAO;

import java.text.DecimalFormat;

public class Price {

	private long value;
	private boolean error;

	public Price(String priceStr) {
		if(priceStr!=null){
			String[] priceSplit = priceStr.split("\\.");
			priceSplit[0] = priceSplit[0].replaceAll("\\D", "");
			if(priceSplit[0].isEmpty())
				value = 0;
			else
				try {
					value = Long.parseLong(priceSplit[0])*1000000;
					error = value <0;
				} catch (NumberFormatException e) {
					new ErrorDAO().saveError(e, "Price.Price");
				    error = true;
				}
			if(priceSplit.length>1){
				priceSplit[1] = priceSplit[1].replaceAll("\\D", "");
				if(!priceSplit[1].isEmpty()){
					if(priceSplit[1].length()<9)
						priceSplit[1] = String.format("%-6s", priceSplit[1]).replace(' ','0');
					else if(priceSplit[1].length()>9)
						priceSplit[1] = priceSplit[1].substring(0,6);
					value += Integer.parseInt(priceSplit[1]);
				}
			}
		}
	}

	public Price(String price, int orderQuantity) {
		this(price);
		value *= orderQuantity;
	}

	public Price(long value) {
		this.value = value;
	}

	public Price(long value, String percentage) {
		double perc = Double.parseDouble(percentage)/100;
		this.value = (long) (value * perc);
	}

	public String getValue(int minLength, int maxLength) {
		String str = "#.";
		maxLength -= minLength;
		for(;minLength>0;minLength--)
			str += '0';
		for(;maxLength>0;maxLength--)
			str += '#';
		DecimalFormat nf = new DecimalFormat(str);

		return error ? "Error" : value==0 ? null : nf.format(value/1000000.0);
	}

	public long getValueLong() {
		return error ? 0 : value;
	}

	public void addValue(long value) {
		this.value += value;
	}

	@Override
	public boolean equals(Object obj) {
		return obj!=null ? obj.hashCode()==hashCode() : false;
	}

	@Override
	public int hashCode() {
		return Long.valueOf(value).hashCode();
	}

//	@Override
//	public String toString() {
//		return "Price [value=" + getValue(1, 6) + ", error="+error+"]";
//	}
}
