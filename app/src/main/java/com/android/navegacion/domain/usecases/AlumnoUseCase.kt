package com.universae.reproductor.domain.usecases

import com.universae.reproductor.domain.entities.alumno.Alumno
import com.universae.reproductor.domain.entities.alumno.AlumnoId

interface AlumnoUseCase {
    fun getAlumno(alumnoId: AlumnoId): Alumno?
}