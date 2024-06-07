package com.universae.domain.entities.tema

import com.universae.domain.entities.alumno.AlumnoId
import com.universae.domain.entities.asignatura.AsignaturaId

/**
 * Interfaz para el repositorio de temas.
 * Esta interfaz define las operaciones que se pueden realizar en un repositorio de temas.
 */
interface TemaRepository {

    /**
     * Guarda el punto de parada de un tema para un alumno.
     *
     * @param alumnoId El identificador único del alumno.
     * @param temaId El identificador único del tema.
     * @param puntoParada El punto de parada del tema.
     * @return El punto de parada guardado.
     */
    fun guardarPuntoParada(alumnoId: AlumnoId, temaId: TemaId, puntoParada: Int): Int

    /**
     * Marca un tema como escuchado para un alumno.
     *
     * @param alumnoId El identificador único del alumno.
     * @param temaId El identificador único del tema.
     * @return El estado de la operación.
     */
    fun marcarTemaComoEscuchado(alumnoId: AlumnoId, temaId: TemaId): Int

    /**
     * Obtiene un tema por su identificador único.
     *
     * @param temaId El identificador único del tema.
     * @return El tema si se encuentra, null en caso contrario.
     */
    fun obtenerTema(temaId: TemaId): Tema?

    /**
     * Obtiene la lista de temas de una asignatura.
     *
     * @param asignaturaId El identificador único de la asignatura.
     * @return La lista de temas de la asignatura.
     */
    fun obtenerTemasAsignatura(asignaturaId: AsignaturaId): List<Tema>
}