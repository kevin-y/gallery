package org.keviny.gallery.common.mail;

import java.io.Serializable;
import java.util.Set;

/**
 * Created by kevin on 5/19/15.
 */
public class MailMessage implements Serializable {
	private static final long serialVersionUID = -3925405674743150223L;

	private Set<String> recipients;
	private String subject;
	private String content;

	public Set<String> getRecipients() {
		return recipients;
	}

	public void setRecipients(Set<String> recipients) {
		this.recipients = recipients;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
}
