package com.android.navegacion.data.remote


import com.universae.reproductor.domain.entities.alumno.Alumno
import com.universae.reproductor.domain.entities.alumno.AlumnoId
import com.universae.reproductor.domain.entities.alumno.AlumnoRepository
import com.universae.reproductor.domain.entities.grado.GradoId
import java.sql.Connection
import java.sql.DriverManager
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException

object AlumnoRepositoryImpl : AlumnoRepository {

    override fun getAlumno(nombreUsuario: String, claveHash: String): Alumno? {
        val query =
            "SELECT * FROM usuarios WHERE nombre_usuario = ? AND LOWER(clave_hash) = LOWER(?)"
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
            preparedStatement.setString(1, nombreUsuario)
            preparedStatement.setString(2, claveHash)
            resultSet = preparedStatement.executeQuery()

            return if (resultSet != null && resultSet.next()) {
                val alumnoId: Int = resultSet.getInt("usuario_id")
                val nombreAlumno: String = resultSet.getString("nombre_usuario")
                val gradosId: List<GradoId> =
                    buscarGradosIdAlumno(alumnoId).map { id -> GradoId(id) }
                Alumno(nombreAlumno, AlumnoId(alumnoId), gradosId)
            } else {
                null
            }
        } catch (e: SQLException) {
            // manejo errores
            println("Database operation failed. Error: ${e.message}")
            e.printStackTrace()
            return null
        } finally {
            // cierra los recursos en orden inverso a como se abrieron
            resultSet?.close()
            preparedStatement?.close()
            connection?.close()
        }
    }

    private fun buscarGradosIdAlumno(alumnoId: Int): List<Int> {
        val query = "SELECT * FROM usuario_grado WHERE usuario_id = ?"
        var connection: Connection? = null
        var preparedStatement: PreparedStatement? = null
        var resultSet: ResultSet? = null
        val gradosId = mutableListOf<Int>()

        try {
            connection = DriverManager.getConnection(
                "jdbc:mysql://10.0.2.2:3306/reproductor",
                "dbadmin",
                "dbadmin"
            )
            preparedStatement = connection.prepareStatement(query)
            preparedStatement.setString(1, alumnoId.toString())
            resultSet = preparedStatement.executeQuery()

            while (resultSet != null && resultSet.next()) {
                gradosId.add(resultSet.getInt("grado_id"))
            }
            return gradosId
        } catch (e: SQLException) {
            // manejo errores
            println("Database operation failed. Error: ${e.message}")
            e.printStackTrace()
            return gradosId
            throw e
        } finally {
            // cierra los recursos en orden inverso a como se abrieron
            resultSet?.close()
            preparedStatement?.close()
            connection?.close()
        }
    }
}