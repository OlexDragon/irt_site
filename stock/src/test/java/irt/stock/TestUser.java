package irt.stock;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import irt.stock.data.jpa.services.UserRoles;

public class TestUser implements UserDetails{
	private static final long serialVersionUID = -7961235450835961071L;

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return new ArrayList<>(Arrays.asList(UserRoles.values()));
	}

	@Override
	public String getPassword() {
		return "password";
	}

	@Override
	public String getUsername() {
		return "UserName";
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
}
