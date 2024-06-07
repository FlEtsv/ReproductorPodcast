package com.universae.domain

/**
 * Interfaz que define un observador de la sesión.
 */
interface SesionObserver {
    /**
     * Método que se invoca cuando la sesión se actualiza.
     */
    fun onSesionUpdated()
}