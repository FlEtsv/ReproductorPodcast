package com.universae.reproductor.domain.entities.tema

import com.universae.reproductor.domain.entities.alumno.AlumnoId
import com.universae.domain.entities.asignatura.AsignaturaId

interface TemaRepository {
    fun guardarPuntoParada(alumnoId: AlumnoId, temaId: TemaId, puntoParada: Int): Int
    fun marcarTemaComoEscuchado(alumnoId: AlumnoId, temaId: TemaId): Int
    fun obtenerTema(temaId: TemaId): Tema?
    fun obtenerTemasAsignatura(asignaturaId: AsignaturaId): List<Tema>
}