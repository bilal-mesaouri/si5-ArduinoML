[[1;34mINFO[m] Scanning for projects...
[[1;34mINFO[m] 
[[1;34mINFO[m] [1m-------------< [0;36mio.github.mosser.arduinoml:external-antlr[0;1m >--------------[m
[[1;34mINFO[m] [1mBuilding external-antlr 1.0-SNAPSHOT[m
[[1;34mINFO[m] [1m--------------------------------[ jar ]---------------------------------[m
[[1;34mINFO[m] 
[[1;34mINFO[m] [1m--- [0;32mexec-maven-plugin:1.6.0:java[m [1m(default-cli)[m @ [36mexternal-antlr[0;1m ---[m


Running the ANTLR compiler for ArduinoML
Using input file: /home/bilal/Documents/vcx/dsl/ArduinoML-kernel/externals/antlr/src/main/resources/red_button.arduinoml
[[1;33mWARNING[m] 
[1;31mjava.lang.IllegalArgumentException[m: [1;31mLCD message is too long[m
    [1mat[m io.github.mosser.arduinoml.kernel.generator.ToWiring.visit ([1mToWiring.java:208[m)
    [1mat[m io.github.mosser.arduinoml.kernel.behavioral.Action.accept ([1mAction.java:40[m)
    [1mat[m io.github.mosser.arduinoml.kernel.generator.ToWiring.visit ([1mToWiring.java:123[m)
    [1mat[m io.github.mosser.arduinoml.kernel.behavioral.State.accept ([1mState.java:44[m)
    [1mat[m io.github.mosser.arduinoml.kernel.generator.ToWiring.visit ([1mToWiring.java:66[m)
    [1mat[m io.github.mosser.arduinoml.kernel.App.accept ([1mApp.java:54[m)
    [1mat[m Main.exportToCode ([1mMain.java:54[m)
    [1mat[m Main.main ([1mMain.java:23[m)
    [1mat[m sun.reflect.NativeMethodAccessorImpl.invoke0 ([1mNative Method[m)
    [1mat[m sun.reflect.NativeMethodAccessorImpl.invoke ([1mNativeMethodAccessorImpl.java:62[m)
    [1mat[m sun.reflect.DelegatingMethodAccessorImpl.invoke ([1mDelegatingMethodAccessorImpl.java:43[m)
    [1mat[m java.lang.reflect.Method.invoke ([1mMethod.java:498[m)
    [1mat[m org.codehaus.mojo.exec.ExecJavaMojo$1.run ([1mExecJavaMojo.java:282[m)
    [1mat[m java.lang.Thread.run ([1mThread.java:748[m)
[[1;34mINFO[m] [1m------------------------------------------------------------------------[m
[[1;34mINFO[m] [1;31mBUILD FAILURE[m
[[1;34mINFO[m] [1m------------------------------------------------------------------------[m
[[1;34mINFO[m] Total time:  0.779 s
[[1;34mINFO[m] Finished at: 2024-12-07T10:07:29+01:00
[[1;34mINFO[m] [1m------------------------------------------------------------------------[m
[[1;31mERROR[m] Failed to execute goal [32morg.codehaus.mojo:exec-maven-plugin:1.6.0:java[m [1m(default-cli)[m on project [36mexternal-antlr[m: [1;31mAn exception occured while executing the Java class. LCD message is too long[m -> [1m[Help 1][m
[[1;31mERROR[m] 
[[1;31mERROR[m] To see the full stack trace of the errors, re-run Maven with the [1m-e[m switch.
[[1;31mERROR[m] Re-run Maven using the [1m-X[m switch to enable full debug logging.
[[1;31mERROR[m] 
[[1;31mERROR[m] For more information about the errors and possible solutions, please read the following articles:
[[1;31mERROR[m] [1m[Help 1][m http://cwiki.apache.org/confluence/display/MAVEN/MojoExecutionException
[0m