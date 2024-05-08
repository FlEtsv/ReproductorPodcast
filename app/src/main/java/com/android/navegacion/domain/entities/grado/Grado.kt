package com.universae.reproductor.domain.entities.grado

import android.graphics.drawable.Icon
import com.universae.reproductor.domain.entities.asignatura.AsignaturaId
import java.util.*

data class Grado(
    val gradoId: GradoId,
    val nombreModulo: String,
    val asignaturasId: List<AsignaturaId>,
    val icoGrado : String
)

class GradoId(id: String) {
    val id: UUID = UUID.nameUUIDFromBytes(id.toByteArray())
}