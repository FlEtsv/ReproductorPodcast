package com.universae.domain.usecases

import com.universae.domain.entities.tema.Tema

/**
 * Interfaz que define los casos de uso del reproductor de audio.
 */
interface AudioPlayerUseCases {
    /**
     * Reproduce un tema.
     * @param tema El tema a reproducir.
     * @param pauseThenPlaying Si es verdadero, pausa la reproducción actual y luego reproduce el tema.
     * @param parentMediaId El ID del medio padre, si existe.
     * @return Verdadero si la reproducción fue exitosa.
     */
    fun reproducir(
        tema: Tema,
        pauseThenPlaying: Boolean? = true,
        parentMediaId: String? = null
    ): Boolean

    /**
     * Reproduce una lista de temas (playlist).
     * @param temas La lista de temas a reproducir.
     */
    fun reproducir(temas: List<Tema>)

    /**
     * Detiene la reproducción.
     */
    fun stop()

    /**
     * Pausa la reproducción.
     */
    fun pausa()

    /**
     * Continúa la reproducción después de pausar.
     */
    fun continuar()

    /**
     * Adelanta la reproducción diez segundos.
     */
    fun adelantarDiezSegundos()

    /**
     * Retrocede la reproducción diez segundos.
     */
    fun retrocederDiezSegundos()

    /**
     * Reproduce el siguiente tema de la lista.
     */
    fun siguienteTema()

    /**
     * Reproduce el tema anterior de la lista.
     */
    fun temaAnterior()

    /**
     * Devuelve true si la reproducción está en curso.
     * @return Verdadero si la reproducción está en curso.
     */
    fun isPlaying(): Boolean
}