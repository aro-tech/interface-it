# interface-it
Java-8 mix-in interface code generator

The arrival of default interfaces in Java 8 opens the door to "mix-in" interfaces - a form of multiple inheritance.

For example:

public class A implements MixinB, MixinC {
}

In Java 8 it's possible that class A, despite having 0 implementation, is actually a useful class because MixinB and MixinC have
default method implementations.

The interface-it library uses reflection to auto-generate Java code for mix-in classes that delegate to static methods.  

This can be useful in unit tests, because libraries like Mockito and AssertJ use a lot of static methods. With a generated 
mix-in, you can replace static methods by inherited methods.

It's also useful for refactoring legacy code. You can replace static calls with calls to a mix-in interface that can be mocked
in unit tests.

Anywhere that you use static imports, you might want to consider generating a mix-in interface instead.

Future directions:

Generate mix-ins for singletons

Acknowledgments: 
Inspired by the following articles:
http://blog.javabien.net/2014/04/23/what-if-assertj-used-java-8/
https://solidsoft.wordpress.com/2015/12/01/using-mockito-without-static-imports-with-java-8/

Note that the advantage of using interface-it over just 
