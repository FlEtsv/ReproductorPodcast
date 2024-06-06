package com.universae.reproductor.domain.usecases

import com.universae.domain.Sesion
import com.universae.domain.entities.asignatura.Asignatura
import com.universae.domain.entities.asignatura.AsignaturaId
import com.universae.reproductor.domain.entities.grado.GradoId
import com.universae.reproductor.domain.entities.tema.TemaId

interface AsignaturaUseCases {
    fun getAsignaturaByAsignaturaId(asignaturaId: AsignaturaId): Asignatura?
    fun getAsignaturaById(id: Int): Asignatura?
    fun getListAsignaturas(asignaturasId: List<AsignaturaId>): List<Asignatura>
    fun porcentajeCompletadoAsignatura(asignaturaId: AsignaturaId): Int
    fun asignaturasNoCompletadas(gradoId: GradoId): List<Asignatura>
    fun getAsignaturaByTemaId(temaId: TemaId): Asignatura?
    fun getNombreAsignaturaByTemaId(temaId: TemaId): String?
    fun getNombreAsignaturaById(idAsignatura: Int) : String?
}

object AsignaturaUseCasesImpl : AsignaturaUseCases {

    override fun getAsignaturaByAsignaturaId(asignaturaId: AsignaturaId): Asignatura? {
        return if (Sesion.sesionIniciada) Sesion.asignaturas.find { asignatura -> asignatura.asignaturaId.id == asignaturaId.id } else null
    }

    override fun getAsignaturaById(id: Int): Asignatura? {
        return if (Sesion.sesionIniciada) Sesion.asignaturas.find { asignatura -> asignatura.asignaturaId.id == id } else null
    }

    override fun getListAsignaturas(asignaturasId: List<AsignaturaId>): List<Asignatura> {
        return if (Sesion.sesionIniciada) Sesion.asignaturas.filter {
            it.asignaturaId.id in asignaturasId.map { it.id }
        } else emptyList()
    }

    override fun porcentajeCompletadoAsignatura(asignaturaId: AsignaturaId): Int {
        getAsignaturaByAsignaturaId(asignaturaId)?.let { asignatura ->
            val temasCompletados = asignatura.temas.count { it.isCompletado() }
            return (temasCompletados * 100) / asignatura.temas.size
        } ?: return -1
    }

    override fun asignaturasNoCompletadas(gradoId: GradoId): List<Asignatura> {
        return GradoUseCaseImpl.getGradoByGradoID(gradoId)?.let { grado ->
            grado.asignaturasId.mapNotNull { getAsignaturaByAsignaturaId(it) }.filter { asignatura ->
                asignatura.temas.any { !it.terminado }
            }
        } ?: emptyList()
    }

    override fun getAsignaturaByTemaId(temaId: TemaId): Asignatura? {
        return if (Sesion.sesionIniciada) Sesion.asignaturas.find { asignatura -> asignatura.temas.any { tema -> tema.temaId.id == temaId.id } } else null
    }

    override fun getNombreAsignaturaByTemaId(temaId: TemaId): String? {
        return getAsignaturaByTemaId(temaId)?.nombreAsignatura
    }

    override fun getNombreAsignaturaById(idAsignatura: Int): String? {
        return getAsignaturaById(idAsignatura)?.nombreAsignatura
    }
}