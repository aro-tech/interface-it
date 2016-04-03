/**
 * 
 */
package com.github.aro_tech.interface_it.ui.meta.error;

/**
 * Exception thrown when a source file or archive is read but the expected
 * source code is found in it
 * 
 * @author aro_tech
 *
 */
public class EmptySource extends Exception {
	private static final long serialVersionUID = 1L;

	public EmptySource(String message) {
		super(message);
	}

}
