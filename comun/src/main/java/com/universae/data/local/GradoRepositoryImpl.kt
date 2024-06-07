package com.universae.data.local

import com.universae.data.local.dataexample.PreviewGrados
import com.universae.domain.entities.grado.Grado
import com.universae.domain.entities.grado.GradoId
import com.universae.domain.entities.grado.GradoRepository

/**
 * Implementación del repositorio de grados.
 * Esta clase se encarga de manejar las operaciones de los grados en la base de datos local.
 */
object GradoRepositoryImpl : GradoRepository {

    /**
     * Obtiene un grado por su ID.
     *
     * @param gradoId El ID del grado.
     * @return El grado si se encuentra, null en caso contrario.
     */
    override fun getGrado(gradoId: GradoId): Grado? {
        return PreviewGrados.firstOrNull { it.gradoId == gradoId }
    }
}