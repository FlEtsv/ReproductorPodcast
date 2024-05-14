package com.android.navegacion.domain

import com.android.navegacion.data.remote.AlumnoRepositoryImpl
import com.android.navegacion.data.remote.AsignaturaRepositoryImpl
import com.android.navegacion.data.remote.GradoRepositoryImpl
import com.universae.reproductor.data.remote.TemaRepositoryImpl
import com.universae.reproductor.domain.entities.alumno.Alumno
import com.universae.reproductor.domain.entities.alumno.AlumnoId
import com.universae.reproductor.domain.entities.asignatura.Asignatura
import com.universae.reproductor.domain.entities.grado.Grado
import com.universae.reproductor.domain.entities.tema.TemaId
import java.security.MessageDigest

object Sesion {
    val sesionIniciada: Boolean
        get() = ::alumno.isInitialized
    lateinit var alumno: Alumno
    var grados: List<Grado> = emptyList()
    var asignaturas: List<Asignatura> = emptyList()
    val temasCompletados: MutableMap<TemaId, Boolean> = mutableMapOf()

    fun iniciarSesion(nombreAlumno: String, clave: String): Boolean {
        if (sesionIniciada) return true

        val conexionAlumno: Alumno? = GetAlumno(nombreAlumno, clave)
        if (conexionAlumno != null) {
            alumno = conexionAlumno
            grados = alumno.gradosId.mapNotNull { gradoId ->
                GradoRepositoryImpl.getGrado(gradoId)
            }
            asignaturas = grados.flatMap { it.asignaturasId }.mapNotNull { asignaturaId ->
                AsignaturaRepositoryImpl.getAsignatura(asignaturaId)
            }.toSet().toList()
            temasCompletados.clear()
            asignaturas.flatMap { it.temas }.forEach { tema ->
                temasCompletados[tema.temaId] = tema.terminado
            }
        }
        return sesionIniciada
    }

    fun marcarTemaComoCompletado(temaId: TemaId): Int {
        //TODO("mirar que esta llamada a DB no bloquea el hilo main de la app")
        val resultado = TemaRepositoryImpl.marcarTemaComoEscuchado(alumno.alumnoId, temaId)
        if (resultado > 0) {
            temasCompletados[temaId] = true
            return resultado
        }
        return resultado
    }

    fun guardarPuntoParada(temaId: TemaId, puntoParada: Int): Int {
        //TODO("mirar que esta llamada a DB no bloquea el hilo main de la app")
        return TemaRepositoryImpl.guardarPuntoParada(alumno.alumnoId, temaId, puntoParada)
    }

    private fun GetAlumno(nombreUsuario: String, clave: String): Alumno? {
        val claveHash = clave.sha2()
        return AlumnoRepositoryImpl.getAlumno(nombreUsuario = nombreUsuario, claveHash = claveHash)
    }

    private fun String.sha2(): String {
        val bytes = this.toByteArray()
        val md = MessageDigest.getInstance("SHA-224")
        val digest = md.digest(bytes)
        return digest.fold("") { str, it -> str + "%02x".format(it) }
    }

    //TODO("implementar mostrar progreso de la asignatura y del grado")

    fun cerrarSesion() {
        //TODO("implementar la funcion cerrarSesion")
    }

}