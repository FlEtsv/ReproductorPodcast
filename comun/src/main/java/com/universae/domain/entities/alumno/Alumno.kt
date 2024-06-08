package com.universae.domain.entities.alumno

import com.universae.domain.entities.grado.GradoId

/**
 * Representa a un alumno en el dominio de la aplicación.
 *
 * @property nombreUsuario El nombre de usuario del alumno.
 * @property nombreReal El nombre real del alumno.
 * @property alumnoId El identificador único del alumno.
 * @property gradosId La lista de identificadores de los grados que el alumno está cursando.
 */
class Alumno(
    val nombreUsuario: String,
    val nombreReal: String,
    val alumnoId: AlumnoId,
    val gradosId: List<GradoId>
)

/**
 * Representa el identificador único de un alumno.
 *
 * @property id El valor del identificador.
 */
data class AlumnoId(val id: Int)