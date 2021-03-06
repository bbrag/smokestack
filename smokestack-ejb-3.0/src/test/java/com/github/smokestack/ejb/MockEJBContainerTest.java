package com.github.smokestack.ejb;

import static org.junit.Assert.*;

import org.hamcrest.core.IsNull;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class MockEJBContainerTest {

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
	public final void testGetSelf() throws InstantiationException, IllegalAccessException {
		MockEJBContainer c=new MockEJBContainer();
		assertThat(c.getInstance(MockEJBContainer.class), IsNull.notNullValue());
	}

}
