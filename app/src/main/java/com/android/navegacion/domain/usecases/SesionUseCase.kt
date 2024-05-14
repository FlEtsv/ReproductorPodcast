package com.android.navegacion.domain.usecases

interface SesionUseCase {
    fun iniciarSesion(nombreUsuario: String, clave: String): Boolean
    fun cerrarSesion()


    companion object SesionUseCaseImpl : SesionUseCase {
        override fun iniciarSesion(nombreUsuario: String, clave: String): Boolean {
            return com.android.navegacion.domain.Sesion.iniciarSesion(nombreUsuario, clave)
        }

        override fun cerrarSesion() {
            com.android.navegacion.domain.Sesion.cerrarSesion()
        }
    }
}