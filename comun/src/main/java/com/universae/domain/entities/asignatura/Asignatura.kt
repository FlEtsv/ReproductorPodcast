package com.universae.domain.entities.asignatura

import com.universae.reproductor.domain.entities.tema.Tema

data class Asignatura(
    val asignaturaId: AsignaturaId,
    val nombreAsignatura: String,
    val temas: List<Tema>,
    val icoAsignatura: String,

    //val progreso : Int //dependiente de tema
)

data class AsignaturaId(val id: Int)