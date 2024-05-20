package com.universae.reproductor.domain.entities.asignatura

import com.universae.domain.entities.asignatura.Asignatura
import com.universae.domain.entities.asignatura.AsignaturaId

interface AsignaturaRepository {
    fun getAsignatura(asignaturaId: AsignaturaId): Asignatura?
}