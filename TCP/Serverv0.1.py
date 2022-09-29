from socketserver import BaseRequestHandler, TCPServer

from time import time

from TCP_coordenadas import TCP_coordenadas

from dronekit import connect

import pandas as pd

import json
import random



tiempo_inicio = time()

frecuencia_refresco = 3



class RequestHandler (BaseRequestHandler):



    def handle(self):

        print("Se ha recibido una conexion desde {}".format(self.client_address))

        tiempo_guardado_previo = time()

        tiempo_inicio = time()

        vehicle = connect("127.0.0.1:14551", wait_ready=True)


        LocSistGlobalTemplate = "LocationGlobal:lat={LAT},lon={LON},alt={ALT}"
        ActTemplate = "Attitude:pitch={PITCH},yaw={YAW},roll={ROLL}"
	
        LOC_SIST_GLOBAL = "Localizacion Sistema Global"
        ACTITUD = "Actitud"
        ELEVACION = "Elevacion"
        
        firstData = True


        while True:

            pt,tiempo_guardado_previo = data(tiempo_guardado_previo, frecuencia_refresco, vehicle)



            if not pt.empty:

               rec = pt.to_json(orient = 'records')
               datos = json.JSONDecoder().decode(rec)[-1]
            	
               lat, lon, alt = [float(datos[LOC_SIST_GLOBAL].replace(",", "=").split("=")[i]) for i in range(0,6) if i%2 == 1]
               pitch, yaw, roll = [float(datos[ACTITUD].replace(",", "=").split("=")[i]) for i in range(0,6) if i%2 == 1]
            	
               # http://wiki.gis.com/wiki/index.php/Decimal_degrees
               if firstData:
                       POS_NOISE = 0
                       ATT_NOISE = 0
                       ALT_NOISE = 0
                       firstData = False
               else:
                       POS_NOISE = 0.00001*4
                       ATT_NOISE = 0.01*4
                       ALT_NOISE = 150
            	
               lat += random.uniform(-POS_NOISE, POS_NOISE)
               lon += random.uniform(-POS_NOISE, POS_NOISE)
               alt += random.uniform(-ALT_NOISE, ALT_NOISE)
            	
               pitch += random.uniform(-ATT_NOISE, ATT_NOISE)
               yaw += random.uniform(-ATT_NOISE, ATT_NOISE)
               roll += random.uniform(-ATT_NOISE, ATT_NOISE)
            	
               datos[LOC_SIST_GLOBAL] = LocSistGlobalTemplate.replace("{LAT}", str(lat)).replace("{LON}", str(lon)).replace("{ALT}", str(alt))
               datos[ACTITUD] = ActTemplate.replace("{PITCH}", str(pitch)).replace("{YAW}", str(yaw)).replace("{ROLL}", str(roll))
               datos[ELEVACION] = str(alt)
            	

               message = json.dumps(datos)
            	

               print('Message:')

               print(message)

   

               self.request.sendall(bytes(message,encoding="utf-8"))

            

            #if time() - tiempo_inicio > 120:

            #    print('Se ha completado la recepción de datos')

            #    break

        





        #return super().handle()





def data(tiempo_guardado_previo, frecuencia_refresco, vehicle):



    if time() - tiempo_guardado_previo > 1 / frecuencia_refresco:

        pt = pd.DataFrame(TCP_coordenadas(vehicle))

        tiempo_guardado_previo = time()

    else:

        pt = pd.DataFrame()



    return pt,tiempo_guardado_previo



    



if __name__ == '__main__':

    server = TCPServer(('',20064), RequestHandler)



    print('El servidor se ha iniciado')

    server.serve_forever()

