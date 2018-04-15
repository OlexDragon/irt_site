package irt.stock.data.jpa.beans;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Manufacture {
	protected Manufacture() { }
	public Manufacture(String id, String name, String link) {
		this.id = id;
		this.name = name;
		this.link = link;
	}

	@Id
	private String id;
	@Column(name="`name`")
	private String name;
	private String link;

	public String getId() { return id; }
	public String getName() { return name; }
	public String getLink() { return link; }

	@Override
	public int hashCode() {
		return 31 + ((id == null) ? 0 : id.hashCode());
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Manufacture other = (Manufacture) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Manufacture [id=" + id + ", name=" + name + ", link=" + link + "]";
	}
}
