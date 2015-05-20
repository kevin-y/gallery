package org.keviny.gallery.common.mail;

import javax.mail.Message;

public interface MailStore {
	public boolean isUnread(String uid);
	public void store(Message message);
	public void addHandler(String mimeType, MailMessageHandler handler);
}
