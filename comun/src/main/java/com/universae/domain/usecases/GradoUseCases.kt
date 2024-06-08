package com.universae.domain.usecases

import com.universae.domain.Sesion
import com.universae.domain.entities.grado.Grado
import com.universae.domain.entities.grado.GradoId

/**
 * Interfaz que define los casos de uso para los grados.
 */
interface GradoUseCases {
    /**
     * Obtiene un grado por su ID.
     * @param gradoId El ID del grado.
     * @return El grado si se encuentra, null en caso contrario.
     */
    fun getGradoByGradoID(gradoId: GradoId): Grado?

    /**
     * Obtiene los grados por el ID del alumno.
     * @param alumnoId El ID del alumno.
     * @return La lista de grados del alumno.
     */
    fun getGradosByAlumnoID(alumnoId: Int): List<Grado>

    /**
     * Calcula el porcentaje completado de un grado.
     * @param gradoId El ID del grado.
     * @return El porcentaje completado del grado.
     */
    fun porcentajeCompletadoGrado(gradoId: GradoId): Int
}

/**
 * Implementación de los casos de uso para los grados.
 */
object GradoUseCasesImpl : GradoUseCases {
    /**
     * Obtiene un grado por su ID.
     * @param gradoId El ID del grado.
     * @return El grado si se encuentra, null en caso contrario.
     */
    override fun getGradoByGradoID(gradoId: GradoId): Grado? {
        return if (Sesion.sesionIniciada) Sesion.grados.find { grado -> grado.gradoId.id == gradoId.id } else null
    }

    /**
     * Obtiene los grados por el ID del alumno.
     * @param alumnoId El ID del alumno.
     * @return La lista de grados del alumno.
     */
    override fun getGradosByAlumnoID(alumnoId: Int): List<Grado> {
        return if (Sesion.sesionIniciada && Sesion.alumno.alumnoId.id == alumnoId) Sesion.grados else emptyList()
    }

    /**
     * Calcula el porcentaje completado de un grado.
     * @param gradoId El ID del grado.
     * @return El porcentaje completado del grado.
     */
    override fun porcentajeCompletadoGrado(gradoId: GradoId): Int {
        getGradoByGradoID(gradoId)?.let { grado ->
            val totalPorcentajeAsignaturas =
                grado.asignaturasId.sumOf { AsignaturaUseCasesImpl.porcentajeCompletadoAsignatura(it) }
            return totalPorcentajeAsignaturas / grado.asignaturasId.size
        } ?: return -1
    }
}