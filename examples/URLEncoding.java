package com.github.aro_tech.interface_it.util.mixin;

import java.io.UnsupportedEncodingException; 
import java.lang.String; 
import java.net.URLEncoder; 

/** 
 * Wrapper of static elements in java.net.URLEncoder
 * Generated by Interface-It: https://github.com/aro-tech/interface-it
 * {@link java.net.URLEncoder}
 */
public interface URLEncoding {


    // CONSTANTS: 


    // DELEGATE METHODS: 

    /**
     * Delegate call to public static java.lang.String java.net.URLEncoder.encode(java.lang.String)
     * {@link java.net.URLEncoder#encode(java.lang.String)}
     */
    @Deprecated
    default String encode(String stringToEncode) {
        return URLEncoder.encode(stringToEncode);
    }



    /**
     * Delegate call to public static java.lang.String java.net.URLEncoder.encode(java.lang.String,java.lang.String) throws java.io.UnsupportedEncodingException
     * {@link java.net.URLEncoder#encode(java.lang.String,java.lang.String)}
     */
    default String encode(String stringToEncode, String characterEncoding) throws UnsupportedEncodingException {
        return URLEncoder.encode(stringToEncode, characterEncoding);
    }



}