package com.universae.reproductor.domain.usecases

import com.universae.domain.Sesion
import com.universae.reproductor.domain.entities.grado.Grado
import com.universae.reproductor.domain.entities.grado.GradoId

interface GradoUseCase {
    fun getGrado(gradoId: GradoId): Grado?
}

object GradoUseCaseImpl : GradoUseCase {
    override fun getGrado(gradoId: GradoId): Grado? {
        return if (Sesion.sesionIniciada) Sesion.grados.find { grado -> grado.gradoId.id == gradoId.id } else null
    }
}