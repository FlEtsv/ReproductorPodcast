package com.universae.domain.usecases

import com.universae.domain.Sesion
import com.universae.domain.entities.alumno.Alumno

/**
 * Interfaz para los casos de uso del alumno.
 * Esta interfaz define las operaciones que se pueden realizar en un caso de uso del alumno.
 */
interface AlumnoUseCases {
    /**
     * Obtiene un alumno por su nombre de usuario y clave.
     *
     * @param nombreUsuario El nombre de usuario del alumno.
     * @param clave La clave del alumno.
     * @return El alumno si se encuentra, null en caso contrario.
     */
    fun getAlumno(nombreUsuario: String, clave: String): Alumno?

    /**
     * Obtiene un alumno por su identificador único.
     *
     * @param id El identificador único del alumno.
     * @return El alumno si se encuentra, null en caso contrario.
     */
    fun getAlumnoById(id: Int): Alumno?
}

/**
 * Implementación de la interfaz AlumnoUseCase.
 * Esta clase define las operaciones que se pueden realizar en un caso de uso del alumno.
 */
object AlumnoUseCasesImpl : AlumnoUseCases {
    /**
     * Obtiene un alumno por su nombre de usuario y clave.
     * Inicia una sesión si el alumno se encuentra.
     *
     * @param nombreUsuario El nombre de usuario del alumno.
     * @param clave La clave del alumno.
     * @return El alumno si se encuentra, null en caso contrario.
     */
    override fun getAlumno(nombreUsuario: String, clave: String): Alumno? {
        Sesion.iniciarSesion(nombreAlumno = nombreUsuario, clave = clave)
        return if (Sesion.sesionIniciada) Sesion.alumno else null
    }

    /**
     * Obtiene un alumno por su identificador único.
     * Verifica si la sesión está iniciada y si el identificador del alumno coincide con el proporcionado.
     *
     * @param id El identificador único del alumno.
     * @return El alumno si se encuentra, null en caso contrario.
     */
    override fun getAlumnoById(id: Int): Alumno? {
        return if (Sesion.sesionIniciada && Sesion.alumno.alumnoId.id == id) Sesion.alumno else null
    }
}