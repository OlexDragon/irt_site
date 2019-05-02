package irt.stock.data.jpa.beans;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.fasterxml.jackson.annotation.JsonFormat;

import irt.stock.data.jpa.beans.Company.CompanyType;

@Entity
@Table(name="movement")
public class ComponentMovement {
	protected ComponentMovement() { }
	public ComponentMovement(User user, Company fromCompany, Company toCompany, String description, Date dateTime) {
		this.user = user;
		from = fromCompany.getType();
		this.fromCompany = fromCompany;
		to = toCompany.getType();
		this.toCompany = toCompany;
		this.description = description;
		this.dateTime = dateTime;
	}

	@Id @GeneratedValue
	private Long id;
	@Enumerated(EnumType.ORDINAL)
	@Column(name="`from`")
	private CompanyType from;
	@Enumerated(EnumType.ORDINAL)
	@Column(name="`to`")
	private CompanyType to;
	private String description;
	private int status = 2;
	@Temporal(TemporalType.TIMESTAMP)
	@JsonFormat(pattern="dd.MMM.yyyy")
	private Date dateTime;

	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="who")
	private User user;
	@ManyToOne(fetch=FetchType.EAGER)
	@NotFound(action = NotFoundAction.IGNORE)
	@JoinColumn(name="from_detail", nullable=true)
	private Company fromCompany;
	@ManyToOne(fetch=FetchType.EAGER)
	@NotFound(action = NotFoundAction.IGNORE)
	@JoinColumn(name="to_detail", nullable=true)
	private Company toCompany;

	public Long 		getId() 		{ return id; }
	public User 		getUser() 		{ return user; }
	public CompanyType 	getFrom() 		{ return from; }
	public Company 		getFromCompany(){ return fromCompany; }
	public CompanyType 	getTo() 		{ return to; }
	public Company 		getToCompany() 	{ return toCompany; }
	public String 		getDescription(){ return description; }
	public int 			getStatus() 	{ return status; }
	public Date 		getDateTime() 	{ return dateTime; }

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
		ComponentMovement other = (ComponentMovement) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "ComponentMovement [id=" + id + ", from=" + from + ", to=" + to + ", description=" + description
				+ ", status=" + status + ", dateTime=" + dateTime + ", user=" + user + ", fromCompany=" + fromCompany
				+ ", toCompany=" + toCompany + "]";
	}
}
