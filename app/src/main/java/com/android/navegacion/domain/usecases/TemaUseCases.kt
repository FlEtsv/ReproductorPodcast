package com.universae.reproductor.domain.usecases

import com.android.navegacion.domain.Sesion
import com.universae.reproductor.domain.entities.asignatura.AsignaturaId
import com.universae.reproductor.domain.entities.tema.Tema
import com.universae.reproductor.domain.entities.tema.TemaId

interface TemaUseCases {
    fun temasPorAsignatura(asignaturaId: AsignaturaId): List<Tema>
    fun marcarTemaComoCompletado(temaId: TemaId): Int // retorna la cantidad de filas afectadas, -1 si no se pudo realizar la operación
    fun marcarPuntoParada(
        temaId: TemaId,
        puntoParada: Int
    ): Int // retorna la cantidad de filas afectadas, -1 si no se pudo realizar la operación
}

object TemaUseCasesImpl : TemaUseCases {
    override fun temasPorAsignatura(asignaturaId: AsignaturaId): List<Tema> {
        return if (Sesion.sesionIniciada) {
            Sesion.asignaturas.find { asignatura -> asignatura.asignaturaId.id == asignaturaId.id }?.temas
                ?: emptyList()
        } else emptyList()
    }

    override fun marcarTemaComoCompletado(temaId: TemaId): Int {
        return if (Sesion.sesionIniciada) {
            val tema =
                Sesion.asignaturas.flatMap { it.temas }.find { tema -> tema.temaId.id == temaId.id }
            tema?.terminado = true
            if (tema?.terminado == true) {
                Sesion.marcarTemaComoCompletado(temaId)
            } else -1
        } else -1
    }

    override fun marcarPuntoParada(temaId: TemaId, puntoParada: Int): Int {
        return if (Sesion.sesionIniciada) {
            val tema =
                Sesion.asignaturas.flatMap { it.temas }.find { tema -> tema.temaId.id == temaId.id }
            tema?.puntoParada = puntoParada
            if (tema?.puntoParada == puntoParada) {
                Sesion.guardarPuntoParada(temaId, puntoParada)
            } else -1
        } else -1
    }
}