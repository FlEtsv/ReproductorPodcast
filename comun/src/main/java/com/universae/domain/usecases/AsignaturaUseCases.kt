package com.universae.domain.usecases

import com.universae.domain.Sesion
import com.universae.domain.entities.asignatura.Asignatura
import com.universae.domain.entities.asignatura.AsignaturaId
import com.universae.domain.entities.grado.GradoId
import com.universae.domain.entities.tema.TemaId

/**
 * Interfaz para los casos de uso de la asignatura.
 * Esta interfaz define las operaciones que se pueden realizar en un caso de uso de la asignatura.
 */
interface AsignaturaUseCases {
    /**
     * Obtiene una asignatura por su identificador único de asignatura.
     *
     * @param asignaturaId El identificador único de la asignatura.
     * @return La asignatura si se encuentra, null en caso contrario.
     */
    fun getAsignaturaByAsignaturaId(asignaturaId: AsignaturaId): Asignatura?

    /**
     * Obtiene una asignatura por su identificador único.
     *
     * @param id El identificador único de la asignatura.
     * @return La asignatura si se encuentra, null en caso contrario.
     */
    fun getAsignaturaById(id: Int): Asignatura?

    /**
     * Obtiene una lista de asignaturas por sus identificadores únicos.
     *
     * @param asignaturasId La lista de identificadores únicos de las asignaturas.
     * @return La lista de asignaturas si se encuentran, una lista vacía en caso contrario.
     */
    fun getListAsignaturas(asignaturasId: List<AsignaturaId>): List<Asignatura>

    /**
     * Obtiene el porcentaje completado de una asignatura.
     *
     * @param asignaturaId El identificador único de la asignatura.
     * @return El porcentaje completado de la asignatura.
     */
    fun porcentajeCompletadoAsignatura(asignaturaId: AsignaturaId): Int

    /**
     * Obtiene una lista de asignaturas no completadas de un grado.
     *
     * @param gradoId El identificador único del grado.
     * @return La lista de asignaturas no completadas del grado.
     */
    fun asignaturasNoCompletadas(gradoId: GradoId): List<Asignatura>

    /**
     * Obtiene una asignatura por el identificador único de un tema.
     *
     * @param temaId El identificador único del tema.
     * @return La asignatura si se encuentra, null en caso contrario.
     */
    fun getAsignaturaByTemaId(temaId: TemaId): Asignatura?

    /**
     * Obtiene el nombre de una asignatura por el identificador único de un tema.
     *
     * @param temaId El identificador único del tema.
     * @return El nombre de la asignatura si se encuentra, null en caso contrario.
     */
    fun getNombreAsignaturaByTemaId(temaId: TemaId): String?

    /**
     * Obtiene el nombre de una asignatura por su identificador único.
     *
     * @param idAsignatura El identificador único de la asignatura.
     * @return El nombre de la asignatura si se encuentra, null en caso contrario.
     */
    fun getNombreAsignaturaById(idAsignatura: Int): String?
}

/**
 * Implementación de la interfaz AsignaturaUseCases.
 * Esta clase define las operaciones que se pueden realizar en un caso de uso de la asignatura.
 */
object AsignaturaUseCasesImpl : AsignaturaUseCases {

    /**
     * Obtiene una asignatura por su identificador único de asignatura.
     * Verifica si la sesión está iniciada y si el identificador de la asignatura coincide con el proporcionado.
     *
     * @param asignaturaId El identificador único de la asignatura.
     * @return La asignatura si se encuentra, null en caso contrario.
     */
    override fun getAsignaturaByAsignaturaId(asignaturaId: AsignaturaId): Asignatura? {
        return if (Sesion.sesionIniciada) Sesion.asignaturas.find { asignatura -> asignatura.asignaturaId.id == asignaturaId.id } else null
    }

    /**
     * Obtiene una asignatura por su identificador único.
     * Verifica si la sesión está iniciada y si el identificador de la asignatura coincide con el proporcionado.
     *
     * @param id El identificador único de la asignatura.
     * @return La asignatura si se encuentra, null en caso contrario.
     */
    override fun getAsignaturaById(id: Int): Asignatura? {
        return if (Sesion.sesionIniciada) Sesion.asignaturas.find { asignatura -> asignatura.asignaturaId.id == id } else null
    }

    /**
     * Obtiene una lista de asignaturas por sus identificadores únicos.
     * Verifica si la sesión está iniciada y si los identificadores de las asignaturas coinciden con los proporcionados.
     *
     * @param asignaturasId La lista de identificadores únicos de las asignaturas.
     * @return La lista de asignaturas si se encuentran, una lista vacía en caso contrario.
     */
    override fun getListAsignaturas(asignaturasId: List<AsignaturaId>): List<Asignatura> {
        return if (Sesion.sesionIniciada) Sesion.asignaturas.filter {
            it.asignaturaId.id in asignaturasId.map { it.id }
        } else emptyList()
    }

    /**
     * Obtiene el porcentaje completado de una asignatura.
     * Verifica si la asignatura se encuentra y calcula el porcentaje de temas completados.
     *
     * @param asignaturaId El identificador único de la asignatura.
     * @return El porcentaje completado de la asignatura.
     */
    override fun porcentajeCompletadoAsignatura(asignaturaId: AsignaturaId): Int {
        getAsignaturaByAsignaturaId(asignaturaId)?.let { asignatura ->
            val temasCompletados = asignatura.temas.count { it.isCompletado() }
            return (temasCompletados * 100) / asignatura.temas.size
        } ?: return -1
    }

    /**
     * Obtiene una lista de asignaturas no completadas de un grado.
     * Verifica si el grado se encuentra y filtra las asignaturas que no están completadas.
     *
     * @param gradoId El identificador único del grado.
     * @return La lista de asignaturas no completadas del grado.
     */
    override fun asignaturasNoCompletadas(gradoId: GradoId): List<Asignatura> {
        return GradoUseCasesImpl.getGradoByGradoID(gradoId)?.let { grado ->
            grado.asignaturasId.mapNotNull { getAsignaturaByAsignaturaId(it) }
                .filter { asignatura ->
                    asignatura.temas.any { !it.terminado }
                }
        } ?: emptyList()
    }

    /**
     * Obtiene una asignatura por el identificador único de un tema.
     * Verifica si la sesión está iniciada y si el tema se encuentra en alguna de las asignaturas.
     *
     * @param temaId El identificador único del tema.
     * @return La asignatura si se encuentra, null en caso contrario.
     */
    override fun getAsignaturaByTemaId(temaId: TemaId): Asignatura? {
        return if (Sesion.sesionIniciada) Sesion.asignaturas.find { asignatura -> asignatura.temas.any { tema -> tema.temaId.id == temaId.id } } else null
    }

    /**
     * Obtiene el nombre de una asignatura por el identificador único de un tema.
     * Verifica si la asignatura se encuentra y devuelve su nombre.
     *
     * @param temaId El identificador único del tema.
     * @return El nombre de la asignatura si se encuentra, null en caso contrario.
     */
    override fun getNombreAsignaturaByTemaId(temaId: TemaId): String? {
        return getAsignaturaByTemaId(temaId)?.nombreAsignatura
    }

    /**
     * Obtiene el nombre de una asignatura por su identificador único.
     * Verifica si la asignatura se encuentra y devuelve su nombre.
     *
     * @param idAsignatura El identificador único de la asignatura.
     * @return El nombre de la asignatura si se encuentra, null en caso contrario.
     */
    override fun getNombreAsignaturaById(idAsignatura: Int): String? {
        return getAsignaturaById(idAsignatura)?.nombreAsignatura
    }
}