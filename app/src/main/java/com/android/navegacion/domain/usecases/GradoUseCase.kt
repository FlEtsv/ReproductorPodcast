package com.universae.reproductor.domain.usecases

import com.universae.reproductor.domain.entities.grado.Grado
import com.universae.reproductor.domain.entities.grado.GradoId

interface GradoUseCase {
    fun getGrado(GradoId: GradoId): Grado?
}