/**
 * 
 */
package com.github.aro_tech.interface_it.ui.meta.error;

import java.io.IOException;

/**
 * Functional error thrown in case of a problem reading the source 
 * @author aro_tech
 *
 */
public class UnableToReadSource extends Exception {
	private static final long serialVersionUID = 1L;
	private final IOException cause;
	
	

	public UnableToReadSource(IOException cause) {
		super();
		this.cause = cause;
	}



	/**
	 * @return the cause
	 */
	@Override
	public IOException getCause() {
		return cause;
	}
	
	
}
