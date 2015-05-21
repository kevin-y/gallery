package org.keviny.gallery.common.amqp;


public class RabbitMessage<T> {
	private String exchange;
	private String routingKey;
	private T body;

	public RabbitMessage() {
	}

	public String getExchange() {
		return exchange;
	}

	public void setExchange(String exchange) {
		this.exchange = exchange;
	}

	public String getRoutingKey() {
		return routingKey;
	}

	public void setRoutingKey(String routingKey) {
		this.routingKey = routingKey;
	}

	public T getBody() {
		return body;
	}

	public void setBody(T body) {
		this.body = body;
	}
}
