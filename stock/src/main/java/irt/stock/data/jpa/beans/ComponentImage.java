package irt.stock.data.jpa.beans;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class ComponentImage {


	protected ComponentImage() { }
	public ComponentImage(Long componentId, ImageLink imageLink) {
		this.componentId = componentId;
		this.imageLink = imageLink;
	}

	@Id
	private Long componentId;
	@ManyToOne  @JoinColumn(name = "image_link_id", referencedColumnName="id", nullable=false, insertable=true, updatable=true)
	private ImageLink imageLink;

	public Long getId() {
		return componentId;
	}
	public void setId(Long id) {
		this.componentId = id;
	}

	public ImageLink getImageLink() {
		return imageLink;
	}
	public void setImageLink(ImageLink imageLink) {
		this.imageLink = imageLink;
	}

	@Override
	public String toString() {
		return "ComponentImage [id=" + componentId + ", imageLink=" + imageLink + "]";
	}
}
