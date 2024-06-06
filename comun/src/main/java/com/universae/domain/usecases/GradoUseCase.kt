package com.universae.reproductor.domain.usecases

import com.universae.domain.Sesion
import com.universae.reproductor.domain.entities.grado.Grado
import com.universae.reproductor.domain.entities.grado.GradoId

interface GradoUseCase {
    fun getGradoByGradoID(gradoId: GradoId): Grado?
    fun getGradosByAlumnoID(alumnoId: Int): List<Grado>
    fun porcentajeCompletadoGrado(gradoId: GradoId): Int
}

object GradoUseCaseImpl : GradoUseCase {
    override fun getGradoByGradoID(gradoId: GradoId): Grado? {
        return if (Sesion.sesionIniciada) Sesion.grados.find { grado -> grado.gradoId.id == gradoId.id } else null
    }

    override fun getGradosByAlumnoID(alumnoId: Int): List<Grado> {
        return if (Sesion.sesionIniciada && Sesion.alumno.alumnoId.id == alumnoId) Sesion.grados else emptyList()
    }

    override fun porcentajeCompletadoGrado(gradoId: GradoId): Int {
        getGradoByGradoID(gradoId)?.let { grado ->
            val totalPorcentajeAsignaturas = grado.asignaturasId.sumOf { AsignaturaUseCasesImpl.porcentajeCompletadoAsignatura(it) }
            return totalPorcentajeAsignaturas / grado.asignaturasId.size
        } ?: return -1
    }
}