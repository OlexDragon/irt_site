package irt.stock.data.jpa.beans;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="users")
public class User {

	@Id @GeneratedValue
	private Long id;
	private String username;
	private String password;
	private String firstname;
	private String lastname;
	private Long permission;
	private String extension;
	private String eMail;

	protected User(){}
	public User(String username, String password, String firstName, String lastName, Long permission, String extension, String email) {

		this.username = username;
		this.password = password;
		this.firstname = firstName;
		this.lastname = lastName;
		this.permission = permission;
		this.extension = extension;
		this.eMail = email;
	}

	public Long   getId() 			{ return id; }
	public String getUsername() 	{ return username; }
	public String getPassword() 	{ return password; }
	public String getFirstName() 	{ return firstname; }
	public String getLastName() 	{ return lastname; }
	public Long   getPermission() 	{ return permission; }
	public String getExtension() 	{ return extension; }
	public String getEmail() 		{ return eMail; }

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
		User other = (User) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", username=" + username + ", password=" + password + ", firstName=" + firstname
				+ ", lastName=" + lastname + ", permision=" + permission + ", extension=" + extension + ", email="
				+ eMail + "]";
	}
}
