package com.universae.reproductor.domain.usecases

import com.universae.reproductor.domain.entities.tema.Tema

interface AudioPlayerUseCases {
    fun reproducir(tema: Tema,
    pauseThenPlaying: Boolean? = true,
    parentMediaId: String? = null
    ): Boolean // Reproduce un tema
    fun reproducir(temas: List<Tema>) // Reproduce una lista de temas (playlist)
    fun stop() // Detiene la reproducción
    fun pausa() // Pausa la reproducción
    fun continuar() // Continúa la reproducción después de pausar
    fun adelantarDiezSegundos() // Adelanta la reproducción diez segundos
    fun retrocederDiezSegundos() // Retrocede la reproducción diez segundos
    fun siguienteTema() // Reproduce el siguiente tema de la lista
    fun temaAnterior() // Reproduce el tema anterior de la lista
    fun isPlaying(): Boolean // Devuelve true si la reproducción está en curso
}