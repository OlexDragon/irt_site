package irt.data.components.movement;

public class CompanyQty {

	private int		id;
	private String	name;
	private int		qty;

	public CompanyQty(int id, String name, int qty) {
		super();
		this.id = id;
		this.name = name;
		this.qty = qty;
	}

	public CompanyQty(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public int getQty() {
		return qty;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setQty(int qty) {
		this.qty = qty;
	}

	@Override
	public boolean equals(Object obj) {
		return obj!=null ? obj.hashCode() == hashCode() : false;
	}

	@Override
	public int hashCode() {
		return id;
	}

	public void substruct(int qty) {
		this.qty -= qty;
	}

	@Override
	public String toString() {
		return "CompanyQty [id=" + id + ", name=" + name + ", qty=" + qty + "]";
	}
}
