package org.keviny.gallery.amqp;

import java.io.IOException;

import org.keviny.gallery.common.mail.MailFactory;
import org.keviny.gallery.common.mail.MailMessage;
import org.keviny.gallery.common.mail.MailSender;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Created by kevin on 5/19/15.
 */
@Component
public class MailMessageHandler implements MessageListener {
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MailFactory mailFactory;
    
    @Override
    public void onMessage(Message message) {
        try {
           MailMessage mail = objectMapper.readValue(message.getBody(), MailMessage.class);
           // simply ignore the message
           if(mail.getRecipients().isEmpty()) return;           
           MailSender sender = mailFactory.getMailSender();
           sender.send(mail);
        } catch (IOException e) {
        }
    }
}
