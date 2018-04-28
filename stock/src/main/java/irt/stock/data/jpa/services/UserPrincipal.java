package irt.stock.data.jpa.services;

import java.util.Collection;
import java.util.Optional;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import irt.stock.data.jpa.beans.User;

public class UserPrincipal implements UserDetails {
	private static final long serialVersionUID = -1600005108091389940L;

	public UserPrincipal(User user) {
		this.user = user;
	}

	private User user;
	public User getUser() { return user; }

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return UserRoles.getAuthorities(user.getPermission());
	}

	@Override
	public String getPassword() {
		return user.getPassword();
	}

	@Override
	public String getUsername() {
		return user.getUsername();
	}

	@Override
	public boolean isAccountNonExpired() {
		return isAccountNonLocked();
	}

	@Override
	public boolean isAccountNonLocked() {
		return Optional.ofNullable(user.getPermission()).filter(p->p>0).isPresent();
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return isAccountNonLocked();
	}

	@Override
	public boolean isEnabled() {
		return isAccountNonLocked();
	}

	@Override
	public String toString() {
		return "UserPrincipal [user=" + user + "]";
	}
}
