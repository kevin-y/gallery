package org.keviny.gallery.amqp;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.keviny.gallery.common.RabbitMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RabbitMessageService {
	@Autowired
	private AmqpTemplate amqpTemplate;
	private final ExecutorService executorService = Executors.newSingleThreadExecutor();
	private static final Logger LOG = LoggerFactory.getLogger(RabbitMessageService.class);

	public void publish(final RabbitMessage message) {
		executorService.execute(new Runnable() {
			@Override
			public void run() {
				if(LOG.isDebugEnabled()) 
					LOG.debug("Publishing message[ Exechange: {}, RoutingKey: {} ]",
								message.getExchange(),  message.getRoutingKey());
				amqpTemplate.convertAndSend(message.getExchange(), message.getRoutingKey(), message.getBody());
			}
		});
	}


}
