package com.universae.reproductor.domain.usecases

import com.android.navegacion.domain.Sesion
import com.universae.reproductor.domain.entities.asignatura.Asignatura
import com.universae.reproductor.domain.entities.asignatura.AsignaturaId
import com.universae.reproductor.domain.entities.grado.GradoId

interface AsignaturaUseCases {
    fun getAsignatura(asignaturaId: AsignaturaId): Asignatura?
    fun getListAsignaturas(asignaturasId: List<AsignaturaId>): List<Asignatura>
    fun porcentajeAsignatura(asignaturaId: AsignaturaId): Int
    fun asignaturasNoCompletadas(gradoId: GradoId): List<Asignatura>
}

object AsignaturaUseCasesImpl : AsignaturaUseCases {

    override fun getAsignatura(asignaturaId: AsignaturaId): Asignatura? {
        return if (Sesion.sesionIniciada) Sesion.asignaturas.find { asignatura -> asignatura.asignaturaId.id == asignaturaId.id } else null
    }

    override fun getListAsignaturas(asignaturasId: List<AsignaturaId>): List<Asignatura> {
        return if (Sesion.sesionIniciada) Sesion.asignaturas.filter {
            it.asignaturaId.id in asignaturasId.map { it.id }
        } else emptyList()
    }

    override fun porcentajeAsignatura(asignaturaId: AsignaturaId): Int {
        getAsignatura(asignaturaId)?.let { asignatura ->
            val temasCompletados = asignatura.temas.count { it.terminado }
            return (temasCompletados * 100) / asignatura.temas.size
        } ?: return -1
    }

    override fun asignaturasNoCompletadas(gradoId: GradoId): List<Asignatura> {
        return GradoUseCaseImpl.getGrado(gradoId)?.let { grado ->
            grado.asignaturasId.mapNotNull { getAsignatura(it) }.filter { asignatura ->
                asignatura.temas.any { !it.terminado }
            }
        } ?: emptyList()
    }
}