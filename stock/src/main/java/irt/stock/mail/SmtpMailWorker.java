package irt.stock.mail;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

@Service
public class SmtpMailWorker implements MailWorker {

	@Autowired private JavaMailSender javaMailSender;

	@Override
	public void sendEmail(String from, String subject, String text, String... to) {

		MimeMessagePreparator messagePreparator = mimeMessage -> {
	        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
	        messageHelper.setFrom(from);
	        messageHelper.setCc(from);
	        messageHelper.setTo(to);
	        messageHelper.setSubject(subject);
	        messageHelper.setText(text, true);
		};

		javaMailSender.send(messagePreparator);
	}

}
