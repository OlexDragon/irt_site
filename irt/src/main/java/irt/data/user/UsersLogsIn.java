package irt.data.user;

import irt.data.CookiesWorker;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class UsersLogsIn {

	private static Map<Long, LogIn> logsIn = new HashMap<>();

	/**
	 * add LogIn Cookie, set session attribute , put in static variable Map<Long, LogIn> logsIn
	 * @param request
	 * @param response
	 * @param userBean
	 * @return new LogIn
	 */
	public static LogIn addUser(HttpServletRequest request, HttpServletResponse response, UserBean userBean){

		LogIn logIn = null;

		if(userBean!=null && userBean.isValid()){

			logIn = new LogIn(userBean);

			CookiesWorker.addCookie(request, response, LogIn.class.getSimpleName(), logIn, 24 * 60 * 60);

			request.getSession(true).setAttribute(LogIn.class.getSimpleName(), logIn);

			logsIn.put(logIn.getId(), logIn);
		}

		return logIn;
	}

	public static void addLogIn(HttpServletRequest request, HttpServletResponse response, LogIn logIn) throws GeneralSecurityException, IOException{

		if (logIn != null)
			if (logIn.isValid()) {

				LogIn tmpLogIn = getLogIn(request);

				if (tmpLogIn==null || logIn.getLastAccessTime() > tmpLogIn.getLastAccessTime()) {
					CookiesWorker.addCookie(request, response, LogIn.class.getSimpleName(), logIn, 24 * 60 * 60);

					request.getSession(true).setAttribute( LogIn.class.getSimpleName(), logIn);

					logsIn.put(logIn.getId(), logIn);
				}
			} else
				logIn.setLifeTime(0);
	}

	public static UserBean getUseBean(long userId){
		LogIn logIn = logsIn.get(userId);
		return logIn!=null
				? logIn.getUserBean()
						: null;
	}

	public void remove(long userId){
		logsIn.remove(userId);
	}

	public static UserBean getUserBean(HttpServletRequest request, HttpServletResponse response) throws GeneralSecurityException, IOException {

		UserBean ub;
		LogIn logIn = getLogIn(request);

		if(logIn!=null && logIn.isValid()){
			ub = logIn.getUserBean();
			logIn = UsersLogsIn.addUser(request, response, ub);
			logsIn.put(logIn.getId(), logIn);

			CookiesWorker.addCookie(request, response, LogIn.class.getSimpleName(), logIn.toString(), 24 * 60 * 60);
			request.getSession(false).setAttribute(LogIn.class.getSimpleName(), logIn);
		}else
			ub = new UserBean();

		return ub;
	}

	public static LogIn getLogIn(HttpServletRequest request) throws GeneralSecurityException, IOException {

		HttpSession session = request.getSession(false);
		LogIn logIn = null;
		if(session!=null)
			logIn = (LogIn)session.getAttribute(LogIn.class.getSimpleName());

		if(logIn==null){
			String logInStr = CookiesWorker.getCookieValue(request, LogIn.class.getSimpleName());
			logIn = LogIn.parseLogIn(logInStr);
		}

		return logIn;
	}

	public static void logOff(HttpServletRequest request, HttpServletResponse response, LogIn logIn) {

		HttpSession session = request.getSession(false);
		if(session!=null)
			session.invalidate();

		LogIn li = logsIn.get(logIn.getId());

		if(li==null){
			li = logIn;
			logsIn.put(logIn.getId(), logIn);
		}

		li.setLifeTime(0);

		CookiesWorker.addCookie(request, response, LogIn.class.getSimpleName(), li.toString(), 24 * 60 * 60);
	}

	public static String asString() {
		return logsIn.toString();
	}
}
