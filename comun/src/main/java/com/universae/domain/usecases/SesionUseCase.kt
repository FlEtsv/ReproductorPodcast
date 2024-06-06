package com.universae.domain.usecases

import com.universae.domain.Sesion
import com.universae.reproductor.domain.entities.alumno.Alumno

interface SesionUseCase {
    fun iniciarSesion(nombreUsuario: String, clave: String): Boolean
    fun cerrarSesion()

    companion object SesionUseCaseImpl : SesionUseCase {
        override fun iniciarSesion(nombreUsuario: String, clave: String): Boolean {
            return Sesion.iniciarSesion(nombreUsuario, clave)
        }

        override fun cerrarSesion() {
            Sesion.cerrarSesion()
        }
    }
}