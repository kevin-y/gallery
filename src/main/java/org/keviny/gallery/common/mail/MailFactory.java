package org.keviny.gallery.common.mail;

import java.util.Properties;

/**
 * Created by kevin on 5/19/15.
 */
public interface MailFactory {
	public Properties getProperties();
	public void setProperties(Properties props);
	public DefaultAuthenticator getDefaultAuthenticator();
	public void setDefaultAuthenticator(DefaultAuthenticator defaultAuthenticator);
    public MailSender getMailSender();
    public MailReceiver getMailReceiver();
}
