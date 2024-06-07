package com.universae.domain.usecases

import com.universae.domain.Sesion
import com.universae.domain.entities.asignatura.AsignaturaId
import com.universae.domain.entities.tema.Tema
import com.universae.domain.entities.tema.TemaId

/**
 * Interfaz que define los casos de uso para los temas.
 */
interface TemaUseCases {
    /**
     * Obtiene un tema por su ID.
     * @param id El ID del tema.
     * @return El tema si se encuentra, null en caso contrario.
     */
    fun getTemaById(id: Int): Tema?

    /**
     * Obtiene los temas por el ID de la asignatura.
     * @param asignaturaId El ID de la asignatura.
     * @return La lista de temas de la asignatura.
     */
    fun temasPorAsignatura(asignaturaId: AsignaturaId): List<Tema>

    /**
     * Marca un tema como completado.
     * @param temaId El ID del tema.
     * @return La cantidad de filas afectadas, -1 si no se pudo realizar la operación.
     */
    fun marcarTemaComoCompletado(temaId: TemaId): Int

    /**
     * Marca un punto de parada en un tema.
     * @param temaId El ID del tema.
     * @param puntoParada El punto de parada.
     * @return La cantidad de filas afectadas, -1 si no se pudo realizar la operación.
     */
    fun marcarPuntoParada(
        temaId: TemaId,
        puntoParada: Int
    ): Int
}

/**
 * Implementación de los casos de uso para los temas.
 */
object TemaUseCasesImpl : TemaUseCases {

    /**
     * Obtiene un tema por su ID.
     * @param id El ID del tema.
     * @return El tema si se encuentra, null en caso contrario.
     */
    override fun getTemaById(id: Int): Tema? {
        return if (Sesion.sesionIniciada) {
            Sesion.asignaturas.flatMap { it.temas }.find { tema -> tema.temaId.id == id }
        } else null
    }

    /**
     * Obtiene los temas por el ID de la asignatura.
     * @param asignaturaId El ID de la asignatura.
     * @return La lista de temas de la asignatura.
     */
    override fun temasPorAsignatura(asignaturaId: AsignaturaId): List<Tema> {
        return if (Sesion.sesionIniciada) {
            Sesion.asignaturas.find { asignatura -> asignatura.asignaturaId.id == asignaturaId.id }?.temas
                ?: emptyList()
        } else emptyList()
    }

    /**
     * Marca un tema como completado.
     * @param temaId El ID del tema.
     * @return La cantidad de filas afectadas, -1 si no se pudo realizar la operación.
     */
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

    /**
     * Marca un punto de parada en un tema.
     * @param temaId El ID del tema.
     * @param puntoParada El punto de parada.
     * @return La cantidad de filas afectadas, -1 si no se pudo realizar la operación.
     */
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