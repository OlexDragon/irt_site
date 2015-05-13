package irt.web.workers.beans;

import irt.web.entities.all.UsersEntity.Permission;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class LoginDetails implements UserDetails{//or SocialUser
	private static final long serialVersionUID = -6350018225137443424L;

	private final Logger logger = LogManager.getLogger();

	private Long id;
	private String username;
    private String password;
    private Long permissions;

  public Long getId() {
		return logger.exit(id);
	}

	@Override
	public String getPassword() {
		return logger.exit(password);
	}

	@Override
	public String getUsername() {
		return logger.exit(username);
	}

	@Override
	public boolean isAccountNonExpired() {
		return logger.exit(!Permission.hasPermission(permissions, Permission.ACCOUNT_EXPIRED));
	}

	@Override
	public boolean isAccountNonLocked() {
		return logger.exit(!Permission.hasPermission(permissions, Permission.LOCKED));
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return logger.exit(!Permission.hasPermission(permissions, Permission.CREDENTIALS_EXPIRED));
	}

	@Override
	public boolean isEnabled() {
		return logger.exit(!Permission.hasPermission(permissions, Permission.DISABLED));
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return logger.exit(permissions!=null ? Permission.toPermissions(permissions) : getDefaultPermission()); 
	}

	public Set<Permission> getDefaultPermission() {
		Set<Permission> permissions = new HashSet<>();
		permissions.add(Permission.DEFAULT);
		return logger.exit(permissions);
	}

	@Override
	public String toString() {
		return "\n\tLoginDetails [\n\t\tid=" + id +
				",\n\t\tusername=" + username +
				",\n\t\tpassword=" + password +
				",\n\t\tpermissions=" + permissions
				+ "]";
	}

	public static Builder getBuilder() {
		return new Builder();
	}

	public static class Builder{


		private final Logger logger = LogManager.getLogger();

		private Long id;
		private String username;
	    private String password;
	    private Long permissions;

	    public Builder setId(Long id) {
	    	logger.entry(id);
			this.id = id;
			return this;
		}
		public Builder setUsername(String username) {
	    	logger.entry(username);
			this.username = username;
			return this;
		}
		public Builder setPassword(String password) {
	    	logger.entry(password);
			this.password = password;
			return this;
		}
		public Builder setPermissions(Long permission) {
	    	logger.entry(permission);
			this.permissions = permission;
			return this;
		}

		public LoginDetails build() {
			LoginDetails loginDetails = new LoginDetails();

			loginDetails.id 		= id;
			loginDetails.username 	= username;
			loginDetails.password	= password;
			loginDetails.permissions= permissions;

			return logger.exit(loginDetails);
		}
	}
}
