package irt.stock.data.jpa.beans;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class ImageLink {

	protected ImageLink() { }
	public ImageLink( String link) {
		this.link = link;
	}

	@Id @GeneratedValue
	private Long id;
	private String link;

	public Long getId() {
		return id;
	}

	public String getLink() {
		return link;
	}

	@Override
	public String toString() {
		return "ImageLink [id=" + id + ", link=" + link + "]";
	}
}
