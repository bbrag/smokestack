/**
 * 
 */
package net.sourceforge.smokestack.jdbc;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Savepoint;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.hamcrest.core.AnyOf;
import org.hamcrest.core.Is;
import org.hamcrest.core.IsNot;

import mockit.external.hamcrest.core.IsNull;
import net.sourceforge.smokestack.exception.NeedsMockDefinitionException;
import net.sourceforge.smokestack.exception.NotYetImplementedException;
import net.sourceforge.smokestack.jdbc.MockStatement.StatementState;

import static org.hamcrest.MatcherAssert.assertThat; 

/**
 * @author gliptak
 *
 */
public class MockConnection implements Connection {

	private String url;
	private Properties info;
	private List<MockStatement> mockStatements=new ArrayList<MockStatement>();
	private List<MockPreparedStatement> mockPreparedStatements=new ArrayList<MockPreparedStatement>();
	
	public enum ConnectionState {NEW, CLOSE};
	protected ConnectionState mockConnectionState=ConnectionState.NEW;

	public enum AutoCommitState {ENABLED, DISABLED};
	protected AutoCommitState autoCommitState=AutoCommitState.ENABLED;

	public enum TransactionState {NEW, ROLLBACK, COMMIT, AUTOROLLBACK, AUTOCOMMIT};
	public TransactionState mockTransactionState=TransactionState.AUTOCOMMIT;
	private int holdability;
	private boolean readOnly;
	private int TransactionIsolationLevel;
	private String catalog;
	private int transactionIsolation;
	private String nativeSQL;
	private int resultSetType;
	private int resultSetConcurrency;
	private int autoGeneratedKeys;
		
	public MockConnection(String url, Properties info) {
		this.url=url;
		this.info=info;
	}

	public MockConnection() {
	}

	/* (non-Javadoc)
	 * @see java.sql.Connection#clearWarnings()
	 */
	public void clearWarnings() throws SQLException {
		assertThat(mockConnectionState, IsNot.not(ConnectionState.CLOSE));
		throw new NotYetImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.Connection#close()
	 */
	public void close() throws SQLException {
		assertThat(mockConnectionState, IsNot.not(ConnectionState.CLOSE));
		_close();
		for(MockStatement m: mockStatements){
			if(m.mockState != MockStatement.StatementState.CLOSE){
				m.autoClose();
			}
		}
		for( MockPreparedStatement mp: mockPreparedStatements){
			if(mp.mockState != MockStatement.StatementState.CLOSE){
				mp.autoClose();
			}
		}
		mockConnectionState=ConnectionState.CLOSE;
	}

	public void _close() {
	}

	/* (non-Javadoc)
	 * @see java.sql.Connection#commit()
	 */
	public void commit() throws SQLException {
		try{
			assertThat(mockConnectionState, IsNot.not(ConnectionState.CLOSE));
			assertThat(autoCommitState, Is.is(AutoCommitState.DISABLED));
			mockTransactionState = TransactionState.COMMIT;
			_commit();
		} catch (SQLException e){
			mockTransactionState = TransactionState.AUTOROLLBACK;
			throw e;
		}
	}

	public void _commit() throws SQLException {
	}

	
	/* (non-Javadoc)
	 * @see java.sql.Connection#createStatement()
	 */
	public Statement createStatement() throws SQLException {
		assertThat(mockConnectionState, IsNot.not(ConnectionState.CLOSE));
		MockStatement st= _createStatement();
		mockStatements.add(st);
		return st;
	}

	public MockStatement _createStatement() {
		return new MockStatement(this);
	}

	/* (non-Javadoc)
	 * @see java.sql.Connection#createStatement(int, int)
	 */
	public Statement createStatement(int resultSetType, int resultSetConcurrency)
			throws SQLException {
		assertThat(mockConnectionState, IsNot.not(ConnectionState.CLOSE));
		MockStatement st= _createStatement(resultSetType, resultSetConcurrency);
		if(st == null){
			st = new MockStatement(this);
		}
		st.setResultType(resultSetType);
		st.setResultSetConcurrency(resultSetConcurrency);
		mockStatements.add(st);
		return st;
	}

	public MockStatement _createStatement(int resultSetType, int resultSetConcurrency) {
		return null;
	}

	/* (non-Javadoc)
	 * @see java.sql.Connection#createStatement(int, int, int)
	 */
	public Statement createStatement(int resultSetType,	int resultSetConcurrency,
			int resultSetHoldability)	throws SQLException {
		assertThat(mockConnectionState, IsNot.not(ConnectionState.CLOSE));
		this.resultSetType = resultSetType;
		this.resultSetConcurrency = resultSetConcurrency;
		this.holdability = resultSetHoldability;
		MockStatement st= _createStatement(resultSetType, resultSetConcurrency, resultSetHoldability);
		if(st == null){
			st = new MockStatement(this);
		}
		st.setResultType(resultSetType);
		st.setResultSetConcurrency(resultSetConcurrency);
		st.setHoldability(resultSetHoldability);
		mockStatements.add(st);
		return st;
	}

	public MockStatement _createStatement(int resultSetType, int resultSetConcurrency, int resultSetHoldability) {
		return null;
	}

	/* (non-Javadoc)
	 * @see java.sql.Connection#getAutoCommit()
	 */
	public boolean getAutoCommit() throws SQLException {
		assertThat(mockConnectionState, IsNot.not(ConnectionState.CLOSE));
		return _getAutoCommit();
	}

	public boolean _getAutoCommit() {
		return autoCommitState == AutoCommitState.ENABLED?true:false;
	}

	/* (non-Javadoc)
	 * @see java.sql.Connection#getCatalog()
	 */
	public String getCatalog() throws SQLException {
		assertThat(mockConnectionState, IsNot.not(ConnectionState.CLOSE));
		return _getCatalog();
	}

	public String _getCatalog() {
		return this.catalog;
	}

	/* (non-Javadoc)
	 * @see java.sql.Connection#getHoldability()
	 */
	public int getHoldability() throws SQLException {
		assertThat(mockConnectionState, IsNot.not(ConnectionState.CLOSE));
		return _getHoldability();
	}

	public int _getHoldability() {
		return holdability;
	}

	/* (non-Javadoc)
	 * @see java.sql.Connection#getMetaData()
	 */
	public DatabaseMetaData getMetaData() throws SQLException {
		assertThat(mockConnectionState, IsNot.not(ConnectionState.CLOSE));
		throw new NotYetImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.Connection#getTransactionIsolation()
	 */
	public int getTransactionIsolation() throws SQLException {
		assertThat(mockConnectionState, IsNot.not(ConnectionState.CLOSE));
		return transactionIsolation;
	}

	/* (non-Javadoc)
	 * @see java.sql.Connection#getTypeMap()
	 */
	public Map<String, Class<?>> getTypeMap() throws SQLException {
		assertThat(mockConnectionState, IsNot.not(ConnectionState.CLOSE));
		throw new NotYetImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.Connection#getWarnings()
	 */
	public SQLWarning getWarnings() throws SQLException {
		assertThat(mockConnectionState, IsNot.not(ConnectionState.CLOSE));
		return _getWarnings();
	}

	public SQLWarning _getWarnings() {
		throw new NeedsMockDefinitionException();
	}

	/* (non-Javadoc)
	 * @see java.sql.Connection#isClosed()
	 */
	public boolean isClosed() throws SQLException {
		return _isClosed();
	}

	public boolean _isClosed() {
		return mockConnectionState==ConnectionState.CLOSE;
	}

	/* (non-Javadoc)
	 * @see java.sql.Connection#isReadOnly()
	 */
	public boolean isReadOnly() throws SQLException {
		assertThat(mockConnectionState, IsNot.not(ConnectionState.CLOSE));
		return _isReadOnly();
	}

	public boolean _isReadOnly() {
		return readOnly;
	}

	/* (non-Javadoc)
	 * @see java.sql.Connection#nativeSQL(java.lang.String)
	 */
	public String nativeSQL(String sql) throws SQLException {
		assertThat(mockConnectionState, IsNot.not(ConnectionState.CLOSE));
		this.nativeSQL = sql;
		return _nativeSQL(sql);
	}

	public String _nativeSQL(String sql) {
		throw new NeedsMockDefinitionException();
	}

	/* (non-Javadoc)
	 * @see java.sql.Connection#prepareCall(java.lang.String)
	 */
	public CallableStatement prepareCall(String sql) throws SQLException {
		assertThat(mockConnectionState, IsNot.not(ConnectionState.CLOSE));
		throw new NotYetImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.Connection#prepareCall(java.lang.String, int, int)
	 */
	public CallableStatement prepareCall(String sql, int resultSetType,
			int resultSetConcurrency) throws SQLException {
		assertThat(mockConnectionState, IsNot.not(ConnectionState.CLOSE));
		throw new NotYetImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.Connection#prepareCall(java.lang.String, int, int, int)
	 */
	public CallableStatement prepareCall(String sql, int resultSetType,
			int resultSetConcurrency, int resultSetHoldability)
			throws SQLException {
		assertThat(mockConnectionState, IsNot.not(ConnectionState.CLOSE));
		this.holdability = resultSetHoldability;
		throw new NotYetImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.Connection#prepareStatement(java.lang.String)
	 */
	public PreparedStatement prepareStatement(String sql) throws SQLException {
		assertThat(mockConnectionState, IsNot.not(ConnectionState.CLOSE));
		MockPreparedStatement pst = _prepareStatement(sql);
		mockPreparedStatements.add(pst);
		return pst;
	}

	public MockPreparedStatement _prepareStatement(String sql) {
		return new MockPreparedStatement(this, sql);
	}

	/* (non-Javadoc)
	 * @see java.sql.Connection#prepareStatement(java.lang.String, int)
	 */
	public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys)
			throws SQLException {
		assertThat(mockConnectionState, IsNot.not(ConnectionState.CLOSE));
		this.autoGeneratedKeys = autoGeneratedKeys;
		MockPreparedStatement pst = _prepareStatement(sql, autoGeneratedKeys);
		mockPreparedStatements.add(pst);
		return pst;
	}

	public MockPreparedStatement _prepareStatement(String sql, int autoGeneratedKeys) {
		return new MockPreparedStatement(this, sql);
	}

	/* (non-Javadoc)
	 * @see java.sql.Connection#prepareStatement(java.lang.String, int[])
	 */
	public PreparedStatement prepareStatement(String sql, int[] columnIndexes)
			throws SQLException {
		assertThat(mockConnectionState, IsNot.not(ConnectionState.CLOSE));
		MockPreparedStatement pst = _prepareStatement(sql, columnIndexes);
		mockPreparedStatements.add(pst);
		return pst;
	}

	public MockPreparedStatement _prepareStatement(String sql, int[] columnIndexes) {
		return new MockPreparedStatement(this, sql);
	}

	/* (non-Javadoc)
	 * @see java.sql.Connection#prepareStatement(java.lang.String, java.lang.String[])
	 */
	public PreparedStatement prepareStatement(String sql, String[] columnNames)
			throws SQLException {
		assertThat(mockConnectionState, IsNot.not(ConnectionState.CLOSE));
		MockPreparedStatement pst = _prepareStatement(sql, columnNames);
		mockPreparedStatements.add(pst);
		return pst;
	}

	public MockPreparedStatement _prepareStatement(String sql, String[] columnNames) {
		return new MockPreparedStatement(this, sql);
	}

	/* (non-Javadoc)
	 * @see java.sql.Connection#prepareStatement(java.lang.String, int, int)
	 */
	public PreparedStatement prepareStatement(String sql, int resultSetType,
			int resultSetConcurrency) throws SQLException {
		assertThat(mockConnectionState, IsNot.not(ConnectionState.CLOSE));
		MockPreparedStatement pst = _prepareStatement(sql, resultSetType, resultSetConcurrency);
		pst.setResultType(resultSetType);
		pst.setResultSetConcurrency(resultSetConcurrency);
		mockPreparedStatements.add(pst);
		return pst;
	}

	public MockPreparedStatement _prepareStatement(String sql, int resultSetType,
			int resultSetConcurrency) {
		return new MockPreparedStatement(this, sql);
	}

	/* (non-Javadoc)
	 * @see java.sql.Connection#prepareStatement(java.lang.String, int, int, int)
	 */
	public PreparedStatement prepareStatement(String sql, int resultSetType,
			int resultSetConcurrency, int resultSetHoldability)
			throws SQLException {
		assertThat(mockConnectionState, IsNot.not(ConnectionState.CLOSE));
		this.holdability = resultSetHoldability;
		MockPreparedStatement pst = _prepareStatement(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
		pst.setResultType(resultSetType);
		pst.setResultSetConcurrency(resultSetConcurrency);
		mockPreparedStatements.add(pst);
		return pst;
	}

	public MockPreparedStatement _prepareStatement(String sql, int resultSetType,
			int resultSetConcurrency, int resultSetHoldability) {
		return new MockPreparedStatement(this, sql);
	}

	/* (non-Javadoc)
	 * @see java.sql.Connection#releaseSavepoint(java.sql.Savepoint)
	 */
	public void releaseSavepoint(Savepoint savepoint) throws SQLException {
		assertThat(mockConnectionState, IsNot.not(ConnectionState.CLOSE));
		throw new NotYetImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.Connection#rollback()
	 */
	public void rollback() throws SQLException {
		assertThat(mockConnectionState, IsNot.not(ConnectionState.CLOSE));
		assertThat(autoCommitState, Is.is(AutoCommitState.DISABLED));
		mockTransactionState = TransactionState.ROLLBACK;
		_rollback();
	}
	

	public void _rollback() {
	}

	/* (non-Javadoc)
	 * @see java.sql.Connection#rollback(java.sql.Savepoint)
	 */
	public void rollback(Savepoint savepoint) throws SQLException {
		assertThat(mockConnectionState, IsNot.not(ConnectionState.CLOSE));
		assertThat(autoCommitState, Is.is(AutoCommitState.DISABLED));
		mockTransactionState = TransactionState.ROLLBACK; 
		_rollback(savepoint);
	}
	
	public void _rollback(Savepoint savepoint) throws SQLException {
	}
	
	/* (non-Javadoc)
	 * @see java.sql.Connection#setAutoCommit(boolean)
	 */
	public void setAutoCommit(boolean autoCommit) throws SQLException {
		assertThat(mockConnectionState, IsNot.not(ConnectionState.CLOSE));
		_setAutoCommit(autoCommit);
	}

	public void _setAutoCommit(boolean autoCommit) {
		autoCommitState = autoCommit? AutoCommitState.ENABLED:AutoCommitState.DISABLED;
	}

	/* (non-Javadoc)
	 * @see java.sql.Connection#setCatalog(java.lang.String)
	 */
	public void setCatalog(String catalog) throws SQLException {
		assertThat(mockConnectionState, IsNot.not(ConnectionState.CLOSE));
		_setCatalog(catalog);
	}

	public void _setCatalog(String catalog2) {
		this.catalog = catalog;
	}

	/* (non-Javadoc)
	 * @see java.sql.Connection#setHoldability(int)
	 */
	public void setHoldability(int holdability) throws SQLException {
		assertThat(mockConnectionState, IsNot.not(ConnectionState.CLOSE));
		_setHoldability(holdability);
	}

	public void _setHoldability(int holdability2) {
		this.holdability = holdability;
	}

	/* (non-Javadoc)
	 * @see java.sql.Connection#setReadOnly(boolean)
	 */
	public void setReadOnly(boolean readOnly) throws SQLException {
		assertThat(mockConnectionState, IsNot.not(ConnectionState.CLOSE));
		_setReadOnly(readOnly);
	}

	public void _setReadOnly(boolean readOnly2) {
		this.readOnly = readOnly;
	}

	/* (non-Javadoc)
	 * @see java.sql.Connection#setSavepoint()
	 */
	public Savepoint setSavepoint() throws SQLException {
		assertThat(mockConnectionState, IsNot.not(ConnectionState.CLOSE));
		throw new NotYetImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.Connection#setSavepoint(java.lang.String)
	 */
	public Savepoint setSavepoint(String name) throws SQLException {
		assertThat(mockConnectionState, IsNot.not(ConnectionState.CLOSE));
		throw new NotYetImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.Connection#setTransactionIsolation(int)
	 */
	public void setTransactionIsolation(int level) throws SQLException {
		assertThat(mockConnectionState, IsNot.not(ConnectionState.CLOSE));
		_setTransactionIsolation(level);
	}

	public void _setTransactionIsolation(int level) {
		this.TransactionIsolationLevel = level;
	}

	/* (non-Javadoc)
	 * @see java.sql.Connection#setTypeMap(java.util.Map)
	 */
	public void setTypeMap(Map<String, Class<?>> map) throws SQLException {
		assertThat(mockConnectionState, IsNot.not(ConnectionState.CLOSE));
		throw new NotYetImplementedException();
	}

	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @return the info
	 */
	public Properties getInfo() {
		return info;
	}
	
	/**
	 * Validation
	 * How do you know the order to be validated? Conn closed first, then stmts?
	 */
	public void assertExplicitClose() {
		assertThat(mockConnectionState, Is.is(ConnectionState.CLOSE));
		for(MockStatement st: mockStatements){
			st.assertExplicitClose();
		}
		for(MockPreparedStatement pst: mockPreparedStatements){
			pst.assertExplicitClose();
		}
	}

	/**
	 * Validation
	 * How do you know the order to be validated? Conn closed first, then stmts?
	 */
	public void assertClosed() {
		assertThat(mockConnectionState, Is.is(ConnectionState.CLOSE));
		for(MockStatement st: mockStatements){
			st.assertClosed();
		}
		for(MockPreparedStatement pst: mockPreparedStatements){
			pst.assertClosed();
		}
	}	
	/**
	 * Validation
	 */
	public void assertExplicitCommit() {
		assertThat("", mockTransactionState, Is.is(TransactionState.COMMIT));
	}

	/**
	 * Validation
	 */
	public void assertAutoCommit() {
		assertThat("", mockTransactionState, Is.is(TransactionState.AUTOCOMMIT));
	}

	/**
	 * Validation
	 */
	public void assertExplicitRollback() {
		assertThat(mockTransactionState, Is.is(TransactionState.ROLLBACK));
	}

	/**
	 * Validation
	 */
	public void assertAutoRollback() {
		assertThat(mockTransactionState, Is.is(TransactionState.AUTOROLLBACK));
	}

	/**
	 * @return the mockStatements
	 */
	protected List<MockStatement> getMockStatements() {
		return mockStatements;
	}

	/**
	 * @return the mockConnectionState
	 */
	protected ConnectionState getMockConnectionState() {
		return mockConnectionState;
	}

	/**
	 * @return the mockTransactionState
	 */
	protected TransactionState getMockTransactionState() {
		return mockTransactionState;
	}
	
	protected void completeOtherStatements(MockStatement st) { //spec 3.0, pg 62 transactions
		assertThat(mockConnectionState, IsNot.not(ConnectionState.CLOSE));
		for(MockStatement mst: mockStatements){
			if(mst.getId()!= st.getId()){
				mst.complete();
			}
		}		
	}
}
