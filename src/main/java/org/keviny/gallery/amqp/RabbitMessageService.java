package org.keviny.gallery.amqp;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.keviny.gallery.common.amqp.RabbitMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

@Service
public class RabbitMessageService {
	@Autowired
	private AmqpTemplate amqpTemplate;
	private final ExecutorService executorService = Executors.newSingleThreadExecutor();
	private static final Logger LOG = LoggerFactory.getLogger(RabbitMessageService.class);

	public void publish(final RabbitMessage<?> message) {
		executorService.execute(new Runnable() {
			@Override
			public void run() {
				String exchange = message.getExchange();
				String routingKey = message.getRoutingKey();
				Object body = message.getBody();
				Assert.notNull(exchange);
				Assert.notNull(routingKey);
				Assert.notNull(body);
				
				if(LOG.isDebugEnabled()) 
					LOG.debug("Publishing message[ Exechange: {}, RoutingKey: {} ]",
								message.getExchange(),  message.getRoutingKey());
				amqpTemplate.convertAndSend(exchange, routingKey, body);
			}
		});
	}


}
