package com.universae.data.local

import com.universae.data.local.dataexample.PreviewTemas
import com.universae.domain.entities.alumno.AlumnoId
import com.universae.domain.entities.asignatura.AsignaturaId
import com.universae.domain.entities.tema.Tema
import com.universae.domain.entities.tema.TemaId
import com.universae.domain.entities.tema.TemaRepository

/**
 * Implementación del repositorio de temas.
 * Esta clase se encarga de manejar las operaciones de los temas en la base de datos local.
 */
object TemaRepositoryImpl : TemaRepository {

    /**
     * Guarda el punto de parada de un tema escuchado por un alumno.
     *
     * @param alumnoId El ID del alumno.
     * @param temaId El ID del tema.
     * @param puntoParada El punto de parada del tema.
     * @return El número de filas afectadas.
     */
    override fun guardarPuntoParada(alumnoId: AlumnoId, temaId: TemaId, puntoParada: Int): Int {
        // TODO("Implementar la lógica para guardar el punto de parada de un tema escuchado por un alumno")
        return 0
    }

    /**
     * Marca un tema como escuchado por un alumno.
     *
     * @param alumnoId El ID del alumno.
     * @param temaId El ID del tema.
     * @return El número de filas afectadas.
     */
    override fun marcarTemaComoEscuchado(alumnoId: AlumnoId, temaId: TemaId): Int {
        TODO(
            "Implementar la lógica para marcar un tema como escuchado por un alumno"
        )
    }

    /**
     * Obtiene un tema por su ID.
     *
     * @param temaId El ID del tema.
     * @return El tema si se encuentra, null en caso contrario.
     */
    override fun obtenerTema(temaId: TemaId): Tema? {
        return PreviewTemas.firstOrNull { it.temaId == temaId }
    }

    /**
     * Obtiene los temas de una asignatura.
     *
     * @param asignaturaId El ID de la asignatura.
     * @return La lista de temas de la asignatura.
     */
    override fun obtenerTemasAsignatura(asignaturaId: AsignaturaId): List<Tema> {
        return AsignaturaRepositoryImpl.getAsignatura(asignaturaId)?.temas ?: emptyList()
    }
}