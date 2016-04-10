/**
 * 
 */
package com.github.aro_tech.interface_it.api.options;

import java.io.File;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;

import com.github.aro_tech.interface_it.api.MultiFileOutputOptions;

/**
 * Options class to use when generating parent and child class wrappers
 * 
 * @author aro_tech
 *
 */
public class OptionsForSplittingChildAndParent implements MultiFileOutputOptions {
	private final String targetPackage;
	private final File saveDirectory;
	private final String childMixinName;
	private final String parentMixinName;
	private final Class<?> childClass;

	/**
	 * Constructor
	 * 
	 * @param targetPackage
	 * @param saveDirectory
	 * @param childMixinName
	 * @param parentMixinName
	 * @param childClass
	 */
	public OptionsForSplittingChildAndParent(String targetPackage, File saveDirectory, String childMixinName,
			String parentMixinName, Class<?> childClass) {
		super();
		this.targetPackage = targetPackage;
		this.saveDirectory = saveDirectory;
		this.childMixinName = childMixinName;
		this.parentMixinName = parentMixinName;
		this.childClass = childClass;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.github.aro_tech.interface_it.api.MultiFileOutputOptions#
	 * getTargetPackageNameForDelegate(java.lang.Class)
	 */
	@Override
	public String getTargetPackageNameForDelegate(Class<?> delegateClass) {
		return targetPackage;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.github.aro_tech.interface_it.api.MultiFileOutputOptions#
	 * getMixinSaveDirectoryForDelegate(java.lang.Class)
	 */
	@Override
	public File getMixinSaveDirectoryForDelegate(Class<?> delegate) {
		return this.saveDirectory;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.github.aro_tech.interface_it.api.MultiFileOutputOptions#
	 * getTargetInterfaceNameForDelegate(java.lang.Class)
	 */
	@Override
	public String getTargetInterfaceNameForDelegate(Class<?> delegateClass) {
		return delegateClass.equals(this.childClass) ? this.childMixinName : this.parentMixinName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.github.aro_tech.interface_it.api.MultiFileOutputOptions#
	 * getMethodFilter()
	 */
	@Override
	public Predicate<? super Method> getMethodFilter() {
		return m -> m.getDeclaringClass().equals(childClass);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.github.aro_tech.interface_it.api.MultiFileOutputOptions#getSuperTypes
	 * (java.lang.Class)
	 */
	@Override
	public Set<String> getSuperTypes(Class<?> delegateClass) {
		if (delegateClass.equals(this.childClass)) {
			return new HashSet<String>() {
				private static final long serialVersionUID = 1L;
				{
					add(parentMixinName);
				}
			};
		}
		return Collections.emptySet();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "OptionsForSplittingChildAndParent [targetPackage=" + targetPackage + ", saveDirectory=" + saveDirectory
				+ ", childMixinName=" + childMixinName + ", parentMixinName=" + parentMixinName + ", childClass="
				+ childClass + ", getMethodFilter()=" + getMethodFilter() + "]";
	}

	
}
