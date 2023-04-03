@echo off
SET wd=%~dp0

SET solutions=%wd%\solutions\java-solutions

SET impl_package=itmo.ja.dyagin.implementor

SET link=https://docs.oracle.com/en/java/javase/19/docs/api

SET test_dir=%wd%\itmo\ja\dyagin\implementor

SET jar_impler=%test_dir%\JarImpler.java

SET impler=%test_dir%\Impler.java

@echo on

javadoc -d %wd%\javadoc -link %link% -cp %solutions%; -private^
 -author -version %impl_package% %jar_impler% %impler%

