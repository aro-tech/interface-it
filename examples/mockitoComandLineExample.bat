:: For v1.0.0 or higher: 
java -cp interface-it-1.0.0-SNAPSHOT.jar;<YOUR HOME DIRECTORY>\.m2\repository\org\mockito\mockito-core\2.0.43-beta\mockito-core-2.0.43-beta.jar;<YOUR HOME DIRECTORY>\.m2\repository\net\bytebuddy\byte-buddy\1.2.3\byte-buddy-1.2.3.jar;<YOUR HOME DIRECTORY>\.m2\repository\org\objenesis\objenesis\2.1\objenesis-2.1.jar com.github.aro_tech.interface_it.ui.commandline.CommandLineMain -n MyTestMockito -c org.mockito.Mockito -p org.test.package -d tmp -s <YOUR HOME DIRECTORY>\.m2\repository\org\mockito\mockito-core\2.0.43-beta\mockito-core-2.0.43-beta-sources.jar



:: Pre-v1.0.0: 
:: java -cp interface-it-1.0.0-SNAPSHOT.jar;<YOUR HOME DIRECTORY>\.m2\repository\org\mockito\mockito-core\2.0.43-beta\mockito-core-2.0.43-beta.jar;<YOUR HOME DIRECTORY>\.m2\repository\net\bytebuddy\byte-buddy\1.2.3\byte-buddy-1.2.3.jar;<YOUR HOME DIRECTORY>\.m2\repository\org\objenesis\objenesis\2.1\objenesis-2.1.jar org.interfaceit.ui.commandline.CommandLineMain -n MyTestMockito -c org.mockito.Mockito -p org.test.package -d tmp -s <YOUR HOME DIRECTORY>\.m2\repository\org\mockito\mockito-core\2.0.43-beta\mockito-core-2.0.43-beta-sources.jar