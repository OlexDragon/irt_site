package irt.data.purchase;

import java.math.BigDecimal;

public class Tax implements Comparable<Object>{
	private String name;
	private BigDecimal pursent;

	public Tax(String name, BigDecimal pursent) {
		super();
		this.name = name;
		this.pursent = pursent;
	}

	public String getName() {
		return name;
	}

	public BigDecimal getPursent() {
		return pursent;
	}

	@Override
	public boolean equals(Object obj) {
		return obj!=null ? obj.hashCode()==hashCode() : false;
	}

	@Override
	public int hashCode() {
		return name.hashCode();
	}

	@Override
	public String toString() {
		return name;
	}

	@Override
	public int compareTo(Object obj) {
		return obj!=null ? hashCode()-obj.hashCode() : -1;
	}
}
