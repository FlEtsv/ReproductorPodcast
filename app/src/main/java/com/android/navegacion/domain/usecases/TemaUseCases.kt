package com.universae.reproductor.domain.usecases

import com.universae.reproductor.domain.entities.asignatura.AsignaturaId
import com.universae.reproductor.domain.entities.tema.Tema
import com.universae.reproductor.domain.entities.tema.TemaId
import kotlin.time.Duration

interface TemaUseCases {
    fun temasPorAsignatura(asignaturaId: AsignaturaId): List<Tema>
    fun marcarTemaComoCompletado(temaId: TemaId): Boolean
    fun marcarPuntoParada(temaId: TemaId, puntoParada: Int): Boolean // deberia llamar a TemaRepository.guardarPuntoParada
}