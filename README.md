# interface-it
Java-8 mix-in interface code generator


##Latest release
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.aro-tech/interface-it/badge.svg)](http://search.maven.org/#artifactdetails|com.github.aro-tech|interface-it|1.0.0|jar)

[Release notes on github] (https://github.com/aro-tech/interface-it/releases/tag/v1.0.0) 

[![Javadoc](https://javadoc-emblem.rhcloud.com/doc/com.github.aro-tech/interface-it/badge.svg)](http://www.javadoc.io/doc/com.github.aro-tech/interface-it/1.0.0)

In Maven:

```html
<dependency>
  <groupId>com.github.aro-tech</groupId>
  <artifactId>interface-it</artifactId>
  <version>1.0.0</version>
</dependency>
```

##Background
The arrival of default interfaces in Java 8 opens the door to "mix-in" interfaces - a form of multiple inheritance.

For example:
```
public class A implements MixinB, MixinC {
}
```

In Java 8 it's possible that class A, despite having 0 implementation, is actually a useful class because MixinB and MixinC have
default method implementations.

##What interface-it does

The interface-it library uses reflection to auto-generate Java code for mix-in classes that delegate to static methods.  

##Why?

This can be useful in unit tests, because libraries like Mockito and AssertJ use a lot of static methods. With a generated 
mix-in, you can replace static methods by inherited methods.

It's also useful for refactoring legacy code. You can replace static calls with calls to a mix-in interface - reducing the coupling in the design because the mix-in can be overridden (and mocked in unit tests).  

Anywhere that you use static imports, you might want to consider generating a mix-in interface instead.

##How to use interface-it

Many of you may just need to copy and paste a source file from the [examples](https://github.com/aro-tech/interface-it/examples "examples")
 and adjust it to the needs of your project.  There are delegate interfaces for [AssertJ](https://github.com/aro-tech/interface-it/blob/master/examples/AssertJ.java "AssertJ"), [Mockito](https://github.com/aro-tech/interface-it/blob/master/examples/Mockito.java "Mockito"), and [JUnit Assert](https://github.com/aro-tech/interface-it/blob/master/examples/Assert.java "JUnit Assert") which you can use in your own unit tests.  A unit test class which implements some of these is [DelegateMethodGeneratorTest](https://github.com/aro-tech/interface-it/blob/master/src/test/java/org/interfaceit/DelegateMethodGeneratorTest.java "DelegateMethodGeneratorTest.java source").

If you want to generate mix-ins which wrap other static classes, such as in your own legacy code, you can build the jar file from source using `mvn clean package` or you can just download it or use a dependency manager to fetch it for you. 

Once you have the binary, you run the jar file, providing a classpath which allows loading the class you want to wrap (and any classes it needs to load for its method signatures) and the appropriate command-line arguments. [An example .bat file](https://github.com/aro-tech/interface-it/blob/master/examples/mockitoComandLineExample.bat "Example .bat file: mockitoComandLineExample.bat") is provided to show how this is done.   

**Or, [use a custom ANT task](https://github.com/aro-tech/interface-it-ant "use a custom ANT task").**
 
 **Or, create your own UI using the [ClassCodeGenerator API](http://static.javadoc.io/com.github.aro-tech/interface-it/0.8.0/org/interfaceit/ClassCodeGenerator.html "ClassCodeGenerator API").** Here's [an example usage of the API] (https://gist.github.com/aro-tech/1a30476d40ffff4dbcd4 "an example usage of the API").
 
 Requires Java 8 (or higher)
 
##Command-line flags
```
-v > Write version number.
-n > Name of the target interface (ex: "MyMixin")
-d > Directory which will contain the generated file (default value is ".")
-c > Fully qualified delegate class name (ex: "java.lang.Math")
-p > The package name for the target interface (ex: "org.example")
-s > File path of either a .jar or .zip file or a single source file ending in .java or .txt containing source code to be used to recover argument names lost during compilation 
```
 
###Complementary project(s)
 
 * [Custom ANT task](https://github.com/aro-tech/interface-it-ant "Custom ANT task") 
 

##Acknowledgments 
Inspired by the following articles:
 - http://blog.javabien.net/2014/04/23/what-if-assertj-used-java-8/
 - https://solidsoft.wordpress.com/2015/12/01/using-mockito-without-static-imports-with-java-8/

I thought that using mockito-java8 would be a good idea, but mockito-java8 was not keeping up with the latest Mockito versions, so it seemed better to make a tool that allows you to use whatever version you want of Mockito, AssertJ or whatever static classes for which you may want to make mix-ins.

##Blog
[![The Green Bar](https://img.shields.io/badge/My_Blog:-The_Green_Bar-brightgreen.svg)](https://thegreenbar.wordpress.com/)

##Press
[![The Green Bar](https://img.shields.io/badge/Mentioned in:-SD Times-blue.svg)](http://sdtimes.com/microsoft-is-working-on-a-chrome-extension-porting-tool-a-css-tutorial-and-searchkit-0-8-sd-times-news-digest-march-21-2016/2/)

##Disclaimer
Not affiliated in any way with the Australian company interfaceit.com

