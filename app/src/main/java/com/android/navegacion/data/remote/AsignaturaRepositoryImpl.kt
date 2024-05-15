package com.android.navegacion.data.remote

import com.universae.reproductor.data.remote.TemaRepositoryImpl
import com.universae.reproductor.domain.entities.asignatura.Asignatura
import com.universae.reproductor.domain.entities.asignatura.AsignaturaId
import com.universae.reproductor.domain.entities.asignatura.AsignaturaRepository
import com.universae.reproductor.domain.entities.tema.Tema
import com.universae.reproductor.domaintest.PreviewAsignaturas
import java.sql.Connection
import java.sql.DriverManager
import java.sql.PreparedStatement
import java.sql.ResultSet

object AsignaturaRepositoryImpl : AsignaturaRepository {

    override fun getAsignatura(asignaturaId: AsignaturaId): Asignatura? {
        return if(PreviewAsignaturas.filter { it.asignaturaId == asignaturaId}.isNotEmpty()){
            PreviewAsignaturas.filter { it.asignaturaId == asignaturaId}[0]
        } else {
            null
        }


        /*val query = "SELECT * FROM asignaturas WHERE asignatura_id = ?"
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
            preparedStatement.setString(1, asignaturaId.id.toString())
            resultSet = preparedStatement.executeQuery()
            return if (resultSet != null && resultSet.next()) {
                val asignaturaId: Int = resultSet.getInt("asignatura_id")
                val nombreAsignatura: String = resultSet.getString("nombre_asignatura")
                val temas: List<Tema> =
                    TemaRepositoryImpl.obtenerTemasAsignatura(AsignaturaId(asignaturaId))
                val icoAsignatura: String = resultSet.getString("icono_asignatura")?.let { it }
                    ?: "" //TODO("implementar el icono por defecto si la asignatura no tiene icono en BD")
                Asignatura(AsignaturaId(asignaturaId), nombreAsignatura, temas, icoAsignatura)
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
}