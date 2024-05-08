package com.universae.reproductor.domain.entities.grado

interface GradoRepository {
    fun getGrado(GradoId: GradoId): Grado?
}