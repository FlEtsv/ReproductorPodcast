package com.universae.reproductor.domain.usecases

import com.android.navegacion.domain.Sesion
import com.universae.reproductor.domain.entities.alumno.Alumno

interface AlumnoUseCase {
    fun getAlumno(nombreUsuario: String, clave: String): Alumno?
}

object AlumnoUseCaseImpl : AlumnoUseCase {
    override fun getAlumno(nombreUsuario: String, clave: String): Alumno? {
        Sesion.iniciarSesion(nombreAlumno = nombreUsuario, clave = clave)
        return if (Sesion.sesionIniciada) Sesion.alumno else null
    }

}