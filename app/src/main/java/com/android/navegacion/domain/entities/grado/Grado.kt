package com.universae.reproductor.domain.entities.grado

import com.universae.reproductor.domain.entities.asignatura.AsignaturaId

data class Grado(
    val gradoId: GradoId,
    val nombreModulo: String,
    val asignaturasId: List<AsignaturaId>,
    val icoGrado: String
)

data class GradoId(val id: Int)