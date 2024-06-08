package com.universae.domain.entities.alumno

/**
 * Interfaz para el repositorio de alumnos.
 * Esta interfaz define las operaciones que se pueden realizar en un repositorio de alumnos.
 */
interface AlumnoRepository {

    /**
     * Obtiene un alumno por su nombre de usuario y clave hash.
     *
     * @param nombreUsuario El nombre de usuario del alumno.
     * @param claveHash La clave hash del alumno.
     * @return El alumno si se encuentra, null en caso contrario.
     */
    fun getAlumno(nombreUsuario: String, claveHash: String): Alumno?
}