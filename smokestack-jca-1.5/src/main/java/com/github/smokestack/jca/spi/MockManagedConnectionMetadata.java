/**
 * 
 */
package com.github.smokestack.jca.spi;

import javax.resource.ResourceException;
import javax.resource.spi.ManagedConnectionMetaData;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import com.github.smokestack.exception.NeedsMockDefinitionException;

/**
 * @author gliptak
 *
 */
public class MockManagedConnectionMetadata implements ManagedConnectionMetaData {

	/* (non-Javadoc)
	 * @see javax.resource.spi.ManagedConnectionMetaData#getEISProductName()
	 */
	public String getEISProductName() throws ResourceException {
		return _getEISProductName();
	}

	public String _getEISProductName() throws ResourceException {
		throw new NeedsMockDefinitionException();
	}

	/* (non-Javadoc)
	 * @see javax.resource.spi.ManagedConnectionMetaData#getEISProductVersion()
	 */
	public String getEISProductVersion() throws ResourceException {
		return _getEISProductVersion();
	}

	public String _getEISProductVersion() throws ResourceException {
		throw new NeedsMockDefinitionException();
	}

	/* (non-Javadoc)
	 * @see javax.resource.spi.ManagedConnectionMetaData#getMaxConnections()
	 */
	public int getMaxConnections() throws ResourceException {
		return _getMaxConnections();
	}

	public int _getMaxConnections() throws ResourceException {
		throw new NeedsMockDefinitionException();
	}

	/* (non-Javadoc)
	 * @see javax.resource.spi.ManagedConnectionMetaData#getUserName()
	 */
	public String getUserName() throws ResourceException {
		return _getUserName();
	}

	public String _getUserName() throws ResourceException {
		throw new NeedsMockDefinitionException();
	}

	public String toString(){
		return ReflectionToStringBuilder.toString(this, ToStringStyle.MULTI_LINE_STYLE);
	}
}
