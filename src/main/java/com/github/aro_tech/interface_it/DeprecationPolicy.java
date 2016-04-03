/**
 * 
 */
package com.github.aro_tech.interface_it;

/**
 * Policy choice on how to handle a deprecated method
 * @author aro_tech
 *
 */
public enum DeprecationPolicy {
	/**
	 * Wrap the deprecated method, flagging the wrapping method as deprecated
	 */
	PROPAGATE_DEPRECATION,
	/**
	 * Wrap the deprecated method without flagging the wrapping method as deprecated
	 */
	WRAP_WITHOUT_DEPRECATING,
	/**
	 * Do not wrap the deprecated method
	 */
	IGNORE_DEPRECATED_METHODS;
}
