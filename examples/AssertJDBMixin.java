package com.github.aro_tech.interface_it.util.mixin;

import java.io.File; 
import org.assertj.db.api.Assertions; 
import org.assertj.db.api.ChangesAssert; 
import org.assertj.db.api.RequestAssert; 
import org.assertj.db.api.TableAssert; 
import org.assertj.db.type.Changes; 
import org.assertj.db.type.Request; 
import org.assertj.db.type.Table; 

/** 
 * Wrapper of static elements in org.assertj.db.api.Assertions
 * Generated by Interface-It: https://github.com/aro-tech/interface-it
 * {@link org.assertj.db.api.Assertions}
 */
public interface AssertJDBMixin {


    // CONSTANTS: 


    // DELEGATE METHODS: 

    /**
     * Delegate call to public static org.assertj.db.api.ChangesAssert org.assertj.db.api.Assertions.assertThat(org.assertj.db.type.Changes)
     * {@link org.assertj.db.api.Assertions#assertThat(org.assertj.db.type.Changes)}
     */
    default ChangesAssert assertThat(Changes changes) {
        return Assertions.assertThat(changes);
    }



    /**
     * Delegate call to public static org.assertj.db.api.RequestAssert org.assertj.db.api.Assertions.assertThat(org.assertj.db.type.Request)
     * {@link org.assertj.db.api.Assertions#assertThat(org.assertj.db.type.Request)}
     */
    default RequestAssert assertThat(Request request) {
        return Assertions.assertThat(request);
    }



    /**
     * Delegate call to public static org.assertj.db.api.TableAssert org.assertj.db.api.Assertions.assertThat(org.assertj.db.type.Table)
     * {@link org.assertj.db.api.Assertions#assertThat(org.assertj.db.type.Table)}
     */
    default TableAssert assertThat(Table table) {
        return Assertions.assertThat(table);
    }



    /**
     * Delegate call to public static byte[] org.assertj.db.api.Assertions.bytesContentFromClassPathOf(java.lang.String)
     * {@link org.assertj.db.api.Assertions#bytesContentFromClassPathOf(java.lang.String)}
     */
    default byte[] bytesContentFromClassPathOf(String resource) {
        return Assertions.bytesContentFromClassPathOf(resource);
    }



    /**
     * Delegate call to public static byte[] org.assertj.db.api.Assertions.bytesContentOf(java.io.File)
     * {@link org.assertj.db.api.Assertions#bytesContentOf(java.io.File)}
     */
    default byte[] bytesContentOf(File file) {
        return Assertions.bytesContentOf(file);
    }



}