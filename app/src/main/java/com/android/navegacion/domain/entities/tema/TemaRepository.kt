package com.universae.reproductor.domain.entities.tema

import com.universae.reproductor.domain.entities.alumno.AlumnoId
import com.universae.reproductor.domain.entities.asignatura.AsignaturaId
import kotlin.time.Duration

interface TemaRepository {
    fun guardarPuntoParada(alumnoId: AlumnoId, temaId: TemaId, puntoParada: Int): Boolean
    fun marcarTemaComoEscuchado(alumnoId: AlumnoId, temaId: TemaId): Boolean
    fun obtenerTema(asignaturaId: AsignaturaId, temaId: TemaId): Tema?
}