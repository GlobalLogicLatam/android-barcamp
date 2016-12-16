#BarCamp

BarCamp MDQ, desarrollada por GlobalLogic Latinoamérica, replica la clásica pizarra del evento para que puedas consultar online las charlas que se suman, ¡en tiempo real y sin moverte de la sala! Además, realiza un seguimiento del evento en Twitter para que no te pierdas ni una conversación.

BarCamp es un open space que reune a estudiantes, profesionales y geeks apasionados por el desarrollo de software, ingeniería, comunicaciones y afines, con el propósito de intercambiar experiencias y conocimientos, mediante la realización de charlas, talleres y debates. La agenda es espontánea y planeada entre todos al inicio del evento.

Support: https://www.globallogic.com/latam/contact-us/

## Configuración Inicial

###Registrar instancia en Firebase

1. Crea un proyecto de Firebase en el [Firebase console](https://firebase.google.com/console/) , si aún no tienes uno. Si ya tienes un proyecto de Google asociado con tu app para dispositivos móviles, haz clic en **Import Google Project**. De lo contrario, haz clic en **Create New Project**.
2. Haz clic en **Add Firebase to your Android app** y sigue los pasos de configuración. Si estás importando un proyecto de Google existente, esto puede realizarse automáticamente y podrás descargar el archivo de configuración.
3. Cuando se te indique, ingresa el nombre del paquete de tu app. Es importante ingresar el nombre del paquete que está usando tu app; esto solo se puede configurar cuando agregas una app a tu proyecto de Firebase.
4. Al final, descargarás un archivo `google-services.json`. Puedes [descargar este archivo](http://support.google.com/firebase/answer/7015592) nuevamente en cualquier momento.

Busca más información en: https://firebase.google.com/docs/android/setup


###Crear archivo de configuración local

Siempre es recomendable tener un archivo local de configuración en el que tendrás almacenado credenciales, claves, contraseñas, etc.
Para este proyecto, se puede crear un archivo con el siguiente formato, y ubicarlo en el root del proyecto:

```properties
twitter.key=[YOUR_TWITTER_KEY]
twitter.secret=[YOUR_TWITTER_SECRET]
crashlytics.apikey=[YOUR_CRASHLYTICS_API_KEY]
facebook.app.id=[YOUR_FACEBOOK_APP_ID]

firebase.root.release=[YOUR_FIREBASE_RELEASE_URL]
firebase.root.beta=[YOUR_FIREBASE_BETA_URL]
firebase.root.debug=[YOUR_FIREBASE_DEBUG_URL]
```

Puedes crear tus propias keys desde estos lugares:
. [Twitter](https://docs.fabric.io/android/twitter/installation.html)
. [Crashlytics](https://docs.fabric.io/android/crashlytics/installation.html)
. [Facebook](https://github.com/facebook/facebook-android-sdk)