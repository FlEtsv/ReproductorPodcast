package com.universae.reproductor.domain.entities.alumno

import com.universae.reproductor.domain.entities.grado.GradoId

class Alumno(val nombreUsuario: String, val alumnoId: AlumnoId, val gradosId: List<GradoId>)

data class AlumnoId(val id: Int)