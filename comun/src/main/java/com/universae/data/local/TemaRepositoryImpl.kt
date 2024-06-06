package com.universae.data.local

import com.universae.reproductor.domain.entities.alumno.AlumnoId
import com.universae.domain.entities.asignatura.AsignaturaId
import com.universae.reproductor.domain.entities.tema.Tema
import com.universae.reproductor.domain.entities.tema.TemaId
import com.universae.reproductor.domain.entities.tema.TemaRepository
import com.universae.data.local.dataexample.PreviewTemas

object TemaRepositoryImpl : TemaRepository {

    override fun guardarPuntoParada(alumnoId: AlumnoId, temaId: TemaId, puntoParada: Int): Int {
        /* TODO("Implementar la lógica para guardar el punto de parada de un tema escuchado por un alumno")
        val query =
            "INSERT INTO progreso_escucha_alumno (usuario_id, tema_id, punto_parada) VALUES (?, ?, ?)"
        var connection: Connection? = null
        var preparedStatement: PreparedStatement? = null

        try {
            connection = DriverManager.getConnection(
                "jdbc:mysql://10.0.2.2:3306/reproductor",
                "dbadmin",
                "dbadmin"
            )
            preparedStatement = connection.prepareStatement(query)
            preparedStatement.setString(1, alumnoId.id.toString())
            preparedStatement.setString(2, temaId.id.toString())
            preparedStatement.setInt(3, puntoParada)
            val affectedRows = preparedStatement.executeUpdate()

            return affectedRows
        } catch (e: SQLException) {
            println("Database operation failed. Error: ${e.message}")
            e.printStackTrace()
            return -1
        } finally {
            preparedStatement?.close()
            connection?.close()
        }
         */
        return 0
    }

    override fun marcarTemaComoEscuchado(alumnoId: AlumnoId, temaId: TemaId): Int {
        TODO(
            "Implementar la lógica para marcar un tema como escuchado por un alumno"
        )
    }

    override fun obtenerTema(temaId: TemaId): Tema? {

        return PreviewTemas.firstOrNull { it.temaId == temaId }

        /*
        val query = "SELECT * FROM temas WHERE tema_id = ?"
        var connection: Connection? = null
        var preparedStatement: PreparedStatement? = null
        var resultSet: ResultSet? = null

        try {
            connection = DriverManager.getConnection(
                "jdbc:mysql://10.0.2.2:3306/reproductor",
                "dbadmin",
                "dbadmin"
            )
            preparedStatement = connection.prepareStatement(query)
            preparedStatement.setString(1, temaId.id.toString())
            resultSet = preparedStatement.executeQuery()
            return if (resultSet != null && resultSet.next()) {
                val nombreTema: String = resultSet.getString("nombre_tema")
                val descripcionTema: String = resultSet.getString("descripcion_tema")
                val duracionAudioSegundos: Int = resultSet.getInt("duracion_segundos_audio")
                val urlRecurso: String = resultSet.getString("url_audio")
                return Tema(
                    temaId = temaId,
                    nombreTema = nombreTema,
                    descripcionTema = descripcionTema,
                    duracionAudio = duracionAudioSegundos.toDuration(DurationUnit.SECONDS),
                    audioUrl = urlRecurso
                )
            } else {
                null
            }
        } catch (e: Exception) {
            println("Database operation failed. Error: ${e.message}")
            e.printStackTrace()
            return null
        } finally {
            resultSet?.close()
            preparedStatement?.close()
            connection?.close()
        }

         */
    }

    override fun obtenerTemasAsignatura(asignaturaId: AsignaturaId): List<Tema> {
        return AsignaturaRepositoryImpl.getAsignatura(asignaturaId)?.temas ?: emptyList()

        /*
        val query = "SELECT * FROM temas WHERE asignatura_id = ?"
        var connection: Connection? = null
        var preparedStatement: PreparedStatement? = null
        var resultSet: ResultSet? = null
        val temas = mutableListOf<Tema>()

        try {
            connection = DriverManager.getConnection(
                "jdbc:mysql://10.0.2.2:3306/reproductor",
                "dbadmin",
                "dbadmin"
            )
            preparedStatement = connection.prepareStatement(query)
            preparedStatement.setString(1, asignaturaId.id.toString())
            resultSet = preparedStatement.executeQuery()

            while (resultSet != null && resultSet.next()) {
                val temaId: Int = resultSet.getInt("tema_id")
                val nombreTema: String = resultSet.getString("nombre_tema")
                val descripcionTema: String = resultSet.getString("descripcion_tema")
                    ?: "" // TODO("Mirar: String empty "" por defecto si el tema no tiene descripcion en BD")
                val duracionAudioSegundos: Int = resultSet.getInt("duracion_segundos_audio")
                val urlRecurso: String =
                    resultSet.getString("url_audio") //TODO("Mirar: String empty "" por defecto si el tema no tiene url del recuso MP3 en BD")
                temas.add(
                    Tema(
                        temaId = TemaId(temaId),
                        nombreTema = nombreTema,
                        descripcionTema = descripcionTema,
                        duracionAudio = duracionAudioSegundos.toDuration(DurationUnit.SECONDS),
                        audioUrl = urlRecurso
                    )
                )
            }
            return temas
        } catch (e: Exception) {
            println("Database operation failed. Error: ${e.message}")
            e.printStackTrace()
            return temas
        } finally {
            resultSet?.close()
            preparedStatement?.close()
            connection?.close()
        }

         */
    }
}