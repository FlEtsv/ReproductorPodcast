package com.universae.data.local

import com.universae.data.local.dataexample.PreviewAsignaturas
import com.universae.domain.entities.asignatura.Asignatura
import com.universae.domain.entities.asignatura.AsignaturaId
import com.universae.domain.entities.asignatura.AsignaturaRepository

/**
 * Implementación del repositorio de asignaturas.
 * Esta clase se encarga de manejar las operaciones de las asignaturas en la base de datos local.
 */
object AsignaturaRepositoryImpl : AsignaturaRepository {

    /**
     * Obtiene una asignatura por su ID.
     *
     * @param asignaturaId El ID de la asignatura.
     * @return La asignatura si se encuentra, null en caso contrario.
     */
    override fun getAsignatura(asignaturaId: AsignaturaId): Asignatura? {
        return PreviewAsignaturas.firstOrNull { it.asignaturaId == asignaturaId }
    }
}

/*
EJEMPLO IMPLEMENTACIÓN CON JDBC ACCEDIENDO A BASE DE DATOS MySQL REMOTA

val query = "SELECT * FROM asignaturas WHERE asignatura_id = ?"
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