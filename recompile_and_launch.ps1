rm demo.jar
cd ./src
rm *.class
cd ..
& 'C:\Program Files\Java\jdk-11.0.15\bin\javac.exe' -cp "lib/worldwind.jar;lib/worldwindx.jar;lib/gdal.jar;lib/jogl-all.jar;lib/gluegen-rt.jar;lib/java-json.jar;lib/bridj-0.6.2.jar;lib/slf4j-api-1.7.2.jar;lib/webcam-capture-0.3.12.jar;src/config/layers.xml;src/config/worldwind.xml" src/*.java
cd ./src
jar cvf demo.jar *.class config/*.xml
rm *.class
cd ..
mv ./src/demo.jar .
& 'C:\Program Files\Java\jdk-11.0.15\bin\java.exe' -cp "demo.jar;lib/worldwind.jar;lib/worldwindx.jar;lib/gdal.jar;lib/jogl-all.jar;lib/gluegen-rt.jar;lib/java-json.jar;lib/bridj-0.6.2.jar;lib/slf4j-api-1.7.2.jar;lib/webcam-capture-0.3.12.jar" Demo
