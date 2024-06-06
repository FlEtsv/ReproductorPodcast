package com.universae.data.local

import com.universae.reproductor.domain.entities.grado.Grado
import com.universae.reproductor.domain.entities.grado.GradoId
import com.universae.reproductor.domain.entities.grado.GradoRepository
import com.universae.data.local.dataexample.PreviewGrados

object GradoRepositoryImpl : GradoRepository {

    override fun getGrado(gradoId: GradoId): Grado? {

        return PreviewGrados.firstOrNull { it.gradoId == gradoId }

        /*val query = "SELECT * FROM grados WHERE grado_id = ?"
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
            preparedStatement.setString(1, gradoId.id.toString())
            resultSet = preparedStatement.executeQuery()
            return if (resultSet != null && resultSet.next()) {
                val gradoId: Int = resultSet.getInt("grado_id")
                val nombreModulo: String = resultSet.getString("nombre_grado")
                val asignaturasId: List<AsignaturaId> =
                    buscarAsignaturasIdGrado(gradoId).map { id -> AsignaturaId(id) }
                val icoGrado: String = resultSet.getString("icono_grado")?.let { it }
                    ?: "" //TODO("implementar el icono por defecto si el grado no tiene icono en BD")
                Grado(GradoId(gradoId), nombreModulo, asignaturasId, icoGrado)
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

    /*
    private fun buscarAsignaturasIdGrado(gradoId: Int): List<Int> {
        val query = "SELECT * FROM grado_asignatura WHERE grado_id = ?"
        var connection: Connection? = null
        var preparedStatement: PreparedStatement? = null
        var resultSet: ResultSet? = null
        val asignaturasId = mutableListOf<Int>()

        try {
            connection = DriverManager.getConnection(
                "jdbc:mysql://10.0.2.2:3306/reproductor",
                "dbadmin",
                "dbadmin"
            )
            preparedStatement = connection.prepareStatement(query)
            preparedStatement.setString(1, gradoId.toString())
            resultSet = preparedStatement.executeQuery()
            while (resultSet != null && resultSet.next()) {
                asignaturasId.add(resultSet.getInt("asignatura_id"))
            }
            return asignaturasId
        } catch (e: Exception) {
            println("Database operation failed. Error: ${e.message}")
            e.printStackTrace()
            return asignaturasId
        } finally {
            resultSet?.close()
            preparedStatement?.close()
            connection?.close()
        }
    }

     */

}