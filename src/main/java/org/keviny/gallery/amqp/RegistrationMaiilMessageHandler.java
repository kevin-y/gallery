package org.keviny.gallery.amqp;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.keviny.gallery.common.mail.MailFactory;
import org.keviny.gallery.common.mail.MailSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

/**
 * Created by kevin on 6/6/15.
 */
public class RegistrationMaiilMessageHandler implements MessageListener {

    private static final Logger LOG = LoggerFactory.getLogger(MailMessageHandler.class);

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MailFactory mailFactory;

    @Override
    public void onMessage(Message message) {
        try {
            // FIXME
            objectMapper.readValue(message.getBody(), Class.class);
            MailSender sender = mailFactory.getMailSender();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
