package com.universae.reproductor.domain.entities.alumno

import com.universae.reproductor.domain.entities.grado.GradoId
import java.util.*

class Alumno(val nombreUsuario: String, val alumnoId: AlumnoId, val gradosId: List<GradoId>)

class AlumnoId(id: String){
    val id: UUID = UUID.nameUUIDFromBytes(id.toByteArray())
}