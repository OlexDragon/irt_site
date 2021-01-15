
package irt.stock.data.jpa.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.test.context.event.annotation.BeforeTestClass;

import irt.stock.data.jpa.beans.User;
import irt.stock.data.jpa.beans.User.Status;

public class UserPrincipalTest {

	private UserPrincipal up;

	@BeforeTestClass
	public void before() {
		up = new UserPrincipal(new User("User Name", "password", "Firstname", "Lastname", UserRoles.ADMIN.getPermission()|UserRoles.STOCK.getPermission(), null, null, Status.ACTIVE));
	}

	@Test
	public void test() {
		Collection<? extends GrantedAuthority> a = up.getAuthorities();
		assertFalse(a.isEmpty());
		assertEquals(2, a.size());
//		final ArrayList<GrantedAuthority> actual = new ArrayList<>(a);
//		assertThat(actual, hasItem(UserRoles.ADMIN));
//		assertThat(actual, hasItem(UserRoles.ADMIN));
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
