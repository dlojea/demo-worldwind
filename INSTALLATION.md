# FIREPOCTEP: PROCESO DE INSTALACIÓN

## Requisitos básicos de instalación:

### Sistema de visualización:

- Sistema Operativo:
  - Windows 10 o superior.
  - Ubuntu 20.04 LTS o superior.

- Java SE 17 o superior (JDK necesario para recompilar el código).
  - Las dependencias necesarias se incluyen en el directorio ``lib``.

### Sistema de simulación:
- Sistema Operativo:
  - Windows 10 o superior (versión para Windows: ``servidor de prueba``).
  - Ubuntu 20.04 LTS o superior (versión para Linux: ``TCP``, basado en mavproxy
    y dronekit).

- Java SE 17 o superior (JDK necesario para recompilar el código, en el caso de
  Windows).

- Las dependencias para la versión de Linux se especifican en el fichero
``TCP/Instrucciones de instalación y uso del servidor TCP y SITL.docx``.

## Uso del proyecto:

1. Una vez descargado el directorio principal del proyecto, acceda a él.
2. Si se encuentra en un sistema Linux, siga las instrucciones del fichero
``TCP/Instrucciones de instalación y uso del servidor TCP y SITL.docx`` para
lanzar el simulador de UAV.
3. En otro caso, utilice el sistema disponible en el directorio ``servidor de
prueba`` para lanzar el simulador debe ejecutarse el fichero DroneServer.jar
(por defecto, el servidor de prueba envía los datos al puerto 20064). La ruta
simulada puede encontrarse en el fichero ``demo.path`` (cada fila incluye las
coordenadas y la orientación del dron en un instante determinado, especificado
en milisegundos).
4. Una vez lanzado el simulador de UAV, vuelva al directorio principal del
   proyecto.
5. Ejecute el fichero ``run`` correspondiente para su sistema operativo
(``run.sh`` para Linux, ``run.bat`` para Windows).
6. Una vez ejecutado el fichero, se mostrará la visualización del sistema.
7. En el menú superior pueden modificarse las capas visualizadas (menú
``Capas``), modificar el ángulo de la cámara del dron (menú ``Opciones``), o
cambiar el dron visualizado (menú ``Cambiar dron``).
8. Adicionalmente, puede modificarse la transparencia de la capa de vídeo (en
caso de que esté disponible una cámara) usando el slider de ``Transparencia``,
así como ajustar el retardo de la visualización respecto a las imágenes de vídeo
con el slider ``Sincronizar retardo imagen``.


# FIREPOCTEP: INSTALLATION PROCESS

## Basic installation requirements:

### Visualization System:

- Operating System:
  - Windows 10 or higher.
  - Ubuntu 20.04 LTS or higher.

- Java SE 17 or higher (JDK required to recompile the code).
  - Necessary dependencies are included in the ``lib`` directory.

### Simulation system:
- Operating System:
  - Windows 10 or higher (Windows version: ``test server``).
  - Ubuntu 20.04 LTS or higher (Linux version: ``TCP``, based on mavproxy and
    dronekit).

- Java SE 17 or higher (JDK needed to recompile the code, in case of Windows).

- The dependencies for the Linux version are specified in the `TCP/Instrucciones
de instalación y uso del servidor TCP y SITL.docx``.

## Using the project:

1. After downloading the main project directory, access it.
2. If you are on a Linux system, follow the instructions in the file
``TCP/Instrucciones de instalación y uso del servidor TCP y SITL.docx``. TCP and
SITL server installation and usage.docx`` to launch the UAV simulator.
3. Otherwise, use the available system in the ``test server`` directory to
   launch the simulator using the DroneServer.jar file (by default, the
   simulator sends data into the 20064 port). The simulated route can be found
   in the ``demo.path`` file (each row includes the coordinates and orientation
   of the drone at a given time, specified in milliseconds).
4. Once the UAV simulator is launched, return to the main project directory.
5. Run the corresponding ``run`` file for your operating system (``run.sh`` for
Linux, ``run.bat`` for Windows).
6. Once the file is executed, the system display will be shown.
7. In the top menu you can change the displayed layers (menu ``Capas``), change
the angle of the drone camera (menu ``Opciones``), or change the displayed drone
(menu ``Cambiar dron``).
8. Additionally, you can modify the transparency of the video layer (in case a
camera is available) using the ``Transparencia`` slider, as well as adjusting
the delay of the display with respect to the video images with the ``Sincronizar
retardo imagen`` slider.
