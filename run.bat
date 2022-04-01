@echo off
REM Default to the ApplicationTemplate example if a class name is not provided
IF "%1"=="" (SET WWDEMO=Demo) ELSE (SET WWDEMO=%1)

REM Run a WorldWind Demo
@echo Running %WWDEMO%
java -cp demo.jar;lib/worldwind.jar;lib/worldwindx.jar;lib/gdal.jar;lib/jogl-all.jar;lib/gluegen-rt.jar;lib/java-json.jar %WWDEMO%