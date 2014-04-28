![alt tag](https://github.com/mikesaurio/trackxi/raw/master/Trackxi/res/drawable-hdpi/ic_launcher.png) TRAXI
============
Desarrollada en Eclipse Kepler y ADT para android 4.0 y superiores

App que te auxilia en a la hora de tomar un taxi del Distrito Federal (DF), puedes tomar una foto al número de placa que esta en el tablero o en la puerta de un taxi o bien ingresarla manualmente, se te mostrarán los datos del vehículo ademas de un termómetro de confianza:

Dado por la app:
* revista vehicular
* infracción de tránsito
* tenencia vehicular
* año del taxi
* verificación vehicular

Dado por los usuarios:
* Calificación de 0 a 5 
* Comentario
                
Adicionalmente puedes ver quien de tus amigos de Facebook han tomado ese taxi y sus calificaciones.

# REGISTRO

La aplicacion pide un nombre de usuario, obtiene el correo enlazado al telefono, y pide 2 contactos de emergencia para ambos casos es telefono celular y correo electrónico, puedes poner una foto de perfil o una imagen de la galería, esta última opción no es obligatoria.

En menu -> Cuenta
Podras editar en todo momento tus contactos de emergencia y tu imagen de perfil, pero no podras cambiar ni tu correo ni tu usuario.

# NIVELES DE SEGURIDAD:

* Una vez iniciado el viaje la app te rastreara cada 5 segundos, 1 minuto, 2 minutos o 5 minutos dependiendo lo configures.
* Botón de pánico, durante tu viaje y sólo durante tu viaje puedes apretar 5 veces el botón de encendido del smartphone y se enviara un SMS a tu contacto de emergencia indicándole que revise su correo y n correos electrónicos mostrando tu ubicación y nivel de batería.
* Modo paranoico, este esta desactivado por defecto per se puede hacer que te estén llegando notificaciones preguntando si te encuentras bien!, de no contestarla se envía un SMS a tu contacto de emergencia indicandole que revise su correo y n correos electrónicos mostrando tu ubicación y nivel de batería.

# MAPA

Durante el viaje el usuario puede ver un mapa (googleMaps) donde ve el camino que a recorrido y puede poner el destino, si hace esto se mostrara la ruta posible que puede tomar el taxi, la distancia y el tiempo que le falta por recorrer.

# CALIFICACIÓN

Al final del viaje el usuario puede calificar el servicio del 0 al 5 y puede hacer un comentario de 50 caracteres.

# CONFIGURACIÓN

Podrás modificar la frecuencia con la que el telefono te rastrea, activar y decidir el tiempo de avisos en el modo paranoico, activar o desactivar si deseas enviar el reporte de fallos en caso que la app falle, ver el acerca de la aplicación.


# IDIOMAS

La aplicación tiene soporte para idiomas español e ingles.

# Pantallas

<p align="center">
  <img src="https://github.com/mikesaurio/trackxi/raw/master/screenshot/Screenshot_2014-04-28-13-32-02.png?raw=true" alt="Splash"/>
    <img src="https://github.com/mikesaurio/trackxi/raw/master/screenshot/Screenshot_2014-04-28-13-32-02.png?raw=true" alt="Splash"/>
      <img src="https://github.com/mikesaurio/trackxi/raw/master/screenshot/Screenshot_2014-04-28-13-32-02.png?raw=true" alt="Splash"/>
        <img src="https://github.com/mikesaurio/trackxi/raw/master/screenshot/Screenshot_2014-04-28-13-32-02.png?raw=true" alt="Splash"/>
</p>



# LIBRERIAS EXTERNAS REQUERIDAS

    google_play_services_lib
    FacebookSDK

AYUDA PARA AGREGAR google_play_services_lib
    seleccionar New-> Project -> Android Application Project from Existing Code
    dar doble click en la ruta - "android-sdk\extras\google\google_play_services"
    
AYUDA PARA AGREGAR FacebookSDK
    se encuentra en https://github.com/facebook/facebook-android-sdk


# LIBRERIAS INTERNAS

    acra-4.5.jar
    activation.jar
    additionnal.jar
    apache-mime4j-core-0.7.2.jar
    httpclient-4.3.1.jar
    httpcore-4.3.jar
    httpmime-4.3.1.jar
    mail.jar
    socketio.jar
    
Estas se encuentran en la carpeta libs del proyecto.


# ¿QUIÉRES PROBAR LA APP?

Estamos en pruebas BETA así que debes enviar un correo (gmail preferentemente) a miguel.moran@codigo.labplc.mx una vez agregado al grupo TRAXI podras descargarla de aqui [Traxi Beta](https://play.google.com/apps/testing/codigo.labplc.mx.trackxi)




# DUDAS

    @yosoymikesaurio
    miguel.moran@codigo.labplc.mx
    http://www.traxi.mx
