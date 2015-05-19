package org.keviny.gallery.common.mail;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

/**
 * Created by kevin on 5/19/15.
 */
public class SimpleMailSender implements Sender {


    private static final String MAIL_SMTP_HOST = "mail.smtp.host";
    private static final String MAIL_SMTP_STARTTTLS_ENABLE = "mail.smtp.starttls.enable";
    private static final String MAIL_SMTP_AUTH = "mail.smtp.auth";
    private static final String MAIL_SMTP_PORT = "mail.smtp.port";
    private static final String MAIL_SMTP_SOCKET_FACTORY_PORT = "mail.smtp.socketFactory.port";
    private static final String MAIL_SMTP_SOCKET_FACTORY_CLASS = "mail.smtp.socketFactory.class";

    private static final String DELFAULT_MAIL_SMTP_HOST = "localhost";
    private static final String DEFAULT_MAIL_SMTP_STARTTTLS_ENABLE = "true";
    private static final String DEFAULT_MAIL_SMTP_AUTH = "true";
    private static final String DEFAULT_MAIL_SMTP_PORT = "465";
    private static final String DEFAULT_MAIL_SMTP_SOCKET_FACTORY_PORT = "465";
    private static final String DELAULT_MAIL_SMTP_SOCKET_FACTORY_CLASS = "javax.net.ssl.SSLSocketFactory";

    private Properties smtpProperties;
    private DefaultAuthenticator defaultAuthenticator;

    public Properties getSmtpProperties() {
        return smtpProperties;
    }

    public void setSmtpProperties(Properties smtpProperties) {
        this.smtpProperties = smtpProperties;
    }

    public DefaultAuthenticator getDefaultAuthenticator() {
        return defaultAuthenticator;
    }

    public void setDefaultAuthenticator(DefaultAuthenticator defaultAuthenticator) {
        this.defaultAuthenticator = defaultAuthenticator;
    }

    @Override
    public void send(final MailMessage mailMessage) {
        // reduce manual configurations and mistakes
        Properties props = new Properties();
        props.setProperty(MAIL_SMTP_HOST, smtpProperties.getProperty(MAIL_SMTP_HOST, DELFAULT_MAIL_SMTP_HOST));
        props.setProperty(MAIL_SMTP_STARTTTLS_ENABLE, smtpProperties.getProperty(MAIL_SMTP_STARTTTLS_ENABLE, DEFAULT_MAIL_SMTP_STARTTTLS_ENABLE));
        props.setProperty(MAIL_SMTP_AUTH, smtpProperties.getProperty(MAIL_SMTP_AUTH, DEFAULT_MAIL_SMTP_AUTH));
        props.setProperty(MAIL_SMTP_PORT, smtpProperties.getProperty(MAIL_SMTP_PORT, DEFAULT_MAIL_SMTP_PORT));
        props.setProperty(MAIL_SMTP_SOCKET_FACTORY_PORT, smtpProperties.getProperty(MAIL_SMTP_SOCKET_FACTORY_PORT, DEFAULT_MAIL_SMTP_SOCKET_FACTORY_PORT));
        props.setProperty(MAIL_SMTP_SOCKET_FACTORY_CLASS, smtpProperties.getProperty(MAIL_SMTP_SOCKET_FACTORY_CLASS, DELAULT_MAIL_SMTP_SOCKET_FACTORY_CLASS));

        Session session = Session.getInstance(props, defaultAuthenticator);
        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(defaultAuthenticator.getUsername()));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(mailMessage.getRecipient()));
            message.setSubject(mailMessage.getSubject());
            message.setContent(mailMessage.getContent(), "text/html");
            Transport.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
