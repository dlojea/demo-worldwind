@echo off
IF "%1"=="" (SET WWDEMO=Demo) ELSE (SET WWDEMO=%1)

@echo Running %WWDEMO%
java -cp demo.jar;lib/worldwind.jar;lib/worldwindx.jar;lib/gdal.jar;lib/jogl-all.jar;lib/gluegen-rt.jar;lib/java-json.jar %WWDEMO%