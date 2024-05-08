package com.universae.reproductor.domain.entities.tema

import java.util.*
import kotlin.time.Duration

class Tema(
    val temaId: TemaId,
    val nombreTema: String,
    val descripcionTema: String,
    val duracionAudio: Duration, // TODO: comprobar si Duration es el tipo correcto
    val audioUrl: String, // URL de la fuente de audio


){
    var terminado: Boolean = false // TODO: una vez marcado como completado, no se puede desmarcar
    var puntoParada: Int = 0 // intentar dato duration en milisegundos
}

class TemaId(id: String) {
    val id: UUID = UUID.nameUUIDFromBytes(id.toByteArray())
}
        //el progreso del tema Puede ser 0 o 1 Boolean
        //si hay 10 temas  el progreso de la asignatura es de 0 a 10 por tema abierto
       // y el progreso del modulo es de asignatura * tema / total en procentaje