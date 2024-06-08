package com.universae.domain.entities.grado

/**
 * Interfaz para el repositorio de grados.
 * Esta interfaz define las operaciones que se pueden realizar en un repositorio de grados.
 */
interface GradoRepository {

    /**
     * Obtiene un grado por su identificador único.
     *
     * @param GradoId El identificador único del grado.
     * @return El grado si se encuentra, null en caso contrario.
     */
    fun getGrado(GradoId: GradoId): Grado?
}