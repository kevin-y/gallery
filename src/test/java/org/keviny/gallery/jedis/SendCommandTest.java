package org.keviny.gallery.jedis;

import java.lang.reflect.Method;

import org.junit.Test;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;

import redis.clients.jedis.Connection;
import redis.clients.jedis.Protocol.Command;
//import redis.clients.jedis.ProtocolCommand;

public class SendCommandTest {
	/*@Test
	public void testSendCommand1() {
		Method SEND_COMMAND = ReflectionUtils.findMethod(Connection.class,
				"sendCommand", new Class[] { Command.class, byte[][].class});
		Assert.notNull(SEND_COMMAND);
	}
	
	@Test
	public void testSendCommand2() {
		Method SEND_COMMAND = ReflectionUtils.findMethod(Connection.class, 
				"sendCommand", new Class[] { ProtocolCommand.class, byte[][].class});
		Assert.notNull(SEND_COMMAND);
	}*/
}
