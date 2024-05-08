package com.universae.reproductor.domain.usecases

import com.universae.reproductor.domain.entities.alumno.AlumnoId
import com.universae.reproductor.domain.entities.asignatura.Asignatura
import com.universae.reproductor.domain.entities.asignatura.AsignaturaId
import com.universae.reproductor.domain.entities.grado.GradoId

interface AsignaturaUseCases {
    fun asignaturasAlumno(alumnoId: AlumnoId): List<Asignatura>
    fun asignaturasGrado(gradoId: GradoId): List<Asignatura>
    fun porcentajeAsignatura(asignaturaId: AsignaturaId): Int
    fun asignaturasNoCompletadas(gradoId: GradoId): List<Asignatura>
}