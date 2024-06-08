package com.universae.domain.entities.asignatura

import com.universae.domain.entities.tema.Tema

/**
 * Representa una asignatura en el dominio de la aplicación.
 *
 * @property asignaturaId El identificador único de la asignatura.
 * @property nombreAsignatura El nombre de la asignatura.
 * @property temas La lista de temas que pertenecen a la asignatura.
 * @property icoAsignatura El ícono de la asignatura.
 */
data class Asignatura(
    val asignaturaId: AsignaturaId,
    val nombreAsignatura: String,
    val temas: List<Tema>,
    val icoAsignatura: String,
)

/**
 * Representa el identificador único de una asignatura.
 *
 * @property id El valor del identificador.
 */
data class AsignaturaId(val id: Int)