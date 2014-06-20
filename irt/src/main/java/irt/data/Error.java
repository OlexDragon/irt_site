package irt.data;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;

public class Error {
	private static final Logger LOGGER = (Logger) LogManager.getLogger();

	private String errorMessage;

	public String getErrorMessage() {
		String tmpErr = errorMessage;
		errorMessage = null;

		return LOGGER.exit(tmpErr);
	}

	public void setErrorMessage(String errorMessage) {
		LOGGER.entry(errorMessage);

		if (errorMessage != null && !errorMessage.trim().isEmpty())
			if (this.errorMessage == null)
				this.errorMessage = errorMessage;
			else
				this.errorMessage += "<br />" + errorMessage;
	}

	public boolean isError() {
		return LOGGER.exit(errorMessage != null);
	}

	public void setErrorMessage(String errorMessage, String htmlClassName) {
		LOGGER.entry(errorMessage, htmlClassName);

		setErrorMessage("<span class=\"" + htmlClassName + "\" >"
				+ errorMessage + "</span>");
	}

	public String getStackTrace(Throwable aThrowable) {

		final Writer result = new StringWriter();
		final PrintWriter printWriter = new PrintWriter(result);
		aThrowable.printStackTrace(printWriter);

		return result.toString();
	}
}
