package com.universae.domain.entities.tema

import kotlin.time.Duration

/**
 * Representa un tema en el dominio de la aplicación.
 *
 * @property temaId El identificador único del tema.
 * @property nombreTema El nombre del tema.
 * @property descripcionTema La descripción del tema.
 * @property duracionAudio La duración del audio del tema.
 * @property audioUrl La URL de la fuente de audio del tema.
 * @property imagenUrl La URL de la imagen del tema.
 * @property trackNumber El número de pista del tema.
 * @property terminado Indica si el tema ha sido terminado.
 */
class Tema(
    val temaId: TemaId,
    val nombreTema: String,
    val descripcionTema: String,
    val duracionAudio: Duration,
    val audioUrl: String, // URL de la fuente de audio
    val imagenUrl: String, // URL de la imagen del tema
    val trackNumber: Int,
    var terminado: Boolean = false
) {

    /**
     * Representa el punto de parada del tema.
     * Intentar dato duration en milisegundos.
     */
    var puntoParada: Int = 0

    /**
     * Verifica si el tema ha sido completado.
     *
     * @return Verdadero si el tema ha sido terminado, falso en caso contrario.
     */
    fun isCompletado(): Boolean {
        return terminado
    }
}

/**
 * Representa el identificador único de un tema.
 *
 * @property id El valor del identificador.
 */
data class TemaId(val id: Int)