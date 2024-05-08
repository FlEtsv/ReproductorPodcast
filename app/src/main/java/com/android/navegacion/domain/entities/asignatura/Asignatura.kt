package com.universae.reproductor.domain.entities.asignatura

import com.universae.reproductor.domain.entities.tema.Tema
import java.util.*

data class Asignatura(
    val asignaturaId: AsignaturaId,
    val nombreAsignatura: String,
    val temas: List<Tema>,
    val icoAsignatura : String,

    //val progreso : Int //dependiente de tema
)

class AsignaturaId(id: String) {
    val id: UUID = UUID.nameUUIDFromBytes(id.toByteArray())
}