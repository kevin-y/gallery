package org.keviny.gallery.common.mail;

import java.util.Properties;

/**
 * Created by kevin on 5/19/15.
 */
public interface MailFactory {
	public Properties getProperties();
	public void setProperties(Properties properties);
	public DefaultAuthenticator getDefaultSmtpAuthenticator();
	public void setDefaultSmtpAuthenticator(DefaultAuthenticator defaultSmtpAuthenticator);
	public DefaultAuthenticator getDefaultPop3Authenticator();
	public void setDefaultPop3Authenticator(DefaultAuthenticator defaultPop3Authenticator);
    public MailSender getMailSender();
    public MailReceiver getMailReceiver();
}
