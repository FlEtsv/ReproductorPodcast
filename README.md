# Navegación Universae

Este proyecto es una aplicación de reproducción de podcasts desarrollada en Kotlin y Java utilizando Gradle como sistema de construcción. La interfaz de usuario se presenta utilizando Jetpack Compose, un moderno kit de herramientas de IU para Android, y el servicio multimedia de audio se implementa utilizando Media3.

## Características pendientes

A continuación se enumeran las características que aún están pendientes de implementación:

- Marcar un tema como escuchado: Cuando un usuario escucha un tema, este debería marcarse como escuchado.
- Filtrar tarjetas y sugerencias por grado: Al hacer clic en la tarjeta de un grado, solo se deberían mostrar las tarjetas y sugerencias correspondientes a ese grado, en lugar de todas a las que tiene acceso el alumno.
- Implementar barra de progreso del player: Falta hacer que la barra represente la duración total del tema que está reproduciendo y que dentro de esa barra marque lo que sería la currentPosition. En el contexto de un reproductor multimedia, la `currentPosition` se refiere a la posición actual de reproducción en la pista de audio.
- Mejorar la funcionalidad de temas recientes en Android Auto: Actualmente, en Android Auto se muestra una rama de temas recientes que solo incluye un único tema, que es el último tema escuchado en el reproductor. Sería conveniente mejorar esta funcionalidad convirtiéndola en una lista y que elimine el elemento de la lista cuando se completa la reproducción del mismo.

## Características

- Autenticación de alumnos: Los alumnos pueden autenticarse en la aplicación para acceder a contenido exclusivo. Si la aplicación no consigue una autenticación exitosa en 10 segundos, esta regresa a la vista del log in.
- Temas sugeridos: Se muestra el primer tema no escuchado de cada una de las asignaturas a las que tiene acceso el alumno.
- Reproducción de podcasts: Permite a los usuarios escuchar sus podcasts favoritos. Al reproducir un tema, este se carga como parte de una playlist correspondiente a todos los temas de la asignatura.
- Avance y retroceso en la pista actual: Los usuarios pueden avanzar o retroceder 10 segundos en la pista que están escuchando.
- Pasar a la siguiente pista: Los usuarios pueden saltar a la siguiente pista en la lista de reproducción.
- Compartir (en desarrollo): Esta característica permitirá a los usuarios compartir con otros medios.

## Estructura del proyecto

El proyecto está dividido en varios módulos:

- `app`: Contiene la lógica de la interfaz de usuario y la navegación. Este módulo es responsable de la interacción del usuario con la aplicación. La interfaz de usuario se presenta utilizando Jetpack Compose.
- `comun`: Contiene la implementación del repositorio de alumnos. Este módulo gestiona la autenticación y el acceso a los datos de los alumnos. Actualmente, el proyecto simula todo el acceso a datos a partir de un archivo Kotlin llamado `PreviewData`. Además, tanto las imágenes como los archivos mp3 que se reproducen en la aplicación se encuentran almacenados en Dropbox.
- `mylibrary`: Este módulo implementa el servicio `MusicService` utilizando Media3. Este servicio es responsable de la reproducción de música en la aplicación. Gestiona la reproducción de pistas, el avance rápido, el retroceso y el salto a la siguiente pista. Además, gestiona el árbol de elementos (BrowseTree) para la representación y navegación de los mismos en Android Auto.

## Cómo ejecutar el proyecto

1. Clona este repositorio: Puedes hacerlo utilizando el comando `git clone`.
2. Abre el proyecto en Android Studio Jellyfish | 2023.3.1: Asegúrate de tener instalada la versión correcta de Android Studio.
3. Para ejecutar el proyecto en un emulador de Android Studio:
   - Asegúrate de tener un emulador configurado. Si no lo tienes, puedes crear uno en AVD Manager.
   - Inicia el emulador.
   - Ejecuta el proyecto en el emulador: Puedes hacerlo utilizando el botón de ejecución en Android Studio.
4. Para ejecutar el proyecto en un dispositivo Android:
   - Conecta tu dispositivo Android a tu computadora: Asegúrate de que tu dispositivo esté en modo de depuración USB.
   - Ejecuta el proyecto en tu dispositivo Android: Puedes hacerlo utilizando el botón de ejecución en Android Studio.
5. Para simular Android Auto:
   - Descarga e instala la aplicación Headunit.
   - En tu dispositivo, ve a Ajustes > Aplicaciones > Ver todas las aplicaciones > Android Auto > Avanzado > Configuración adicional en la aplicación > Versión del desarrollador.
   - Toca varias veces la versión hasta que te conviertas en desarrollador.
   - Luego ve a los tres puntos en la esquina superior derecha y selecciona "Iniciar Headunit Server".
   - Abre una terminal y ejecuta el siguiente comando para realizar el port forwarding: `adb forward tcp:5277 tcp:5277`
   - Finalmente, inicia la aplicación Headunit y selecciona "Iniciar servidor Headunit".
