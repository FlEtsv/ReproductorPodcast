package com.universae.data.local

import com.universae.data.local.dataexample.PreviewAlumno
import com.universae.domain.entities.alumno.Alumno
import com.universae.domain.entities.alumno.AlumnoRepository

/**
 * Implementación del repositorio de alumnos.
 * Esta clase se encarga de manejar las operaciones de los alumnos en la base de datos local.
 */
object AlumnoRepositoryImpl : AlumnoRepository {

    /**
     * Obtiene un alumno por su nombre de usuario y clave hash.
     *
     * @param nombreUsuario El nombre de usuario del alumno.
     * @param claveHash La clave hash del alumno.
     * @return El alumno si se encuentra, null en caso contrario.
     */
    override fun getAlumno(nombreUsuario: String, claveHash: String): Alumno? {
        return if (PreviewAlumno.filter { it.nombreUsuario == nombreUsuario }.isNotEmpty()) {
            PreviewAlumno.filter { it.nombreUsuario == nombreUsuario }[0]
        } else {
            null
        }
    }

    /**
     * Obtiene un alumno por su ID.
     *
     * @param alumnoId El ID del alumno.
     * @return El alumno si se encuentra, null en caso contrario.
     */
    fun getAlumnoById(alumnoId: Int): Alumno? {
        return if (PreviewAlumno.filter { it.alumnoId.id == alumnoId }.isNotEmpty()) {
            PreviewAlumno.filter { it.alumnoId.id == alumnoId }[0]
        } else {
            null
        }
    }
}