package org.keviny.gallery.amqp;

import org.keviny.gallery.common.RabbitMessage;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class RabbitMessageService {
	@Autowired
	private AmqpTemplate amqpTemplate;
	private ExecutorService executorService = Executors.newSingleThreadExecutor();

	public void publish(final RabbitMessage message) {
		executorService.execute(new Runnable() {
			@Override
			public void run() {
				amqpTemplate.convertAndSend(message.getExchange(), message.getRoutingKey(), message.getBody());
			}
		});
	}


}
