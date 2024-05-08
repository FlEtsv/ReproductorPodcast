package com.universae.reproductor.domain.usecases

import com.universae.reproductor.domain.entities.tema.Tema

interface AudioPlayerUseCases {
    fun play(tema: Tema)
    fun stop()
    fun pausa()
    fun continuar()
}