package com.universae.reproductor.domain.entities.asignatura

interface AsignaturaRepository {
    fun getAsignatura(asignaturaId: AsignaturaId): Asignatura?
}