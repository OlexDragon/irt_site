package irt.stock.mail;

public interface MailWorker {

	void sendEmail(String sendFrom, String subject, String text, String... sendTo);
}
