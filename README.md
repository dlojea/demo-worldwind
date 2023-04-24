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


## Datos necesarios para el visualizador

Los datos que necesita el visualizador siguen la siguiente estructura en formato JSON:
```
 {
  "Actitud": "Attitude:pitch=<num>,yaw=<num>,roll=<num>",
  "Elevacion": "<num>",
  "GPS": "GPSInfo:fix=6,num_sat=10",
  "Localizacion Sistema Global": "LocationGlobal:lat=<num>,lon=<num>,alt=<num>",
  "Localizacion Sistema Global Relativo a Home": "LocationGlobalRelative:lat=<num>,lon=<num>,alt=<num>",
  "Localizacion Sistema Local": "LocationLocal:north=<num>,east=<num>,down=<num>",
  "Tiempo": "<datetime>"
 }
```

En la siguiente tabla se especifica el significado de cada uno de los componentes de este objeto JSON:

| ELEMENTO  |  SIGNIFICADO |
|---|---|
|  {  | Delimita el punto de inicio del objeto JSON, a partir del cual se incluyen el conjunto de claves y valores que contiene. |
|  "Actitud"  | Cadena correspondiente a la clave del primer elemento del objeto JSON. Hace referencia a la actitud del UAV, es decir, el ángulo del vehículo en relación a los tres ejes del espacio (cabeceo, alabeo y guiñada). |
|  :  | Indica la separación entre la clave o nombre de un elemento del objeto JSON y su valor. |
|  "Attitude:pitch=\<num>,yaw=\<num>,roll=\<num>"  | El valor del primer elemento del objeto JSON, va encabezado con la palabra "Attitude", que hace referencia a la actitud del UAV, seguido por un delimitador ":" y tres elementos: "pitch", "yaw" y "roll", correspondientes a los tres ángulos que definen la actitud de un UAV. Cada uno de ellos está seguido del símbolo "=", que lo separa de su valor ("\<num>"), que es un número decimal. Cada uno de estos elementos está separado por un delimitador ",". |
|  ,  | Separador entre elementos de un objeto JSON. |
|  "Elevación"  | Cadena correspondiente a la clave del segundo elemento del objeto JSON. Hace referencia a la altitud del UAV, es decir, el número de metros de altura a los que se encuentra respecto al nivel del mar. |
|  "\<num>"  | Representa el valor de la segunda clave del objeto JSON, y es sustituido por un número decimal que representa la altura del UAV. |
|  "GPS"  | Cadena correspondiente a la clave del tercer elemento del objeto JSON. Hace referencia a la información de posición del UAV, basada en el sistema de posicionamiento global o GPS. |
|  "GPSInfo:fix=6,num_sat=10"  | El valor del tercer elemento del objeto JSON, va encabezado con el término "GPSInfo", que hace referencia a la información sobre el sistema GPS, seguido por un delimitador ":" y dos elementos: "fix" y "num_sat", correspondientes respectivamente al método de determinación de la posición del UAV a través del GPS y al número de satélites disponibles. Cada uno de ellos está seguido del símbolo "=", que lo separa de su valor, que es un número entero. Cada uno de estos elementos está separado por un delimitador ",". |
|  "Localizacion Sistema Global"  | Cadena correspondiente a la clave del cuarto elemento del objeto JSON. Hace referencia a la información de posición en relación al sistema global del UAV, basada en el sistema de posicionamiento global o GPS. |
|  "LocationGlobal:lat=\<num>,lon=\<num>,alt=\<num>"  | El valor del cuarto elemento del objeto JSON, va encabezado con el término "LocationGlobal", que hace referencia a la posición global del UAV, seguido por un delimitador ":" y tres elementos: "lat", "lon" y "alt", correspondientes a los tres valores que definen la posición espacial del UAV, su latitud (distancia angular entre la línea ecuatorial y la posición del UAV), su longitud (distancia angular entre la posición del UAV y el meridiano de 0º), y su altitud (el número de metros de altura a los que se encuentra respecto al nivel del mar). Este último dato, la altitud, se obtiene de forma repetida en dos campos. Cada una de estas propiedades está seguida del símbolo "=", que la separa de su valor ("\<num>"), que es un número decimal. Cada uno de estos elementos está separado por un delimitador ",". |
|  "Localizacion Sistema Global Relativo a Home"  | Cadena correspondiente a la clave del quinto elemento del objeto JSON. Hace referencia a la información de posición del UAV en relación a su posición de origen o "Home", basada en el sistema de posicionamiento global o GPS. |
|  "LocationGlobalRelative:lat=\<num>,lon=\<num>,alt=\<num>"  | El valor del quinto elemento del objeto JSON, va encabezado con el término "LocationGlobalRelative", que hace referencia a la posición global del UAV relativa a su posición de origen, seguido por un delimitador ":" y tres elementos: "lat", "lon" y "alt", correspondientes a los tres valores que definen la posición espacial relativa del UAV respecto a su punto de origen, su latitud (diferencia entre la latitud actual del UAV y la latitud de su punto de origen), su longitud (diferencia entre la longitud actual del UAV y la longitud de su punto de origen), y su altitud (la diferencia entre la altitud actual del UAV y la altitud de su punto de origen). Cada una de estas propiedades está seguida del símbolo "=", que la separa de su valor ("\<num>"), que es un número decimal. Cada uno de estos elementos está separado por un delimitador ",". |
|  "Localizacion Sistema Local"  | Cadena correspondiente a la clave del sexto elemento del objeto JSON. Hace referencia a la información de posición relativa del UAV en relación a su posición de origen o "Home", basada la distancia en metros en orientación Norte, Este y Abajo. |
|  "LocationLocal:north=\<num>,east=\<num>,down=\<num>"  | El valor del sexto elemento del objeto JSON, va encabezado con el término "LocationLocal", que hace referencia a la posición relativa del UAV respecto a su posición de origen, seguido por un delimitador ":" y tres elementos: "north", "east" y "down", correspondientes a los tres valores que definen la posición espacial relativa del UAV respecto a su punto de origen, la distancia respecto a su origen en metros en dirección norte, este y abajo (vertical). Cada una de estas propiedades está seguida del símbolo "=", que la separa de su valor ("\<num>"), que es un número decimal. Cada uno de estos elementos está separado por un delimitador ",". |
|  "Tiempo"  | Cadena correspondiente a la clave del séptimo y último elemento del objeto JSON. Hace referencia a la información temporal de los datos recibidos. |
|  "\<datetime>"  | Valor del séptimo y último elemento del objeto JSON, contiene una marca temporal que indica el momento en el que fue obtenida la información contenida en el objeto. |
|  }  | Delimita el punto de finalización del objeto JSON, a partir del cual ya no se encuentra información perteneciente a dicho objeto. |


## Data required for the visualizer

The data needed by the visualizer follows the following structure in JSON format:
```
 {
  "Actitud": "Attitude:pitch=<num>,yaw=<num>,roll=<num>",
  "Elevacion": "<num>",
  "GPS": "GPSInfo:fix=6,num_sat=10",
  "Localizacion Sistema Global": "LocationGlobal:lat=<num>,lon=<num>,alt=<num>",
  "Localizacion Sistema Global Relativo a Home": "LocationGlobalRelative:lat=<num>,lon=<num>,alt=<num>",
  "Localizacion Sistema Local": "LocationLocal:north=<num>,east=<num>,down=<num>",
  "Tiempo": "<datetime>"
 }
```

En la siguiente tabla se especifica el significado de cada uno de los componentes de este objeto JSON:

| ELEMENTO  |  SIGNIFICADO |
|---|---|
|  {  | Delimits the starting point of the JSON object, from which the set of keys and values it contains are included. |
|  "Actitud"  | String corresponding to the key of the first element of the JSON object. It refers to the attitude of the UAV, i.e. the angle of the vehicle relative to the three axes of space (pitch, roll and yaw). |
|  :  | Indicates the separation between the key or name of a JSON object element and its value. |
|  "Attitude:pitch=\<num>,yaw=\<num>,roll=\<num>"  | The value of the first element of the JSON object is headed with the word "Attitude", which refers to the attitude of the UAV, followed by a delimiter ":" and three elements: "pitch", "yaw" and "roll", corresponding to the three angles that define the attitude of a UAV. Each of them is followed by the symbol "=", which separates it from its value ("\<num>"), which is a decimal number. Each of these elements is separated by a delimiter ",". |
|  ,  | Separator between elements of a JSON object. |
|  "Elevación"  | String corresponding to the key of the second element of the JSON object. It refers to the altitude of the UAV, i.e. the number of meters above sea level. |
|  "\<num>"  | It represents the value of the second key of the JSON object, and is replaced by a decimal number representing the height of the UAV. |
|  "GPS"  | String corresponding to the key of the third element of the JSON object. It refers to the position information of the UAV, based on the global positioning system or GPS. |
|  "GPSInfo:fix=6,num_sat=10"  | The value of the third element of the JSON object is headed with the term "GPSInfo", which refers to information about the GPS system, followed by a delimiter ":" and two elements: "fix" and "num_sat", corresponding respectively to the method of determining the position of the UAV through GPS and the number of available satellites. Each of them is followed by the symbol "=", which separates it from its value, which is an integer. Each of these elements is separated by a delimiter ",". |
|  "Localizacion Sistema Global"  | String corresponding to the key of the fourth element of the JSON object. It refers to the position information in relation to the global system of the UAV, based on the global positioning system or GPS. |
|  "LocationGlobal:lat=\<num>,lon=\<num>,alt=\<num>"  | The value of the fourth element of the JSON object, is headed with the term "LocationGlobal", which refers to the global position of the UAV, followed by a ":" delimiter and three elements: "lat", "lon" and "alt", corresponding to the three values that define the spatial position of the UAV, its latitude (angular distance between the equatorial line and the UAV's position), its longitude (angular distance between the UAV's position and the 0° meridian), and its altitude (the number of meters in height at which it is located with respect to sea level). This last data, the altitude, is obtained repeatedly in two fields. Each of these properties is followed by the symbol "=", which separates it from its value ("\<num>"), which is a decimal number. Each of these elements is separated by a delimiter ",". |
|  "Localizacion Sistema Global Relativo a Home"  | String corresponding to the key of the fifth element of the JSON object. It refers to the position information of the UAV in relation to its home position, based on the global positioning system or GPS. |
|  "LocationGlobalRelative:lat=\<num>,lon=\<num>,alt=\<num>"  | The value of the fifth element of the JSON object, is headed with the term "LocationGlobalRelative", which refers to the global position of the UAV relative to its home position, followed by a ":" delimiter and three elements: "lat", "lon" and "alt", corresponding to the three values that define the relative spatial position of the UAV relative to its point of origin, its latitude (difference between the UAV's current latitude and the latitude of its point of origin), its longitude (difference between the UAV's current longitude and the longitude of its point of origin), and its altitude (the difference between the UAV's current altitude and the altitude of its point of origin). Each of these properties is followed by the symbol "=", which separates it from its value ("\<num>"), which is a decimal number. Each of these elements is separated by a delimiter ",". |
|  "Localizacion Sistema Local"  | String corresponding to the key of the sixth element of the JSON object. Refers to the relative position information of the UAV in relation to its home position, based on the distance in meters in North, East and Down orientation. |
|  "LocationLocal:north=\<num>,east=\<num>,down=\<num>"  | The value of the sixth element of the JSON object is headed with the term "LocationLocal", which refers to the relative position of the UAV with respect to its position of origin, followed by a delimiter ":" and three elements: "north", "east" and "down", corresponding to the three values that define the relative spatial position of the UAV with respect to its point of origin, the distance from its origin in meters in the north, east and down (vertical) direction. Each of these properties is followed by the symbol "=", which separates it from its value ("\<num>"), which is a decimal number. Each of these elements is separated by a delimiter ",". |
|  "Tiempo"  | String corresponding to the key of the seventh and last element of the JSON object. It refers to the temporal information of the received data. |
|  "\<datetime>"  | Value of the seventh and last element of the JSON object, it contains a timestamp indicating the time at which the information contained in the object was obtained. |
|  }  | Delimits the end point of the JSON object, after which information pertaining to that object is no longer found. |