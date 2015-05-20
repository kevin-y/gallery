package org.keviny.gallery.common.mail;

import java.io.IOException;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

import javax.mail.*;
import javax.mail.internet.MimeBodyPart;

public class DefaultMailStore implements MailStore {

	private ConcurrentHashMap<String, MailMessageHandler> handlers = new ConcurrentHashMap<String, MailMessageHandler>();
	
	@Override
	public boolean isUnread(String uid) {
		return true;
	}

	@Override
	public void store(Message message) {
        System.out.println("-------------------------------------------");
        try {
            Date sentDate = message.getSentDate();
            String contentType = message.getContentType();

            if(contentType.contains("multipart")) {
                Multipart multipart = (Multipart)message.getContent();
                int numOfParts = multipart.getCount();
                for(int i = 0; i < numOfParts; i++) {
                    MimeBodyPart part = (MimeBodyPart)multipart.getBodyPart(i);
                    String disposition = part.getDisposition();
                    if(Part.ATTACHMENT.equalsIgnoreCase(disposition)) {
                        String filename = part.getFileName();
                        System.out.println("Attachment: " + filename);
                    } else if(Part.INLINE.equalsIgnoreCase(disposition)) {
                        String filename = part.getFileName();
                        System.out.println("Inline: " +  filename);
                    } else {
                        System.out.println("Content: " + part.getContent());
                    }
                }
            } else {
                System.out.println(message.getContent());
            }

        System.out.println();
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

	@Override
	public void addHandler(String mimeType, MailMessageHandler handler) {
		handlers.putIfAbsent(mimeType, handler);
	}
	
	
}
