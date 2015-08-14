package irt.data.components.movement;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonProperty;

public class ComponentsMovementBean {
	@JsonProperty("pf")
	private Byte 				placeFromId;
	@JsonProperty("pt")
	private Byte 				placeToId;
	@JsonProperty("cf")
	private Integer 				companyFromId;
	@JsonProperty("ct")
	private Integer 				companyToId;
	@JsonProperty("d")
	private String					description;
	@JsonProperty("dt")
	private Date 					date;
	@JsonProperty("s")
	private int						status;
	@JsonProperty("cs")
	private Map<Integer, ComponentQtyBean> 	componentsToMove = new HashMap<>();

	public Byte getPlaceFromId() {
		return placeFromId;
	}
	public void setPlaceFromId(Byte placeFromId) {
		this.placeFromId = placeFromId;
	}
	public Byte getPlaceToId() {
		return placeToId;
	}
	public void setPlaceToId(Byte placeToId) {
		this.placeToId = placeToId;
	}
	public Integer getCompanyFromId() {
		return companyFromId;
	}
	public void setCompanyFromId(int companyFromId) {
		this.companyFromId = companyFromId;
	}
	public Integer getCompanyToId() {
		return companyToId;
	}
	public void setCompanyToId(int companyToId) {
		this.companyToId = companyToId;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public Map<Integer, ComponentQtyBean> getComponentsToMove() {
		return componentsToMove;
	}
	public void setComponentsToMove(Map<Integer, ComponentQtyBean> componentsToMove) {
		this.componentsToMove = componentsToMove;
	}
	@Override
	public String toString() {
		return "ComponentsMovementBean [placeFromId=" + placeFromId
				+ ", placeToId=" + placeToId + ", companyFromId="
				+ companyFromId + ", companyToId=" + companyToId
				+ ", description=" + description + ", date=" + date
				+ ", status=" + status + ", componentsToMove="
				+ componentsToMove + "]";
	}
}
