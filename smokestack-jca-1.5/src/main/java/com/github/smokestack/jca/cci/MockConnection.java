package com.github.smokestack.jca.cci;

import static org.hamcrest.MatcherAssert.assertThat;

import javax.resource.ResourceException;
import javax.resource.cci.Connection;
import javax.resource.cci.ConnectionMetaData;
import javax.resource.cci.ConnectionSpec;
import javax.resource.cci.Interaction;
import javax.resource.cci.LocalTransaction;
import javax.resource.cci.ResultSetInfo;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.hamcrest.core.Is;

public class MockConnection implements Connection {

	public enum ConnectionState {NEW, CLOSE};
	
	protected ConnectionState connectionState=ConnectionState.NEW;
	
	protected MockInteraction mockInteraction;
	protected MockLocalTransaction mockTransaction;
	protected ConnectionSpec connectionSpec;
	protected MockResultSetInfo mockResultSetInfo;
	protected MockConnectionMetaData mockConnectionMetaData;
	
	public MockConnection(ConnectionSpec connectionSpec) {
		this.connectionSpec=connectionSpec;
	}

	public MockConnection() {
	}

	/* (non-Javadoc)
	 * @see javax.resource.cci.Connection#close()
	 */
	public void close() throws ResourceException {
		assertThat("connectionState", connectionState, Is.is(ConnectionState.NEW));
		connectionState=ConnectionState.CLOSE;
		_close();
	}

	public void _close() throws ResourceException {
	}

	/* (non-Javadoc)
	 * @see javax.resource.cci.Connection#createInteraction()
	 */
	public Interaction createInteraction() throws ResourceException {
		assertThat("connectionState", connectionState, Is.is(ConnectionState.NEW));
		_createInteraction();
		if (mockInteraction==null){
			mockInteraction=new MockInteraction(this);
		}
		return mockInteraction;
	}

	public MockInteraction _createInteraction() throws ResourceException {
		return null;
	}

	/* (non-Javadoc)
	 * @see javax.resource.cci.Connection#getLocalTransaction()
	 */
	public LocalTransaction getLocalTransaction() throws ResourceException {
		assertThat("connectionState", connectionState, Is.is(ConnectionState.NEW));
		_getLocalTransaction();
		if (mockTransaction==null){
			mockTransaction=new MockLocalTransaction();
		}
		return mockTransaction;
	}

	public MockLocalTransaction _getLocalTransaction() throws ResourceException {
		return null;
	}

	/* (non-Javadoc)
	 * @see javax.resource.cci.Connection#getMetaData()
	 */
	public ConnectionMetaData getMetaData() throws ResourceException {
		assertThat("connectionState", connectionState, Is.is(ConnectionState.NEW));
		_getMetaData();
		if (mockConnectionMetaData==null){
			mockConnectionMetaData=new MockConnectionMetaData();
		}
		return mockConnectionMetaData;
	}

	public MockConnectionMetaData _getMetaData() throws ResourceException {
		return null;
	}

	/* (non-Javadoc)
	 * @see javax.resource.cci.Connection#getResultSetInfo()
	 */
	public ResultSetInfo getResultSetInfo() throws ResourceException {
		assertThat("connectionState", connectionState, Is.is(ConnectionState.NEW));
		_getResultSetInfo();
		if (mockResultSetInfo==null){
			mockResultSetInfo=new MockResultSetInfo();
		}
		return mockResultSetInfo;
	}
	
	public MockResultSetInfo _getResultSetInfo() throws ResourceException {
		return null;
	}
	
	/**
	 * Utility method that should be used to ensure that the 
	 * connection and all its dependent transactions and interactions
	 * are completed. 
	 * This assertion ensures that the mock container is in a good state
	 */
	public void assertMockComplete(){
		assertThat("connectionState", connectionState, Is.is(ConnectionState.CLOSE));
		if (mockTransaction!=null){
			mockTransaction.assertMockComplete();			
		}
		if (mockInteraction!=null){
			mockInteraction.assertMockComplete();			
		}		
	}
	
	/**
	 * Utility method that should be used to ensure that the 
	 * all the dependent transactions and interactions
	 * are committed. 
	 * This assertion would be invoked to verify that the 
	 * transactions and interactions were committed. 
	 */
	public void assertMockCommit(){
		assertThat("connectionState", connectionState, Is.is(ConnectionState.CLOSE));
		if (mockTransaction!=null){
			mockTransaction.assertMockCommit();			
		}
		if (mockInteraction!=null){
			mockInteraction.assertMockComplete();			
		}		
	}
	
	/**
	 * Utility method that should be used to ensure that the 
	 * all the dependent transactions and interactions
	 * are in a rollback state.
	 * This assertion would be invoked to verify that the 
	 * transaction rolledback as expected
	 */
	public void assertMockRollback(){
		assertThat("connectionState", connectionState, Is.is(ConnectionState.CLOSE));
		if (mockTransaction!=null){
			mockTransaction.validateMockRollback();			
		}
		if (mockInteraction!=null){
			mockInteraction.assertMockComplete();			
		}		
	}
	
	@Override
	public String toString(){
		return ReflectionToStringBuilder.toString(this, ToStringStyle.MULTI_LINE_STYLE);
	}
}
