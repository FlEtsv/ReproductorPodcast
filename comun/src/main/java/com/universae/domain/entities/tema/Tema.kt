package com.universae.reproductor.domain.entities.tema

import kotlin.time.Duration

class Tema(
    val temaId: TemaId,
    val nombreTema: String,
    val descripcionTema: String,
    val duracionAudio: Duration,
    val audioUrl: String, // URL de la fuente de audio
    val imagenUrl: String, // URL de la imagen del tema
    val trackNumber: Int,
) {

    var puntoParada: Int = 0 // intentar dato duration en milisegundos

    private var _terminado: Boolean = false

    var terminado: Boolean
        get() = _terminado
        set(value) {
            if (!_terminado) {
                _terminado = value
            }
        }
}

data class TemaId(val id: Int)
//el progreso del tema es la variable terminado: Boolean -> true si esta terminado