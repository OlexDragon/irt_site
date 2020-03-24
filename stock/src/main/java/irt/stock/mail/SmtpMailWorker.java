package irt.stock.mail;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import javax.mail.MessagingException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import irt.stock.data.jpa.beans.User;
import irt.stock.data.jpa.beans.engineering.EngineeringChange;
import irt.stock.data.jpa.beans.engineering.EngineeringChangeStatus;
import irt.stock.data.jpa.beans.engineering.StatusOf;
import irt.stock.data.jpa.beans.engineering.eco.Eco;
import irt.stock.data.jpa.beans.engineering.eco.EcoStatus;
import irt.stock.data.jpa.beans.engineering.ecr.EcrStatus;
import irt.stock.data.jpa.repositories.UserRepository;
import irt.stock.data.jpa.services.UserRoles;

@Service
public class SmtpMailWorker implements MailWorker {
	private final static Logger logger = LogManager.getLogger();

	@Value("${irt.mail.subject.length}") private int subjectLength;
	@Value("${spring.mail.username}")	 private String mailUserName;

	@Autowired private JavaMailSender javaMailSender;
	@Autowired private UserRepository userRepository;
	@Autowired private TemplateEngine templateEngine;

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

	@Override
	public void sendEcr(EngineeringChange ec, URL url) {

		EngineeringChangeStatus lastStatus = ec.getLastStatus();
		StatusOf status = lastStatus.getStatus();
		logger.error(status);

		if(status == EcrStatus.Status.CREATED || status == EcoStatus.Status.CREATED)
			sendCreated(lastStatus, ec, url);

		else if(status == EcrStatus.Status.FORWARDED)
			sendForward(lastStatus, ec, url);

		else if(status == EcrStatus.Status.REJECTED)
			sendReject(ec, url);

		else throw new RuntimeException("Have to add action for " + status);
	}

	private void sendReject(EngineeringChange ecr, URL url) {

		List<EngineeringChangeStatus> ecrStatus = ecr.getStatus();
		User changedBy = ecr.getLastStatus().getChangedBy();
//		logger.error(ecrStatus);

		//Send TO
		// Get the penultimate user who changed status. 
		User toUser = (ecrStatus.size() > 1 ? ecrStatus.get(1) : ecrStatus.get(0)).getChangedBy();

		List<User> toUsers = new ArrayList<User>();
		toUsers.add(toUser);

		//Send CC
		List<User> ccUsers = ecrStatus.parallelStream().map(EngineeringChangeStatus::getChangedBy).collect(Collectors.toList());

		//Send BCC
		List<User> bccUsers = userRepository.findByPermission(UserRoles.ENGINEERING_BCC.getPermission());

		sendMessage(toUsers, ccUsers, bccUsers, getSubject(ecr), getEmailText(changedBy, ecr, url));
	}

	private void sendCreated(EngineeringChangeStatus lastStatus, EngineeringChange ec, URL url) {

		if(!ecoHasContents(ec))
			return;

		List<User> toUsers = userRepository.findByPermission(UserRoles.ENGINEERING_TOP.getPermission());
		List<User> bccUsers = userRepository.findByPermission(UserRoles.ENGINEERING_BCC.getPermission());
		List<User> ccUsers = new ArrayList<>();

		User createdBy = lastStatus.getChangedBy();
		ccUsers.add(createdBy);

		String subject = getSubject(ec);

		sendMessage(toUsers, ccUsers, bccUsers, subject, getEmailText(createdBy, ec, url));
	}

	private boolean ecoHasContents(EngineeringChange ec) {

		if(ec instanceof Eco) 
			return Optional.of(ec).map(Eco.class::cast).map(Eco::getDescription).map(description->description != null).orElse(false);
		
		return false;
	}

	private void sendForward(EngineeringChangeStatus lastStatus, EngineeringChange ecr, URL url) {

		List<User> toUsers = new ArrayList<User>();
		toUsers.add(lastStatus.getForwardTo().getUser());

		List<User> bccUsers = userRepository.findByPermission(UserRoles.ENGINEERING_BCC.getPermission());

		List<User> ccUsers = new ArrayList<>();

		User createdBy = lastStatus.getChangedBy();
		ccUsers.add(createdBy);

		String subject = getSubject(ecr);

		sendMessage(toUsers, ccUsers, bccUsers, subject, getEmailText(createdBy, ecr, url));
	}

	private void sendMessage(Iterable<User> toUser, Iterable<User> ccUser, Iterable<User> bccUser, String subject, String text) {

		MimeMessagePreparator messagePreparator = mimeMessage -> {
	        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
	        messageHelper.setFrom(mailUserName);

	        // set TO
	        StreamSupport.stream(toUser.spliterator(), false)
	        .map(User::getEmail)
	        .forEach(
	        		email -> {
	        			try {

	        				messageHelper.addTo(email);

	        			} catch (MessagingException e) {
	        				logger.catching(e);
	        			}
	        		});

	        // set CC
	        StreamSupport.stream(ccUser.spliterator(), false)
	        .filter(user->!StreamSupport.stream(toUser.spliterator(), true).anyMatch(u->u.equals(user)))
	        .map(User::getEmail)
	        .forEach(
	        		email -> {
	        			try {

	        				messageHelper.addCc(email);

	        			} catch (MessagingException e) {
	        				logger.catching(e);
	        			}
	        		});

	        // set BCC
	        StreamSupport.stream(bccUser.spliterator(), false)
	        .filter(user->!StreamSupport.stream(toUser.spliterator(), true).anyMatch(u->u.equals(user)))
	        .filter(user->!StreamSupport.stream(ccUser.spliterator(), true).anyMatch(u->u.equals(user)))
	        .map(User::getEmail)
	        .forEach(
	        		email -> {
	        			try {

	        				messageHelper.addBcc(email);

	        			} catch (MessagingException e) {
	        				logger.catching(e);
	        			}
	        		});

	        messageHelper.setSubject(subject);
			messageHelper.setText(text, true);
		};

		javaMailSender.send(messagePreparator);
	}

	private String getSubject(EngineeringChange engineeringChange) {

		StatusOf status = engineeringChange.getLastStatus().getStatus();
		String simpleName = engineeringChange.getClass().getSimpleName().toUpperCase();
		long number = engineeringChange.getNumber();
		String reason = engineeringChange.getReason();

		String subject = status + ": " + simpleName + " #" + number + "; ";
		int maxLength = subjectLength - subject.length();
		return subject + Optional.of(reason).filter(r->r.length()>maxLength).map(r->r.substring(0, maxLength-3) + "...").orElse(reason);
	}

	private String getEmailText(User user, EngineeringChange ec, URL url) {
		Context context = new Context();
		context.setVariable("engineeringChange"	, ec);
		context.setVariable("user"	, user);
		context.setVariable("url"	, url);
		return templateEngine.process("engineering/eco/eco_email_template", context);
	}
}
