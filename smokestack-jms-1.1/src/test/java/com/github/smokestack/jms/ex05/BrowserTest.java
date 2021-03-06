package com.github.smokestack.jms.ex05;

import java.util.Vector;

import javax.jms.JMSException;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import mockit.Expectations;
import mockit.Mocked;
import com.github.smokestack.jms.MockConnectionFactory;
import com.github.smokestack.jms.MockQueue;
import com.github.smokestack.jms.MockQueueBrowser;
import com.github.smokestack.jms.MockTextMessage;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class BrowserTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public final void testMain() throws NamingException, JMSException {
		Context c=new InitialContext();
		final MockConnectionFactory cf=new MockConnectionFactory();
		c.bind("ConnectionFactory", cf);
		c.bind("queue", new MockQueue("queueName"));

		new Expectations(){
			@Mocked( methods= {"_getEnumeration"})
			MockQueueBrowser b;
			{
				Vector<TextMessage> v=new Vector<TextMessage>();
				v.add(new MockTextMessage("queue message 1"));
				v.add(new MockTextMessage("queue message 2"));
				b._getEnumeration(); returns(v.elements());
			}
		};
		
		Browser.main(new String[]{"queue"});
		
		cf.assertMockComplete();
		c.close();
	}

}
