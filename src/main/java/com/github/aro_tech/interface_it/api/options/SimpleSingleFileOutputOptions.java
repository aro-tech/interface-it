/**
 * 
 */
package com.github.aro_tech.interface_it.api.options;

import java.io.File;
import java.lang.reflect.Method;
import java.util.function.Predicate;

import com.github.aro_tech.interface_it.api.MultiFileOutputOptions;

/**
 * Implementation which serves to adapt the new multi-file option-based one to
 * the old version's argument list
 * 
 * @author aro_tech
 *
 */
public class SimpleSingleFileOutputOptions implements MultiFileOutputOptions {
	private final String targetInterfaceName;
	private final String targetPackageName;
	private final File saveDirectory;

	/**
	 * Constructor
	 * @param targetInterfaceName
	 * @param targetPackageName
	 * @param saveDirectory
	 */
	public SimpleSingleFileOutputOptions(String targetInterfaceName, String targetPackageName, File saveDirectory) {
		this.targetInterfaceName = targetInterfaceName;
		this.targetPackageName = targetPackageName;
		this.saveDirectory = saveDirectory;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.github.aro_tech.interface_it.api.MultiFileOutputOptions#
	 * getTargetInterfaceNameForDelegate(java.lang.Class)
	 */
	@Override
	public String getTargetInterfaceNameForDelegate(Class<?> delegateClass) {
		return targetInterfaceName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.github.aro_tech.interface_it.api.MultiFileOutputOptions#
	 * getTargetPackageNameForDelegate(java.lang.Class)
	 */
	@Override
	public String getTargetPackageNameForDelegate(Class<?> delegateClass) {
		return targetPackageName;
	}

	/* (non-Javadoc)
	 * @see com.github.aro_tech.interface_it.api.MultiFileOutputOptions#getMixinSaveDirectoryForDelegate(java.lang.Class)
	 */
	@Override
	public File getMixinSaveDirectoryForDelegate(Class<?> delegate) {
		return saveDirectory;
	}

}
