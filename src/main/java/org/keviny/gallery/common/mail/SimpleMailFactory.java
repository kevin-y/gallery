package org.keviny.gallery.common.mail;

import java.util.Properties;

public class SimpleMailFactory implements MailFactory {

	private Properties properties;
	private DefaultAuthenticator defaultSmtpAuthenticator;
	private DefaultAuthenticator defaultPop3Authenticator;

	public Properties getProperties() {
		return properties;
	}

	public void setProperties(Properties properties) {
		this.properties = properties;
	}

	public DefaultAuthenticator getDefaultSmtpAuthenticator() {
		return defaultSmtpAuthenticator;
	}

	public void setDefaultSmtpAuthenticator(
			DefaultAuthenticator defaultSmtpAuthenticator) {
		this.defaultSmtpAuthenticator = defaultSmtpAuthenticator;
	}

	public DefaultAuthenticator getDefaultPop3Authenticator() {
		return defaultPop3Authenticator;
	}

	public void setDefaultPop3Authenticator(
			DefaultAuthenticator defaultPop3Authenticator) {
		this.defaultPop3Authenticator = defaultPop3Authenticator;
	}

	public MailSender getMailSender() {
		SimpleMailSender mailSender = new SimpleMailSender();
		mailSender.setDefaultAuthenticator(defaultSmtpAuthenticator);
		mailSender.setSmtpProperties(properties);
		return mailSender;
	}

	public MailReceiver getMailReceiver() {
		SimpleMailReceiver mailReceiver = new SimpleMailReceiver();
		mailReceiver.setDefaultAuthenticator(defaultPop3Authenticator);
		mailReceiver.setPop3Properties(properties);
		return mailReceiver;
	}

}
