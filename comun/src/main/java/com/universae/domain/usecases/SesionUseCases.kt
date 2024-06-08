package com.universae.domain.usecases

import com.universae.domain.Sesion

/**
 * Interfaz que define los casos de uso para las sesiones.
 */
interface SesionUseCases {
    /**
     * Inicia una sesión.
     * @param nombreUsuario El nombre del usuario.
     * @param clave La clave del usuario.
     * @return Verdadero si la sesión se inició correctamente.
     */
    fun iniciarSesion(nombreUsuario: String, clave: String): Boolean

    /**
     * Cierra la sesión actual.
     */
    fun cerrarSesion()
}

/**
 * Implementación de los casos de uso para las sesiones.
 */
object SesionUseCasesImpl : SesionUseCases {
    /**
     * Inicia una sesión.
     * @param nombreUsuario El nombre del usuario.
     * @param clave La clave del usuario.
     * @return Verdadero si la sesión se inició correctamente.
     */
    override fun iniciarSesion(nombreUsuario: String, clave: String): Boolean {
        return Sesion.iniciarSesion(nombreUsuario, clave)
    }

    /**
     * Cierra la sesión actual.
     */
    override fun cerrarSesion() {
        Sesion.cerrarSesion()
    }
}