package com.github.smokestack.jdbc.ex01;

import mockit.Expectations;
import mockit.Mocked;
import com.github.smokestack.jdbc.MockConnection;
import com.github.smokestack.jdbc.MockDriver;
import com.github.smokestack.jdbc.MockResultSet;
import com.github.smokestack.jdbc.MockStatement;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class HelloWorldTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		MockDriver.instance.reset();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public final void testSaveHello() throws Exception {
		new Expectations(){
			@Mocked( methods= {"_execute"})
			MockStatement st;
			@Mocked( methods= {"_next", "_getString"})			
			MockResultSet rs;
			{
				st._execute((String)any);
				st._execute((String)any);
				rs._next(); returns(true);
				rs._getString("message"); returns("hello");
				rs._next(); returns(false);
			}
		};
		Class.forName("com.github.smokestack.jdbc.MockDriver");	
		new HelloWorld().saveHello();
		// there is no easy way to get to the Connection ...
		MockConnection c=MockDriver.instance.getMockConnections().get(0);
		c.assertExplicitClose();
	}

	@Test
	public final void testSaveHelloAgain() throws Exception {
		new Expectations(){
			@Mocked( methods= {"_execute"})
			MockStatement st;
			@Mocked( methods= {"_next", "_getString"})			
			MockResultSet rs;
			{
				st._execute((String)any);
				st._execute((String)any);
				rs._next(); returns(true);
				rs._getString("message"); returns("hello");
				rs._next(); returns(false);
			}
		};
		Class.forName("com.github.smokestack.jdbc.MockDriver");	
		new HelloWorld().saveHelloAgain();
		// there is no easy way to get to the Connection ...
		MockConnection c=MockDriver.instance.getMockConnections().get(0);
		c.assertClosed();
	}
	
}
