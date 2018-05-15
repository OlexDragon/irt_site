
package irt.stock.data.jpa.services;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Before;
import org.junit.Test;
import org.springframework.security.core.GrantedAuthority;

import irt.stock.data.jpa.beans.User;

public class UserPrincipalTest {

	private UserPrincipal up;

	@Before
	public void before() {
		up = new UserPrincipal(new User("User Name", "password", "Firstname", "Lastname", UserRoles.ADMIN.getPermission()|UserRoles.STOCK.getPermission(), null, null));
	}

	@Test
	public void test() {
		Collection<? extends GrantedAuthority> a = up.getAuthorities();
		assertFalse(a.isEmpty());
		assertEquals(2, a.size());
		final ArrayList<GrantedAuthority> actual = new ArrayList<>(a);
		assertThat(actual, hasItem(UserRoles.ADMIN));
		assertThat(actual, hasItem(UserRoles.ADMIN));
	}

	@Test
	public void someTest() {

		Pattern p = Pattern.compile("[ -]");
		Matcher m = p.matcher("1-6");
		if (m.find()) {
		   int position = m.start();
			assertEquals(1, position);
		}


		m = p.matcher("1 6");
		if (m.find()) {
		   int position = m.start();
			assertEquals(1, position);
		}

		m = p.matcher("1111-67899");
		if (m.find()) {
		   int position = m.start();
			assertEquals(4, position);
		}
	}
}
