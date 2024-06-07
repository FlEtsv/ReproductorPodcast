# Navegación Universae

Este proyecto es una aplicación de reproducción de podcasts desarrollada en Kotlin y Java utilizando Gradle como sistema de construcción. La interfaz de usuario se presenta utilizando Jetpack Compose, un moderno kit de herramientas de IU para Android, y el servicio multimedia de audio se implementa utilizando Media3.

## Características

- Reproducción de podcasts: Permite a los usuarios escuchar sus podcasts favoritos.
- Avance rápido y retroceso en la pista actual: Los usuarios pueden avanzar o retroceder en la pista que están escuchando.
- Pasar a la siguiente pista: Los usuarios pueden saltar a la siguiente pista en la lista de reproducción.
- Compartir (en desarrollo): Esta característica permitirá a los usuarios compartir con otros medios.
- Autenticación de alumnos: Los alumnos pueden autenticarse en la aplicación para acceder a contenido exclusivo.

## Estructura del proyecto

El proyecto está dividido en varios módulos:

- `app`: Contiene la lógica de la interfaz de usuario y la navegación. Este módulo es responsable de la interacción del usuario con la aplicación. La interfaz de usuario se presenta utilizando Jetpack Compose.
- `comun`: Contiene la implementación del repositorio de alumnos. Este módulo gestiona la autenticación y el acceso a los datos de los alumnos.
- `mylibrary`: Este módulo implementa el servicio `MusicService` utilizando Media3. Este servicio es responsable de la reproducción de música en la aplicación. Gestiona la reproducción de pistas, el avance rápido, el retroceso y el salto a la siguiente pista.

## Cómo ejecutar el proyecto

1. Clona este repositorio: Puedes hacerlo utilizando el comando `git clone`.
2. Abre el proyecto en Android Studio Jellyfish | 2023.3.1: Asegúrate de tener instalada la versión correcta de Android Studio.
3. Ejecuta el proyecto en un emulador o dispositivo Android: Puedes hacerlo utilizando el botón de ejecución en Android Studio.