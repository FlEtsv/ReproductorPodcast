package com.universae.domain

import com.universae.data.local.AlumnoRepositoryImpl
import com.universae.data.local.AsignaturaRepositoryImpl
import com.universae.data.local.GradoRepositoryImpl
import com.universae.data.local.TemaRepositoryImpl
import com.universae.reproductor.domain.entities.alumno.Alumno
import com.universae.domain.entities.asignatura.Asignatura
import com.universae.reproductor.domain.entities.alumno.AlumnoId
import com.universae.reproductor.domain.entities.grado.Grado
import com.universae.reproductor.domain.entities.tema.TemaId
import java.security.MessageDigest

object Sesion {
    private val observers = mutableListOf<SesionObserver>()
    fun addObserver(observer: SesionObserver) {
        observers.add(observer)
    }

    fun removeObserver(observer: SesionObserver) {
        observers.remove(observer)
    }

    private fun notifyObservers() {
        observers.forEach { it.onSesionUpdated() }
    }
    val sesionIniciada: Boolean
        get() = Sesion::alumno.isInitialized
    lateinit var alumno: Alumno
    var grados: List<Grado> = emptyList()
    var asignaturas: List<Asignatura> = emptyList()
    val temasCompletados: MutableMap<TemaId, Boolean> = mutableMapOf()

    fun iniciarSesion(nombreAlumno: String, clave: String): Boolean {
        if (sesionIniciada && nombreAlumno.equals(alumno.nombreUsuario)) return true
        else {
            cerrarSesion()
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
            notifyObservers()
            return sesionIniciada
        }
    }

    fun marcarTemaComoCompletado(temaId: TemaId): Int {
        //TODO("mirar que esta llamada a DB no bloquea el hilo main de la app")
        val resultado = TemaRepositoryImpl.marcarTemaComoEscuchado(alumno.alumnoId, temaId)
        if (resultado > 0) {
            temasCompletados[temaId] = true
            return resultado
        }
        notifyObservers()
        return resultado
    }

    fun guardarPuntoParada(temaId: TemaId, puntoParada: Int): Int {
        //TODO("mirar que esta llamada a DB no bloquea el hilo main de la app")
        notifyObservers()
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
        alumno = Alumno(nombreUsuario = "", nombreReal = "", alumnoId = AlumnoId(-1), gradosId = emptyList())
        grados = emptyList()
        asignaturas = emptyList()
        temasCompletados.clear()
        notifyObservers()
    }

}