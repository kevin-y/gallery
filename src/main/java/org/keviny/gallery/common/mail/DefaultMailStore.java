package org.keviny.gallery.common.mail;

import java.util.concurrent.ConcurrentHashMap;

import javax.mail.Message;
import javax.mail.MessagingException;

public class DefaultMailStore implements MailStore {

	private ConcurrentHashMap<String, MailMessageHandler> handlers = new ConcurrentHashMap<String, MailMessageHandler>();
	
	@Override
	public boolean isUnread(String uid) {
		return true;
	}

	@Override
	public void store(Message message) {
		try {
			String mimeType = message.getContentType();
			System.out.println(mimeType);
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void addHandler(String mimeType, MailMessageHandler handler) {
		handlers.putIfAbsent(mimeType, handler);
	}
	
	
}
