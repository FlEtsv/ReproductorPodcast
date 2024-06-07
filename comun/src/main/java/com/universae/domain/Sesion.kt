package com.universae.domain

import com.universae.data.local.AlumnoRepositoryImpl
import com.universae.data.local.AsignaturaRepositoryImpl
import com.universae.data.local.GradoRepositoryImpl
import com.universae.data.local.TemaRepositoryImpl
import com.universae.domain.entities.alumno.Alumno
import com.universae.domain.entities.alumno.AlumnoId
import com.universae.domain.entities.asignatura.Asignatura
import com.universae.domain.entities.grado.Grado
import com.universae.domain.entities.tema.TemaId
import java.security.MessageDigest

/**
 * Objeto que representa una sesión de usuario.
 */
object Sesion {
    private val observers = mutableListOf<SesionObserver>()

    /**
     * Añade un observador a la sesión.
     * @param observer El observador a añadir.
     */
    fun addObserver(observer: SesionObserver) {
        observers.add(observer)
    }

    /**
     * Elimina un observador de la sesión.
     * @param observer El observador a eliminar.
     */
    fun removeObserver(observer: SesionObserver) {
        observers.remove(observer)
    }

    /**
     * Notifica a todos los observadores de la sesión.
     */
    private fun notifyObservers() {
        observers.forEach { it.onSesionUpdated() }
    }

    /**
     * Indica si la sesión está iniciada.
     */
    val sesionIniciada: Boolean
        get() = Sesion::alumno.isInitialized

    /**
     * Almacena el alumno de la sesión.
     */
    lateinit var alumno: Alumno

    /**
     * Almacena los grados del alumno de la sesión.
     */
    var grados: List<Grado> = emptyList()

    /**
     * Almacena las asignaturas del alumno de la sesión.
     */
    var asignaturas: List<Asignatura> = emptyList()

    /**
     * Almacena los temas completados del alumno de la sesión.
     */
    val temasCompletados: MutableMap<TemaId, Boolean> = mutableMapOf()

    /**
     * Inicia una sesión.
     * @param nombreAlumno El nombre del alumno.
     * @param clave La clave del alumno.
     * @return Verdadero si la sesión se inició correctamente.
     */
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

    /**
     * Marca un tema como completado.
     * @param temaId El ID del tema.
     * @return La cantidad de filas afectadas, -1 si no se pudo realizar la operación.
     */
    fun marcarTemaComoCompletado(temaId: TemaId): Int {
        //TODO("mirar que esta llamada a DB no bloquea el hilo main de la app si le actuliza la informacion en la BD en linea")
        val resultado = TemaRepositoryImpl.marcarTemaComoEscuchado(alumno.alumnoId, temaId)
        if (resultado > 0) {
            temasCompletados[temaId] = true
            return resultado
        }
        notifyObservers()
        return resultado
    }

    /**
     * Guarda un punto de parada en un tema.
     * @param temaId El ID del tema.
     * @param puntoParada El punto de parada.
     * @return La cantidad de filas afectadas, -1 si no se pudo realizar la operación.
     */
    fun guardarPuntoParada(temaId: TemaId, puntoParada: Int): Int {
        //TODO("mirar que esta llamada a DB no bloquea el hilo main de la app si le actuliza la informacion en la BD en linea")
        notifyObservers()
        return TemaRepositoryImpl.guardarPuntoParada(alumno.alumnoId, temaId, puntoParada)
    }

    /**
     * Obtiene un alumno.
     * @param nombreUsuario El nombre del usuario.
     * @param clave La clave del usuario.
     * @return El alumno si se encuentra, null en caso contrario.
     */
    private fun GetAlumno(nombreUsuario: String, clave: String): Alumno? {
        val claveHash = clave.sha2()
        return AlumnoRepositoryImpl.getAlumno(nombreUsuario = nombreUsuario, claveHash = claveHash)
    }

    /**
     * Genera un hash SHA-224 de una cadena.
     * @return La cadena hash.
     */
    private fun String.sha2(): String {
        val bytes = this.toByteArray()
        val md = MessageDigest.getInstance("SHA-224")
        val digest = md.digest(bytes)
        return digest.fold("") { str, it -> str + "%02x".format(it) }
    }

    /**
     * Cierra la sesión actual.
     */
    fun cerrarSesion() {
        alumno = Alumno(
            nombreUsuario = "",
            nombreReal = "",
            alumnoId = AlumnoId(-1),
            gradosId = emptyList()
        )
        grados = emptyList()
        asignaturas = emptyList()
        temasCompletados.clear()
        notifyObservers()
    }
}







