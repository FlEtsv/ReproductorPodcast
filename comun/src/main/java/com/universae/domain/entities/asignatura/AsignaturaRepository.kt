package com.universae.domain.entities.asignatura

/**
 * Interfaz para el repositorio de asignaturas.
 * Esta interfaz define las operaciones que se pueden realizar en un repositorio de asignaturas.
 */
interface AsignaturaRepository {

    /**
     * Obtiene una asignatura por su identificador único.
     *
     * @param asignaturaId El identificador único de la asignatura.
     * @return La asignatura si se encuentra, null en caso contrario.
     */
    fun getAsignatura(asignaturaId: AsignaturaId): Asignatura?
}