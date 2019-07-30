package irt.entities;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import irt.controllers.components.interfaces.ValueText;

@Entity
@Table(name="manufacture")
public class ManufactureEntity implements Serializable, ValueText{
	private static final long serialVersionUID = 5517766516881906893L;

	@Id
	private String id;		public String getId() { return id; }		public void setId(String id) { this.id = id; }
	private String name;	public String getName() { return name; }	public void setName(String name) { this.name = name; }
	private String link;	public String getLink() { return link; }	public void setLink(String link) { this.link = link; }

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
		ManufactureEntity other = (ManufactureEntity) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	@Transient
	public String getValue() { return id; }
	@Override
	@Transient
	public String getText() { return name; }
}
