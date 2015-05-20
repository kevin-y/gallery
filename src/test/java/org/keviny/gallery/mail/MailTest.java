package org.keviny.gallery.mail;

import java.util.Properties;

import org.junit.Before;
import org.junit.Test;
import org.keviny.gallery.common.mail.DefaultAuthenticator;
import org.keviny.gallery.common.mail.MailFactory;
import org.keviny.gallery.common.mail.MailMessage;
import org.keviny.gallery.common.mail.MailReceiver;
import org.keviny.gallery.common.mail.MailSender;
import org.keviny.gallery.common.mail.SimpleMailFactory;

public class MailTest {
	private MailFactory mailFactory;

	@Before
	public void init() {
		mailFactory = new SimpleMailFactory();
		mailFactory.setDefaultAuthenticator(new DefaultAuthenticator("test@example.com", "password"));
		Properties props = new Properties();
		// SMTP
		props.setProperty("mail.smtp.auth", "true");
		props.setProperty("mail.smtp.starttls.enable", "true");
		props.setProperty("mail.smtp.host", "smtpHost");
		props.setProperty("mail.smtp.port", "465");
		props.put("mail.smtp.socketFactory.port", "465");
		props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		
		// POP3
		props.put("mail.pop3.host", "smtpHost");
		props.put("mail.pop3.port", "995");
		props.put("mail.pop3.starttls.enable", "true");
		props.put("mail.pop3.socketFactory.port", "995");
        props.put("mail.pop3.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        mailFactory.setProperties(props);
	}

	@Test
	public void testMailSender() {
		MailSender mailSender = mailFactory.getMailSender();
		MailMessage mm = new MailMessage();
		mm.setRecipient("test@example.com");
		mm.setSubject("Important meetings");
		mm.setContent("This is a test message.");
		mailSender.send(mm);
	}
	
	@Test
	public void testMailReceiver() {
		MailReceiver mailReceiver = mailFactory.getMailReceiver();
		mailReceiver.receive();		
	}
}
