package org.keviny.gallery.common.mail;

import java.util.Properties;

import javax.mail.FetchProfile;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.UIDFolder;

import com.sun.mail.pop3.POP3Folder;

/**
 * Created by kevin on 5/19/15.
 */
public class SimpleMailReceiver implements MailReceiver {


    private static final String MAIL_POP3_HOST = "mail.pop3.host";
    private static final String MAIL_POP3_STARTTTLS_ENABLE = "mail.pop3.starttls.enable";
    private static final String MAIL_POP3_AUTH = "mail.pop3.auth";
    private static final String MAIL_POP3_PORT = "mail.pop3.port";
    private static final String MAIL_POP3_SOCKET_FACTORY_PORT = "mail.pop3.socketFactory.port";
    private static final String MAIL_POP3_SOCKET_FACTORY_CLASS = "mail.pop3.socketFactory.class";

    private static final String DELFAULT_MAIL_POP3_HOST = "localhost";
    private static final String DEFAULT_MAIL_POP3_STARTTTLS_ENABLE = "true";
    private static final String DEFAULT_MAIL_POP3_AUTH = "true";
    private static final String DEFAULT_MAIL_POP3_PORT = "995";
    private static final String DEFAULT_MAIL_POP3_SOCKET_FACTORY_PORT = "995";
    private static final String DELAULT_MAIL_POP3_SOCKET_FACTORY_CLASS = "javax.net.ssl.SSLSocketFactory";

    private Properties pop3Properties;
    private DefaultAuthenticator defaultAuthenticator;
    private MailStore mailStore;

    public SimpleMailReceiver() {
    	
    }
    
    public SimpleMailReceiver(Properties pop3Properties, DefaultAuthenticator defaultAuthenticator) {
    	this.pop3Properties = pop3Properties;
    	this.defaultAuthenticator = defaultAuthenticator;
    }
    
    public Properties getPop3Properties() {
        return pop3Properties;
    }

    public void setPop3Properties(Properties pop3Properties) {
        this.pop3Properties = pop3Properties;
    }

    public DefaultAuthenticator getDefaultAuthenticator() {
        return defaultAuthenticator;
    }

    public void setDefaultAuthenticator(DefaultAuthenticator defaultAuthenticator) {
        this.defaultAuthenticator = defaultAuthenticator;
    }

    @Override
    public void receive() {
        // reduce manual configurations and mistakes
        Properties props = new Properties();
        props.setProperty(MAIL_POP3_HOST, pop3Properties.getProperty(MAIL_POP3_HOST, DELFAULT_MAIL_POP3_HOST));
        props.setProperty(MAIL_POP3_STARTTTLS_ENABLE, pop3Properties.getProperty(MAIL_POP3_STARTTTLS_ENABLE, DEFAULT_MAIL_POP3_STARTTTLS_ENABLE));
        props.setProperty(MAIL_POP3_AUTH, pop3Properties.getProperty(MAIL_POP3_AUTH, DEFAULT_MAIL_POP3_AUTH));
        props.setProperty(MAIL_POP3_PORT, pop3Properties.getProperty(MAIL_POP3_PORT, DEFAULT_MAIL_POP3_PORT));
        props.setProperty(MAIL_POP3_SOCKET_FACTORY_PORT, pop3Properties.getProperty(MAIL_POP3_SOCKET_FACTORY_PORT, DEFAULT_MAIL_POP3_SOCKET_FACTORY_PORT));
        props.setProperty(MAIL_POP3_SOCKET_FACTORY_CLASS, pop3Properties.getProperty(MAIL_POP3_SOCKET_FACTORY_CLASS, DELAULT_MAIL_POP3_SOCKET_FACTORY_CLASS));

        Session session = Session.getInstance(props, defaultAuthenticator);

        try {
            Store store = session.getStore("pop3s");
            store.connect(props.getProperty(MAIL_POP3_HOST), defaultAuthenticator.getUsername(), defaultAuthenticator.getPassword());
            //create the folder object and open it
            POP3Folder inbox = (POP3Folder)store.getFolder("INBOX");
            
            inbox.open(Folder.READ_ONLY);
            FetchProfile profile = new FetchProfile();
            profile.add(UIDFolder.FetchProfileItem.UID);
            Message[] messages = inbox.getMessages();
            inbox.fetch(messages,profile);
            mailStore = new DefaultMailStore();
            for (int i = 0, n = messages.length; i < n; i++) {
                String uid = inbox.getUID(messages[i]);
                if(mailStore.isUnread(uid)) {
                	// save mail to db/file
                	Message message = inbox.getMessage(i + 1);
                	mailStore.store(message);
                }

            }

            //close the store and folder objects
            inbox.close(false);
            store.close();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        } 
    }
}
