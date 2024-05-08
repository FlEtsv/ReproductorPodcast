package com.universae.reproductor.data.remote

import com.universae.reproductor.domain.entities.alumno.AlumnoId
import com.universae.reproductor.domain.entities.tema.Tema
import com.universae.reproductor.domain.entities.tema.TemaId
import com.universae.reproductor.domain.entities.tema.TemaRepository
import kotlin.time.Duration

class TemaRepositoryImpl : TemaRepository {

    override fun guardarPuntoParada(alumnoId: AlumnoId, temaId: TemaId, puntoParada: Int): Boolean {
        TODO("Not yet implemented")
    }

    override fun marcarTemaComoEscuchado(alumnoId: AlumnoId, temaId: TemaId): Boolean {
        TODO("Not yet implemented")
    }

    override fun obtenerTema(alumnoId: AlumnoId, temaId: TemaId): Tema? {
        TODO("Not yet implemented")
    }

}