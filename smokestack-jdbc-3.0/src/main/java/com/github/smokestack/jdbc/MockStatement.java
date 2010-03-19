/**
 * 
 */
package com.github.smokestack.jdbc;

import static org.hamcrest.MatcherAssert.assertThat;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.hamcrest.core.AnyOf;
import org.hamcrest.core.Is;
import org.hamcrest.core.IsNot;

import com.github.smokestack.exception.NeedsMockDefinitionException;
import com.github.smokestack.exception.NotYetImplementedException;

/**
 * @author gliptak
 *
 */
public class MockStatement implements Statement {

	private static long autoId = 0;
	private long id;
	private int direction;
	private Connection connection;
	private MockConnection parent;
	
	private List<MockResultSet> mockResultSets=new ArrayList<MockResultSet>();

	public enum StatementState {NEW, CLOSE, COMPLETE, AUTOCLOSE};
	
	protected StatementState mockState=StatementState.NEW;
	private int fetchSize;
	private List<String> batchSQLs = new ArrayList<String>();
	private int maxFieldSize;
	private int maxRows;
	private int queryTimeout;
	private int resultSetConcurrency;
	private int resultSetHoldability;
	private int resultSetType;
	private int updateCount;
	private String cursorName;
	private boolean escapeProcessing;

	public MockStatement(Connection connection) {
		this.connection=connection;
		this.parent = (MockConnection) connection;
		this.id = ++autoId;
	}

	/* (non-Javadoc)
	 * @see java.sql.Statement#addBatch(java.lang.String)
	 */
	public void addBatch(String sql) throws SQLException {
		assertThat(mockState, AnyOf.anyOf(IsNot.not(StatementState.CLOSE), IsNot.not(StatementState.AUTOCLOSE)));
		assertThat("Auto Commit State", parent.autoCommitState, Is.is(MockConnection.AutoCommitState.DISABLED));
		_addBatch(sql);
		batchSQLs.add(new String());
	}

	public void _addBatch(String sql) {
	}

	/* (non-Javadoc)
	 * @see java.sql.Statement#cancel()
	 */
	public void cancel() throws SQLException {
		assertThat(mockState, AnyOf.anyOf(IsNot.not(StatementState.CLOSE), IsNot.not(StatementState.AUTOCLOSE)));
		_cancel();
		this.mockState = StatementState.NEW;
	}

	public void _cancel() throws SQLException {
	}

	/* (non-Javadoc)
	 * @see java.sql.Statement#clearBatch()
	 */
	public void clearBatch() throws SQLException {
		assertThat(mockState, AnyOf.anyOf(IsNot.not(StatementState.CLOSE), IsNot.not(StatementState.AUTOCLOSE)));
		assertThat("Auto Commit State", parent.autoCommitState, Is.is(MockConnection.AutoCommitState.DISABLED));
		_clearBatch();
		batchSQLs.clear();
	}

	public void _clearBatch() {
	}

	/* (non-Javadoc)
	 * @see java.sql.Statement#clearWarnings()
	 */
	public void clearWarnings() throws SQLException {
		assertThat(mockState, AnyOf.anyOf(IsNot.not(StatementState.CLOSE), IsNot.not(StatementState.AUTOCLOSE)));
		_clearWarnings();
		this.mockState = StatementState.NEW;
	}

	public void _clearWarnings() {
	}

	/* (non-Javadoc)
	 * @see java.sql.Statement#close()
	 */
	public void close() throws SQLException {
		assertThat("Statement State", mockState, AnyOf.anyOf(IsNot.not(StatementState.CLOSE), IsNot.not(StatementState.AUTOCLOSE)));
		assertThat("Statement State", mockState, Is.is(StatementState.COMPLETE));
		_close();
		autoCloseRS();
		this.mockState=StatementState.CLOSE;
	}

	protected void autoCloseRS() {
		for(MockResultSet rs: mockResultSets){
			if(rs.mockState != MockResultSet.ResultSetState.CLOSE){
				rs.autoClose();
			}
		}
	}

	public void _close() throws SQLException {
	}

	/* (non-Javadoc)
	 * @see java.sql.Statement#execute(java.lang.String)
	 */
	public boolean execute(String sql) throws SQLException {
		assertThat(mockState, AnyOf.anyOf(IsNot.not(StatementState.CLOSE), IsNot.not(StatementState.AUTOCLOSE)));
		mockState = StatementState.COMPLETE;
		parent.completeOtherStatements(this);
		return _execute(sql);
	}

	/* (non-Javadoc)
	 * @see java.sql.Statement#execute(java.lang.String)
	 */
	public boolean _execute(String sql) throws SQLException {
		throw new NeedsMockDefinitionException();	
	}

	/* (non-Javadoc)
	 * @see java.sql.Statement#execute(java.lang.String, int)
	 */
	public boolean execute(String sql, int autoGeneratedKeys)
			throws SQLException {
		assertThat(mockState, AnyOf.anyOf(IsNot.not(StatementState.CLOSE), IsNot.not(StatementState.AUTOCLOSE)));
		mockState = StatementState.COMPLETE;
		parent.completeOtherStatements(this);
		return _execute(sql, autoGeneratedKeys);
	}

	public boolean _execute(String sql, int autoGeneratedKeys) {
		throw new NeedsMockDefinitionException();	
	}

	/* (non-Javadoc)
	 * @see java.sql.Statement#execute(java.lang.String, int[])
	 */
	public boolean execute(String sql, int[] columnIndexes) throws SQLException {
		assertThat(mockState, AnyOf.anyOf(IsNot.not(StatementState.CLOSE), IsNot.not(StatementState.AUTOCLOSE)));
		mockState = StatementState.COMPLETE;
		parent.completeOtherStatements(this);
		return _execute(sql, columnIndexes);
	}

	public boolean _execute(String sql, int[] columnIndexes) {
		throw new NeedsMockDefinitionException();	
	}

	/* (non-Javadoc)
	 * @see java.sql.Statement#execute(java.lang.String, java.lang.String[])
	 */
	public boolean execute(String sql, String[] columnNames)
			throws SQLException {
		assertThat(mockState, AnyOf.anyOf(IsNot.not(StatementState.CLOSE), IsNot.not(StatementState.AUTOCLOSE)));
		mockState = StatementState.COMPLETE;
		parent.completeOtherStatements(this);
		return _execute(sql, columnNames);
	}

	public boolean _execute(String sql, String[] columnNames) {
		throw new NeedsMockDefinitionException();	
	}

	/* (non-Javadoc)
	 * @see java.sql.Statement#executeBatch()
	 */
	public int[] executeBatch() throws SQLException {
		assertThat("mock State", mockState, AnyOf.anyOf(IsNot.not(StatementState.CLOSE), IsNot.not(StatementState.AUTOCLOSE)));
		assertThat("Auto Commit State", parent.autoCommitState, Is.is(MockConnection.AutoCommitState.DISABLED));
		int[] b = null;
		try{
			b = _executeBatch();
			mockState = StatementState.COMPLETE;
			parent.completeOtherStatements(this);
		}catch (SQLException se){
			parent.mockTransactionState= MockConnection.TransactionState.ROLLBACK;
		}
		return b;
	}

	public int[] _executeBatch() throws SQLException {
		throw new NeedsMockDefinitionException();	
	}

	/* (non-Javadoc)
	 * @see java.sql.Statement#executeQuery(java.lang.String)
	 */
	public ResultSet executeQuery(String sql) throws SQLException {
		assertThat(mockState, AnyOf.anyOf(IsNot.not(StatementState.CLOSE), IsNot.not(StatementState.AUTOCLOSE)));
		MockResultSet rs=new MockResultSet(sql);
		rs.setParent(this);
		mockResultSets.add(rs);
		mockState = StatementState.COMPLETE;
		parent.completeOtherStatements(this);
		_executeQuery(sql);
		return rs;
	}

	public void _executeQuery(String sql) {
	}

	/* (non-Javadoc)
	 * @see java.sql.Statement#executeUpdate(java.lang.String)
	 */
	public int executeUpdate(String sql) throws SQLException {
		assertThat(mockState, AnyOf.anyOf(IsNot.not(StatementState.CLOSE), IsNot.not(StatementState.AUTOCLOSE)));
		int b = 0;
		parent.mockTransactionState= MockConnection.TransactionState.AUTOCOMMIT;
		try{
			mockState = StatementState.COMPLETE;
			parent.completeOtherStatements(this);
			b = _executeUpdate(sql);
		}catch (SQLException se){
			parent.mockTransactionState= MockConnection.TransactionState.ROLLBACK;
		}		
		return b;
	}

	/* (non-Javadoc)
	 * @see java.sql.Statement#executeUpdate(java.lang.String)
	 */
	public int _executeUpdate(String sql) throws SQLException {
		throw new NeedsMockDefinitionException();	
	}	
	/* (non-Javadoc)
	 * @see java.sql.Statement#executeUpdate(java.lang.String, int)
	 */
	public int executeUpdate(String sql, int autoGeneratedKeys)
			throws SQLException {
		assertThat(mockState, AnyOf.anyOf(IsNot.not(StatementState.CLOSE), IsNot.not(StatementState.AUTOCLOSE)));
		int b = 0;
		parent.mockTransactionState= MockConnection.TransactionState.AUTOCOMMIT;
		try{
			mockState = StatementState.COMPLETE;
			parent.completeOtherStatements(this);
			b = _executeUpdate(sql, autoGeneratedKeys);
		}catch (SQLException se){
			parent.mockTransactionState= MockConnection.TransactionState.ROLLBACK;
		}		
		return b;
	}

	public int _executeUpdate(String sql, int autoGeneratedKeys) throws SQLException{
		throw new NeedsMockDefinitionException();	
	}

	/* (non-Javadoc)
	 * @see java.sql.Statement#executeUpdate(java.lang.String, int[])
	 */
	public int executeUpdate(String sql, int[] columnIndexes)
			throws SQLException {
		assertThat(mockState, AnyOf.anyOf(IsNot.not(StatementState.CLOSE), IsNot.not(StatementState.AUTOCLOSE)));
		int b = 0;
		parent.mockTransactionState= MockConnection.TransactionState.AUTOCOMMIT;
		try{
			mockState = StatementState.COMPLETE;
			parent.completeOtherStatements(this);
			b = _executeUpdate(sql, columnIndexes);
		}catch (SQLException se){
			parent.mockTransactionState= MockConnection.TransactionState.ROLLBACK;
		}		
		return b;
	}

	public int _executeUpdate(String sql, int[] columnIndexes) throws SQLException{
		throw new NeedsMockDefinitionException();	
	}

	/* (non-Javadoc)
	 * @see java.sql.Statement#executeUpdate(java.lang.String, java.lang.String[])
	 */
	public int executeUpdate(String sql, String[] columnNames)
			throws SQLException {
		assertThat(mockState, AnyOf.anyOf(IsNot.not(StatementState.CLOSE), IsNot.not(StatementState.AUTOCLOSE)));
		int b = 0;
		parent.mockTransactionState= MockConnection.TransactionState.AUTOCOMMIT;
		try{
			mockState = StatementState.COMPLETE;
			parent.completeOtherStatements(this);
			b = _executeUpdate(sql, columnNames);
		}catch (SQLException se){
			parent.mockTransactionState= MockConnection.TransactionState.ROLLBACK;
		}		
		return b;
	}

	public int _executeUpdate(String sql, String[] columnNames) throws SQLException{
		throw new NeedsMockDefinitionException();	
	}

	/* (non-Javadoc)
	 * @see java.sql.Statement#getConnection()
	 */
	public Connection getConnection() throws SQLException {
		assertThat(mockState, AnyOf.anyOf(IsNot.not(StatementState.CLOSE), IsNot.not(StatementState.AUTOCLOSE)));
		_getConnection();
		return connection;
	}

	public Connection _getConnection() {
		return null; 
	}

	/* (non-Javadoc)
	 * @see java.sql.Statement#getFetchDirection()
	 */
	public int getFetchDirection() throws SQLException {
		assertThat(mockState, AnyOf.anyOf(IsNot.not(StatementState.CLOSE), IsNot.not(StatementState.AUTOCLOSE)));
		_getFetchDirection();
		return direction;
	}

	public int _getFetchDirection() {
		return -1;
	}

	/* (non-Javadoc)
	 * @see java.sql.Statement#getFetchSize()
	 */
	public int getFetchSize() throws SQLException {
		assertThat(mockState, AnyOf.anyOf(IsNot.not(StatementState.CLOSE), IsNot.not(StatementState.AUTOCLOSE)));
		_getFetchSize();
		return fetchSize;
	}

	public int _getFetchSize() {
		return -1;
	}

	/* (non-Javadoc)
	 * @see java.sql.Statement#getGeneratedKeys()
	 */
	public ResultSet getGeneratedKeys() throws SQLException {
		assertThat(mockState, AnyOf.anyOf(IsNot.not(StatementState.CLOSE), IsNot.not(StatementState.AUTOCLOSE)));
		throw new NotYetImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.Statement#getMaxFieldSize()
	 */
	public int getMaxFieldSize() throws SQLException {
		assertThat(mockState, AnyOf.anyOf(IsNot.not(StatementState.CLOSE), IsNot.not(StatementState.AUTOCLOSE)));
		_getMaxFieldSize();
		return maxFieldSize;
	}

	public int _getMaxFieldSize() {
		return -1;
	}

	/* (non-Javadoc)
	 * @see java.sql.Statement#getMaxRows()
	 */
	public int getMaxRows() throws SQLException {
		assertThat(mockState, AnyOf.anyOf(IsNot.not(StatementState.CLOSE), IsNot.not(StatementState.AUTOCLOSE)));
		_getMaxRows();
		return maxRows;
	}

	public int _getMaxRows() {
		return -1;
	}

	/* (non-Javadoc)
	 * @see java.sql.Statement#getMoreResults()
	 */
	public boolean getMoreResults() throws SQLException {
		assertThat(mockState, AnyOf.anyOf(IsNot.not(StatementState.CLOSE), IsNot.not(StatementState.AUTOCLOSE)));
		return _getMoreResults();
	}

	public boolean _getMoreResults() {
		throw new NeedsMockDefinitionException();
	}

	/* (non-Javadoc)
	 * @see java.sql.Statement#getMoreResults(int)
	 */
	public boolean getMoreResults(int current) throws SQLException {
		assertThat(mockState, AnyOf.anyOf(IsNot.not(StatementState.CLOSE), IsNot.not(StatementState.AUTOCLOSE)));
		return _getMoreResults(current);
	}

	public boolean _getMoreResults(int current) {
		throw new NeedsMockDefinitionException();
	}

	/* (non-Javadoc)
	 * @see java.sql.Statement#getQueryTimeout()
	 */
	public int getQueryTimeout() throws SQLException {
		assertThat(mockState, AnyOf.anyOf(IsNot.not(StatementState.CLOSE), IsNot.not(StatementState.AUTOCLOSE)));
		_getQueryTimeout();
		return queryTimeout;
	}

	public int _getQueryTimeout() {
		return -1;
	}

	/* (non-Javadoc)
	 * @see java.sql.Statement#getResultSet()
	 */
	public ResultSet getResultSet() throws SQLException {
		assertThat(mockState, AnyOf.anyOf(IsNot.not(StatementState.CLOSE), IsNot.not(StatementState.AUTOCLOSE)));
		_getResultSet();
		return this.mockResultSets.get(mockResultSets.size()-1);
	}

	public ResultSet _getResultSet() {
		return null; 
	}

	/* (non-Javadoc)
	 * @see java.sql.Statement#getResultSetConcurrency()
	 */
	public int getResultSetConcurrency() throws SQLException {
		assertThat(mockState, AnyOf.anyOf(IsNot.not(StatementState.CLOSE), IsNot.not(StatementState.AUTOCLOSE)));
		_getResultSetConcurrency();
		return resultSetConcurrency;
	}

	public int _getResultSetConcurrency() {
		return -1;
	}

	/* (non-Javadoc)
	 * @see java.sql.Statement#getResultSetHoldability()
	 */
	public int getResultSetHoldability() throws SQLException {
		assertThat(mockState, AnyOf.anyOf(IsNot.not(StatementState.CLOSE), IsNot.not(StatementState.AUTOCLOSE)));
		_getResultSetHoldability();
		return resultSetHoldability;
	}

	public int _getResultSetHoldability() {
		return -1;
	}

	/* (non-Javadoc)
	 * @see java.sql.Statement#getResultSetType()
	 */
	public int getResultSetType() throws SQLException {
		assertThat(mockState, AnyOf.anyOf(IsNot.not(StatementState.CLOSE), IsNot.not(StatementState.AUTOCLOSE)));
		_getResultSetType();
		return resultSetType;
	}

	public int _getResultSetType() {
		return -1;
	}

	/* (non-Javadoc)
	 * @see java.sql.Statement#getUpdateCount()
	 */
	public int getUpdateCount() throws SQLException {
		assertThat(mockState, AnyOf.anyOf(IsNot.not(StatementState.CLOSE), IsNot.not(StatementState.AUTOCLOSE)));
		return _getUpdateCount();
	}

	public int _getUpdateCount() {
		throw new NeedsMockDefinitionException();
	}

	/* (non-Javadoc)
	 * @see java.sql.Statement#getWarnings()
	 */
	public SQLWarning getWarnings() throws SQLException {
		assertThat(mockState, AnyOf.anyOf(IsNot.not(StatementState.CLOSE), IsNot.not(StatementState.AUTOCLOSE)));
		return _getWarnings();
	}

	public SQLWarning _getWarnings() {
		throw new NeedsMockDefinitionException();
	}

	/* (non-Javadoc)
	 * @see java.sql.Statement#setCursorName(java.lang.String)
	 */
	public void setCursorName(String name) throws SQLException {
		assertThat(mockState, AnyOf.anyOf(IsNot.not(StatementState.CLOSE), IsNot.not(StatementState.AUTOCLOSE)));
		_setCursorName( name);
		this.cursorName = name;
	}

	public void _setCursorName(String name) {
	}

	/* (non-Javadoc)
	 * @see java.sql.Statement#setEscapeProcessing(boolean)
	 */
	public void setEscapeProcessing(boolean enable) throws SQLException {
		assertThat(mockState, AnyOf.anyOf(IsNot.not(StatementState.CLOSE), IsNot.not(StatementState.AUTOCLOSE)));
		_setEscapeProcessing(enable);
		this.escapeProcessing = enable;
	}

	public void _setEscapeProcessing(boolean enable) {
	}

	/* (non-Javadoc)
	 * @see java.sql.Statement#setFetchDirection(int)
	 */
	public void setFetchDirection(int direction) throws SQLException {
		assertThat(mockState, AnyOf.anyOf(IsNot.not(StatementState.CLOSE), IsNot.not(StatementState.AUTOCLOSE)));
		_setFetchDirection( direction);
		this.direction=direction;
	}

	public void _setFetchDirection(int direction) {
	}

	/* (non-Javadoc)
	 * @see java.sql.Statement#setFetchSize(int)
	 */
	public void setFetchSize(int rows) throws SQLException {
		assertThat(mockState, AnyOf.anyOf(IsNot.not(StatementState.CLOSE), IsNot.not(StatementState.AUTOCLOSE)));
		_setFetchSize(rows);
		this.fetchSize = rows;
	}

	public void _setFetchSize(int rows) {
	}

	/* (non-Javadoc)
	 * @see java.sql.Statement#setMaxFieldSize(int)
	 */
	public void setMaxFieldSize(int max) throws SQLException {
		assertThat(mockState, AnyOf.anyOf(IsNot.not(StatementState.CLOSE), IsNot.not(StatementState.AUTOCLOSE)));
		_setMaxFieldSize(max);
		this.maxFieldSize = max;
	}

	public void _setMaxFieldSize(int max) {
	}

	/* (non-Javadoc)
	 * @see java.sql.Statement#setMaxRows(int)
	 */
	public void setMaxRows(int max) throws SQLException {
		assertThat(mockState, AnyOf.anyOf(IsNot.not(StatementState.CLOSE), IsNot.not(StatementState.AUTOCLOSE)));
		_setMaxRows(max);
		this.maxRows = max;
	}

	public void _setMaxRows(int max) {
	}

	/* (non-Javadoc)
	 * @see java.sql.Statement#setQueryTimeout(int)
	 */
	public void setQueryTimeout(int seconds) throws SQLException {
		assertThat(mockState, AnyOf.anyOf(IsNot.not(StatementState.CLOSE), IsNot.not(StatementState.AUTOCLOSE)));
		_setQueryTimeout(seconds);
		this.queryTimeout = seconds;
	}

	public void _setQueryTimeout(int seconds) {
	}

	/**
	 * @return the mockResultSets
	 */
	public List<MockResultSet> getMockResultSets() {
		assertThat(mockState, AnyOf.anyOf(IsNot.not(StatementState.CLOSE), IsNot.not(StatementState.AUTOCLOSE)));
		return mockResultSets;
	}

	protected void autoClose() {
		assertThat(mockState, AnyOf.anyOf(IsNot.not(StatementState.CLOSE), IsNot.not(StatementState.AUTOCLOSE)));
		autoCloseRS();		
		this.mockState=StatementState.AUTOCLOSE;
	}

	public void complete() {
		mockState = StatementState.COMPLETE;
	}

	public long getId() {
		// TODO Auto-generated method stub
		return id;
	}

	public void setParent(MockConnection mockConnection) {
		this.parent = mockConnection;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setResultType(int resultSetType) {
		this.resultSetType = resultSetType;
	}

	public void setResultSetConcurrency(int resultSetConcurrency) {
		this.resultSetConcurrency = resultSetConcurrency;
	}

	public void setHoldability(int resultSetHoldability) {
		this.resultSetHoldability = resultSetHoldability;
	}
	
	/**
	 * Utility assertion method that allows the users to verify that the
	 * statement is explicitly closed and all its associated result sets
	 * are also explicitly closed
	 */
	public void assertExplicitClose() {
		assertThat(mockState, Is.is(StatementState.CLOSE));
		for(MockResultSet rs: mockResultSets){
			rs.assertExplicitClose();
		}
	}

	/**
	 * Utility assertion method that allows the users to verify that the
	 * statement is closed and all its associated result sets
	 */
	public void assertClosed() {
		assertThat(mockState, AnyOf.anyOf(Is.is(StatementState.CLOSE), Is.is(StatementState.AUTOCLOSE)));
		for(MockResultSet rs: mockResultSets){
			rs.assertClosed();
		}
	}
	
	@Override
	public String toString(){
		return ReflectionToStringBuilder.toString(this, ToStringStyle.MULTI_LINE_STYLE);
	}
}
