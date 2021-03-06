package irt.data;

import java.util.Comparator;
import java.util.TreeSet;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CookiesWorker {

	private static final Logger logger = LogManager.getLogger();

	public static String getCookieValue(HttpServletRequest request, String startWith) {
		logger.entry(startWith);

		String value = null;

		TreeSet<Cookie> cookiesStartWith = getCookiesStartWith(request, startWith);
		if(!cookiesStartWith.isEmpty())
			value = cookiesStartWith.last().getValue();

		return logger.exit(value);
	}

	public static void addCookie(HttpServletRequest request, HttpServletResponse response, String name, Object value, int cookieMaxAge) {

		removeCookiesStartWith(request, response, name);

		logger.trace("\n\tname\t{},\n\tvalue\t{}",name, value);

		Cookie c = new Cookie(name+System.currentTimeMillis(), value.toString());
		c.setMaxAge(cookieMaxAge);//in seconds
		response.addCookie(c);
	}

	public static void removeCookiesStartWith(HttpServletRequest request, HttpServletResponse response, String startWith) {
		logger.trace("\n\tstartWith\t{}", startWith);

		for(Cookie c:getCookiesStartWith(request, startWith)){
			c.setValue(null);
			c.setMaxAge(0);
			response.addCookie(c);
		}
	}

	public static TreeSet<Cookie> getCookiesStartWith(HttpServletRequest request, String startWith) {
		logger.entry(startWith);

		TreeSet<Cookie> cookiesSet = new TreeSet<Cookie>(new Comparator<Cookie>() {

			@Override
			public int compare(Cookie c1, Cookie c2) {
				return c1.getName().compareTo(c2.getName());
			}
		});

		Cookie[] cookies = request.getCookies();
		if(cookies!=null)
			for(Cookie c:cookies)
				if(c.getName().startsWith(startWith)){
					cookiesSet.add(c);
				}

		return logger.exit(cookiesSet);
	}
}
