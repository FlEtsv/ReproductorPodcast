package com.universae.reproductor.domain.entities.alumno

interface AlumnoRepository {
    fun getAlumno(nombreUsuario: String, claveHash: String): Alumno?
}