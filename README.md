# FIREPOCTEP: SISTEMA DE AYUDA AL PILOTAJE DE UAVS (FIREPOCTEP: UAVS PILOTING ASSISTANCE SYSTEM)

### Resumen del proyecto

El **sistema de ayuda al pilotaje de UAVs en condiciones de fuego nocturno**, se
basa en la superposición de imágenes cartográficas —proyectadas a partir de
informaciones satelital (posición), e inercial (actitud)—, sobre imágenes obtenidas por las
cámaras termográficas embarcadas.

Una de las grandes dificultades que sufre el personal de emergencias (112) que trabajan en
la vigilancia del frente de fuego, es el seguimiento de dicho frente en condiciones de baja o
nula visibilidad. Cuando se inspecciona la zona bajo incendio, especialmente de noche, se
busca poder proporcionar información de evolución del mismo (en términos de dirección,
amplitud y velocidad del frente). En estos casos es habitual el empleo de aeronaves no
tripuladas (UAV), en las que se embarcan diferentes tipos de cámaras que permiten a un
operador pilotarlas y observar el fuego con el fin de determinar las características
mencionadas. En el caso de incendios nocturnos, el piloto suele tener problemas para
operar la aeronave: se producen elevados contrastes entre el entorno de oscuridad (debido
a la noche), y la intensa luz que emana del propio incendio. Este subproyecto trata de
subsanar estos problemas proporcionando una **capa de ayuda que se superponga
sobre la imagen de la cámara proporcionando detalles cartográficos y de
relieve del terreno que sobrevuela el UAV**, a partir de datos satelitales y aquellos
que proporciona el propio UAV.

### Project summary

The **UAV piloting aid system in night fire conditions** is based on the superposition
of cartographic images - projected from satellite information (position) and inertial
information (attitude) - over images obtained by on-board thermal imagers.

One of the major difficulties faced by the emergency personnel working in fire front
surveillance is the monitoring of the fire front in conditions of low or zero visibility. When
inspecting the area affected by fire, especially at night, the objective is to provide
information about the evolution of the fire (in terms of direction, amplitude and speed of
spreading). In these cases it is common to use unmanned aerial vehicles (UAV), which
carry different types of cameras that allow an operator to fly them and observe the fire in
order to determine the aforementioned characteristics. In the case of night fires, the pilot
usually encounters difficulties when operating the aircraft: there are high contrasts
between the dark environment (due to the night), and the intense light emanating from the
fire itself. This subproject tries to overcome these problems by providing **an aid layer
that is overlapped on top of the camera image, providing cartographic and
topographic details of the terrain covered by the UAV**, based on satellite data and
information provided by the UAV itself.

## Estructura de ficheros

El proyecto se divide en la siguiente estructura de ficheros y directorios:

- ``TCP``: Directorio que contiene el código necesario para lanzar una versión simulada de un UAV 
haciendo uso de dronekit y mavproxy. Los requisitos y pasos para la instalación y uso de este
simulador se encuentran en el fichero ``TCP/Instrucciones de instalación y uso del servidor
TCP y SITL.docx``.

- ``bin``: Directorio que almacena el bytecode compilado del proyecto de visualizador Java.

- ``lib``: Directorio que contiene las dependencias del proyecto de visualizador Java, en formato de
ficheros ``jar``.

- ``servidor de prueba``: Directorio que contiene el código Java para construir un simulador de UAV
sin necesidad del sistema basado en dronekit y mavproxy (directorio ``TCP``).

- ``src``: Directorio del código principal del proyecto, correspondiente al sistema de visualización.

- ``demo.jar``: Versión compilada, empaquetada y ejecutable del sistema de visualización.

- ``run.bat``: Fichero para ejecutar el sistema de visualización en equipos Windows.

- ``run.sh``: Fichero para ejecutar el sistema de visualización en equipos Linux.

- ``README.md``: Fichero que contiene la descripción del proyecto y sus ficheros. 

- ``INSTALLATION.md``: Fichero que explica el proceso de instalación del proyecto.

## File structure

The project is divided into the following file and directory structure:

- ``TCP``: Directory containing the code necessary to launch a simulated version of a UAV 
using dronekit and mavproxy. The requirements and steps for the installation and use of this 
simulator can be found in the file ``TCP/Instrucciones de instalación y uso del servidor
TCP y SITL.docx``.

- ``bin``: Directory that stores the compiled bytecode of the Java viewer project.

- ``lib``: Directory containing the Java viewer project dependencies, in the form of ``jar`` files.

- ``servidor de prueba``: Directory containing the Java code to build a UAV simulator without the need of the 
dronekit and mavproxy based system (``TCP`` directory).

- ``src``: Directory of the main code of the project, corresponding to the visualization system.

- ``demo.jar``: Compiled, packaged and executable version of the visualization system.

- ``run.bat``: File to run the visualization system on Windows computers.

- ``run.sh``: File to run the visualization system on Linux computers.

- ``README.md``: File that contains the project and its files description.

- ``INSTALLATION.md``: File that explains the project installation process.
