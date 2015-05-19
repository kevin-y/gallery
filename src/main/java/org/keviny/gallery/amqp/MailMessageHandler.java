package org.keviny.gallery.amqp;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

/**
 * Created by kevin on 5/19/15.
 */
@Component
public class MailMessageHandler implements MessageListener {
    @Autowired
    private ObjectMapper objectMapper;
    @Override
    public void onMessage(Message message) {
        MessageProperties mProps = message.getMessageProperties();
        try {
            Map<?, ?> m = objectMapper.readValue(message.getBody(), Map.class);
            for(Object key : m.keySet()) {
                System.out.println(key + "=>" + m.get(key));
            }
        } catch (IOException e) {
        }
    }
}
