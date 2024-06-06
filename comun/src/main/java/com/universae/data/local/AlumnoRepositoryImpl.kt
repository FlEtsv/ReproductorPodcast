package com.universae.data.local


import com.universae.reproductor.domain.entities.alumno.Alumno
import com.universae.reproductor.domain.entities.alumno.AlumnoRepository
import com.universae.data.local.dataexample.PreviewAlumno

object AlumnoRepositoryImpl : AlumnoRepository {

    override fun getAlumno(nombreUsuario: String, claveHash: String): Alumno? {
        return if (PreviewAlumno.filter { it.nombreUsuario == nombreUsuario }.isNotEmpty()) {
            PreviewAlumno.filter { it.nombreUsuario == nombreUsuario }[0]
        } else {
            null
        }
    }

    fun getAlumnoById(alumnoId: Int): Alumno? {
        return if (PreviewAlumno.filter { it.alumnoId.id == alumnoId }.isNotEmpty()) {
            PreviewAlumno.filter { it.alumnoId.id == alumnoId }[0]
        } else {
            null
        }
    }
}