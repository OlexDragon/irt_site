package irt.stock.data.jpa.beans;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
public class BomHistory {

	protected BomHistory() { }

	public BomHistory(Long pcaId, Long userId, Long componentId, Long oldReference, Long newReference, BomAction bomAction) {
		this.pcaId = pcaId;
		this.userId = userId;
		this.componentId = componentId;
		this.oldReference = oldReference;
		this.newReference = newReference;
		this.action = bomAction;
	}

	@Id @GeneratedValue
	private Long id;
	@Column(nullable=false)
	private Long userId;
	@Column(nullable=false)
	private Long pcaId;
	@Column(nullable=false)
	private Long componentId;
	private Long oldReference;
	private Long newReference;
	@Enumerated(EnumType.STRING)
	@Column(nullable=false)
	private BomAction action;
	@Temporal(TemporalType.TIMESTAMP)
	@JsonFormat(pattern="dd.MMM.yy")
	public Date actionDate;

	@PrePersist
	protected void onCreate() {
		actionDate = new Date();
	}

	public enum BomAction {

		CREATED,
		INSERTED,
		DELETED,
		REPLACED,
		CHANGED,
		DELETED_BOM
	}

	public Long getId() {
		return id;
	}

	public BomAction getAction() {
		return action;
	}

	public Date getActionDate() {
		return actionDate;
	}

	public Long getPcaId() {
		return pcaId;
	}

	public Long getComponentId() {
		return componentId;
	}

	public Long getOldReference() {
		return oldReference;
	}

	public Long getNewReference() {
		return newReference;
	}
}
