package irt.web.view.workers.component;

import java.util.Objects;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PartNumbers {

	private static final Logger logger = LogManager.getLogger();

	public static String format(String partNumber){
		if(partNumber!=null && !(partNumber = partNumber.trim().toUpperCase()).isEmpty() && partNumber.length()>3){
			StringBuilder sb = new StringBuilder(partNumber);
			int length = partNumber.length();
			if(length>15)
				sb.insert(length-2, '-');
			if(length>10)
				if(partNumber.startsWith("00I") || partNumber.startsWith("TPB") || partNumber.startsWith("TRS"))
					sb.insert(10, '-');
				else if(partNumber.startsWith("0IS") || partNumber.startsWith("0RF"))
					sb.insert(8, '-');
				else
					sb.insert(9, '-');

			sb.insert(3, '-');
			partNumber = sb.toString();
		}
		return logger.exit(partNumber);
	}

	public static String dbFormat(String partNumber) {
		if(partNumber!=null){
			partNumber = partNumber.replaceAll("[\\s-]", "");
			partNumber = partNumber.replaceAll("\\*", "%");
			partNumber = partNumber.replaceAll("\\?", "_");
			partNumber = partNumber.toUpperCase();
		}
		return logger.exit(partNumber);
	}

	public static boolean equals(String partNumber1, String partNumber2) {
		partNumber1 = dbFormat(partNumber1);
		partNumber2 = dbFormat(partNumber2);
		return logger.exit(Objects.equals(partNumber1, partNumber2));
	}
}
