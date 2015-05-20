package org.keviny.gallery.common.mail;

import java.util.Properties;

public class SimpleMailFactory implements MailFactory {

	private Properties props;
	private DefaultAuthenticator defaultAuthenticator;

	public Properties getProperties() {
		return props;
	}

	public void setProperties(Properties props) {
		this.props = props;
	}

	public DefaultAuthenticator getDefaultAuthenticator() {
		return defaultAuthenticator;
	}

	public void setDefaultAuthenticator(DefaultAuthenticator defaultAuthenticator) {
		this.defaultAuthenticator = defaultAuthenticator;
	}

	public MailSender getMailSender() {
		SimpleMailSender mailSender = new SimpleMailSender();
		mailSender.setDefaultAuthenticator(defaultAuthenticator);
		mailSender.setSmtpProperties(props);
		return mailSender;
	}

	public MailReceiver getMailReceiver() {
		SimpleMailReceiver mailReceiver = new SimpleMailReceiver();
		mailReceiver.setDefaultAuthenticator(defaultAuthenticator);
		mailReceiver.setPop3Properties(props);
		return mailReceiver;
	}

}
