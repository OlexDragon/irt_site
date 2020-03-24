package irt.stock.mail;

import java.net.URL;

import irt.stock.data.jpa.beans.engineering.EngineeringChange;

public interface MailWorker {

	void sendEmail(String sendFrom, String subject, String text, String... sendTo);
	void sendEcr(EngineeringChange engineeringChange, URL url);
}
