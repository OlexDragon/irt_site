package irt.data.user;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.HashMap;
import java.util.Map;

public class LogIn {

	/** The Log In Lifetime in milliseconds */
	public static final int LIFETIME = 4*60*60*1000;//milliseconds

	private long lastAccessTime;
	private UserBean userBean;
	private long lifeTime;

	public LogIn(UserBean userBean){
		this.lifeTime = LIFETIME;
		this.userBean = userBean;
		lastAccessTime = System.currentTimeMillis();
	}

	public UserBean getUserBean() {

		UserBean ub = null;

		if(lastAccessTime - lifeTime > 0){
			lastAccessTime = System.currentTimeMillis();
			ub = userBean;
		}

		if(ub==null)
			ub = new UserBean();

		return ub;
	}

	public long getLastAccessTime() {
		return lastAccessTime;
	}

	public long getId() {
		return userBean!=null ? userBean.getID() : 0;
	}

    @Override
	public boolean equals(Object obj) {
		return obj!=null ? obj.hashCode()==hashCode() : false;
	}

	@Override
	public int hashCode() {
		return getId()>0 ? new Long(getId()).hashCode() : super.hashCode();
	}

	public boolean isValid() {
		boolean isValid = (System.currentTimeMillis() - lastAccessTime) < lifeTime;
		return isValid && userBean!=null ? userBean.isValid() : false;
	}

	@Override
	public String toString() {
		return "LogIn [userBean=" + userBean + ", lifeTime=" + lifeTime
				+ ", lastAccessTime=" + lastAccessTime + "]";
	}

	public static LogIn parseLogIn(String logInStr) throws GeneralSecurityException, IOException {
		LogIn logIn = null;
		if(logInStr!=null && logInStr.startsWith("LogIn")){

			logInStr = logInStr.substring(logInStr.indexOf('[')+1, logInStr.lastIndexOf(']'));

			int index = logInStr.indexOf(']')+1;
			UserBean userBean = UserBean.parseUserBean(logInStr.substring(logInStr.indexOf('=')+1, index));

			String[] split = logInStr.substring(index+2).split(", ");
			Map<String, String> map = new HashMap<>();
			for(String s:split){
				String[] valueSplit = s.split("=");
				if(valueSplit.length==2)
					map.put(valueSplit[0], valueSplit[1]);
			}

			long tmpLong;
			logInStr = map.get("lifeTime");
			if(logInStr!=null)
				tmpLong = Long.parseLong(logInStr);
			else
				tmpLong = 0;

			logIn = new LogIn(userBean);
			logIn.setLifeTime(tmpLong);

			logInStr = map.get("lastAccessTime");
			if(logInStr!=null)
				tmpLong = Long.parseLong(logInStr);
			else
				tmpLong = 0;

			logIn.setLastAccessTime(tmpLong);
		}

		return logIn;
	}

	public long getLifeTime() {
		return lifeTime;
	}

	public void setLifeTime(long lifeTime) {
		this.lifeTime = lifeTime;
	}

	public void setLastAccessTime(long lastAccessTime) {
		this.lastAccessTime = lastAccessTime;
	}

	public String getUsername(){
		return userBean!=null ? userBean.getUsername() : "";
	}

	public String getPassword(){
		return userBean!=null ? userBean.getPassword() : "";
	}
}
