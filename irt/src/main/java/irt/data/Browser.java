package irt.data;

import javax.servlet.http.HttpServletRequest;

public class Browser {

	public enum BrowserId{
		MSIE,
		CHROME,
		FIREFOX
	};

	private BrowserId browserId;

	public BrowserId getBrowserId() {
		return browserId;
	}

	public void setBrowserId(BrowserId browserId) {
		this.browserId =browserId;
	}

	public static Browser getBrowser(HttpServletRequest request) {

		String browserStr = request.getHeader("user-agent");
			Browser browser = new Browser();

			if(browserStr!=null)
				if (browserStr.contains("MSIE"))
					browser.setBrowserId(BrowserId.MSIE);
				else if (browserStr.contains("Chrome"))
					browser.setBrowserId(BrowserId.CHROME);
				else if (browserStr.contains("Firefox"))
					browser.setBrowserId(BrowserId.FIREFOX);
	
			return browser;
	}

	@Override
	public String toString() {
		return "Browser [browserId=" + browserId + "]";
	}
}
