package irt.web.view.beans;

public class UserView {

	private String username;
	private String password;
	private boolean rememberMe;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isRememberMe() {
		return rememberMe;
	}

	public void setRememberMe(boolean rememberMe) {
		this.rememberMe = rememberMe;
	} 

	@Override
	public String toString() {
		return "\n\tUserView [\n\t\tusername=" + username +
				",\n\t\tpassword=" + password +
				",\n\t\trememberMe=" + rememberMe + "]";
	}
}
