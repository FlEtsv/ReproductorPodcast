package com.universae.domain.entities.grado

import com.universae.domain.entities.asignatura.AsignaturaId

/**
 * Representa un grado en el dominio de la aplicación.
 *
 * @property gradoId El identificador único del grado.
 * @property nombreModulo El nombre del módulo del grado.
 * @property asignaturasId La lista de identificadores de las asignaturas que pertenecen al grado.
 * @property icoGrado El ícono del grado.
 */
data class Grado(
    val gradoId: GradoId,
    val nombreModulo: String,
    val asignaturasId: List<AsignaturaId>,
    val icoGrado: String
)

/**
 * Representa el identificador único de un grado.
 *
 * @property id El valor del identificador.
 */
data class GradoId(val id: Int)