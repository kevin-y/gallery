package org.keviny.gallery.common;

import java.util.HashMap;
import java.util.Map;

public class RabbitMessage {
	private String exchange;
	private String routingKey;
	private Map<String, Object> body;

	public RabbitMessage() {
		this.body = new HashMap<String, Object>();
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

	public Map<String, Object> getBody() {
		return body;
	}

	public void put(String key, Object value) {
		body.put(key, value);
	}
}
