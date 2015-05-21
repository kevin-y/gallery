package org.keviny.gallery.mail;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.keviny.gallery.amqp.RabbitMessageService;
import org.keviny.gallery.common.amqp.RabbitMessage;
import org.keviny.gallery.common.mail.MailFactory;
import org.keviny.gallery.common.mail.MailMessage;
import org.keviny.gallery.common.mail.MailReceiver;
import org.keviny.gallery.common.mail.MailSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:mvc-config.xml"})
public class MailTest {
	@Autowired
	private MailFactory mailFactory;
	@Autowired
	private RabbitMessageService rabbitMessageService;
	
	
	@Test
	public void testMailSender() {
		MailSender mailSender = mailFactory.getMailSender();
		MailMessage mm = new MailMessage();
		Set<String> recipients = new HashSet<String>();
		recipients.add("kings988@163.com");
		recipients.add("craaazy123@163.com");
		mm.setRecipients(recipients);
		mm.setSubject("Important meetings");
		mm.setContent("Everyone please attend on time!!!");
		mailSender.send(mm);
	}
	
	@Test
	public void testMailReceiver() {
		MailReceiver mailReceiver = mailFactory.getMailReceiver();
		mailReceiver.receive();		
	}
	
	@Test
	public void testAsynMailSending() {
		RabbitMessage<MailMessage> message = new RabbitMessage<MailMessage>();
		MailMessage mm = new MailMessage();
		Set<String> recipients = new HashSet<String>();
		recipients.add("kings988@163.com");
		recipients.add("craaazy123@163.com");
		mm.setRecipients(recipients);
		mm.setSubject("Kevin's github repository");
		String content = "Bellow's the link:<br>"
					   + "<a href=\"https://github.com/kevin-y/gallery\">Kevin's github repository</a><br>"
					   + "Sample image:<br>"
					   + "<img src=\"http://www.ipaddesk.com/uploadfile/2013/0118/20130118104739919.jpg\" title=\"Architecture\" alt=\"Architecture\">";
		mm.setContent(content);
		message.setExchange("gallery.mail");
		message.setRoutingKey("gallery.mail.send");
		message.setBody(mm);
		rabbitMessageService.publish(message);
	}
}
