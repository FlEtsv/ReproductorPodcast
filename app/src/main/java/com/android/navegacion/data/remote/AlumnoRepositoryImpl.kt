package com.android.navegacion.data.remote


import com.universae.reproductor.domain.entities.alumno.Alumno
import com.universae.reproductor.domain.entities.alumno.AlumnoId
import com.universae.reproductor.domain.entities.alumno.AlumnoRepository
import com.universae.reproductor.domain.entities.grado.GradoId
import com.universae.reproductor.domaintest.PreviewAlumno
import java.sql.Connection
import java.sql.DriverManager
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException

object AlumnoRepositoryImpl : AlumnoRepository {

    override fun getAlumno(nombreUsuario: String, claveHash: String): Alumno? {
        return if(PreviewAlumno.filter { it.nombreUsuario == nombreUsuario}.isNotEmpty()){
            PreviewAlumno.filter { it.nombreUsuario == nombreUsuario}[0]
        } else {
            null
        }
    }

    fun getAlumnoById(alumnoId: Int): Alumno? {
        return if(PreviewAlumno.filter { it.alumnoId.id == alumnoId}.isNotEmpty()){
            PreviewAlumno.filter { it.alumnoId.id == alumnoId}[0]
        } else {
            null
        }
    }
}