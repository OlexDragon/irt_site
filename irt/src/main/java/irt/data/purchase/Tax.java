package irt.data.purchase;

public class Tax implements Comparable<Object>{
	private String name;
	private Price pursent;

	public Tax(String name, Price pursent) {
		super();
		this.name = name;
		this.pursent = pursent;
	}

	public String getName() {
		return name;
	}

	public Price getPursent() {
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
