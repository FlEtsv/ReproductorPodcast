package com.universae.reproductor.domaintest

import com.universae.reproductor.domain.entities.alumno.Alumno
import com.universae.reproductor.domain.entities.alumno.AlumnoId
import com.universae.domain.entities.asignatura.Asignatura
import com.universae.domain.entities.asignatura.AsignaturaId
import com.universae.reproductor.domain.entities.grado.Grado
import com.universae.reproductor.domain.entities.grado.GradoId
import com.universae.reproductor.domain.entities.tema.Tema
import com.universae.reproductor.domain.entities.tema.TemaId
import kotlin.time.DurationUnit
import kotlin.time.toDuration

val urlAudio = "https://file-examples.com/storage/fe9037cdb6665870598c2d1/2017/11/file_example_MP3_5MG.mp3"
val urlImagen = "https://estaticos-cdn.prensaiberica.es/epi/public/file/2023/0804/12/universae-f534810.png"

val PreviewTemas = listOf(
    Tema(
        temaId = TemaId(1),
        nombreTema = "Tema 1",
        descripcionTema = "descripcion1",
        duracionAudio = 120.toDuration(DurationUnit.SECONDS),
        audioUrl = urlAudio,
        imagenUrl = urlImagen,
        trackNumber = 1,
        terminado = true
    ),
    Tema(
        temaId = TemaId(2),
        nombreTema = "Tema 2",
        descripcionTema = "descripcion2",
        duracionAudio = 130.toDuration(DurationUnit.SECONDS),
        audioUrl = urlAudio,
        imagenUrl = urlImagen,
        trackNumber = 2
    ),
    Tema(
        temaId = TemaId(3),
        nombreTema = "Tema 3",
        descripcionTema = "descripcion3",
        duracionAudio = 140.toDuration(DurationUnit.SECONDS),
        audioUrl = urlAudio,
        imagenUrl = urlImagen,
        trackNumber = 3
    ),
    Tema(
        temaId = TemaId(4),
        nombreTema = "Tema 4",
        descripcionTema = "descripcion4",
        duracionAudio = 150.toDuration(DurationUnit.SECONDS),
        audioUrl = urlAudio,
        imagenUrl = urlImagen,
        trackNumber = 1
    ),
    Tema(
        temaId = TemaId(5),
        nombreTema = "Tema 5",
        descripcionTema = "descripcion5",
        duracionAudio = 160.toDuration(DurationUnit.SECONDS),
        audioUrl = urlAudio,
        imagenUrl = urlImagen,
        trackNumber = 2
    ),
    Tema(
        temaId = TemaId(6),
        nombreTema = "Tema 6",
        descripcionTema = "descripcion6",
        duracionAudio = 170.toDuration(DurationUnit.SECONDS),
        audioUrl = urlAudio,
        imagenUrl = urlImagen,
        trackNumber = 1
    ),
    Tema(
        temaId = TemaId(7),
        nombreTema = "Tema 7",
        descripcionTema = "descripcion7",
        duracionAudio = 180.toDuration(DurationUnit.SECONDS),
        audioUrl = urlAudio,
        imagenUrl = urlImagen,
        trackNumber = 2
    ),
    Tema(
        temaId = TemaId(8),
        nombreTema = "Tema 8",
        descripcionTema = "descripcion8",
        duracionAudio = 190.toDuration(DurationUnit.SECONDS),
        audioUrl = urlAudio,
        imagenUrl = urlImagen,
        trackNumber = 3
    ),
    Tema(
        temaId = TemaId(9),
        nombreTema = "Tema 9",
        descripcionTema = "descripcion9",
        duracionAudio = 200.toDuration(DurationUnit.SECONDS),
        audioUrl = urlAudio,
        imagenUrl = urlImagen,
        trackNumber = 1
    ),
    Tema(
        temaId = TemaId(10),
        nombreTema = "Tema 10",
        descripcionTema = "descripcion10",
        duracionAudio = 210.toDuration(DurationUnit.SECONDS),
        audioUrl = urlAudio,
        imagenUrl = urlImagen,
        trackNumber = 2
    ),
    Tema(
        temaId = TemaId(11),
        nombreTema = "Tema 11",
        descripcionTema = "descripcion11",
        duracionAudio = 220.toDuration(DurationUnit.SECONDS),
        audioUrl = urlAudio,
        imagenUrl = urlImagen,
        trackNumber = 1
    ),
    Tema(
        temaId = TemaId(12),
        nombreTema = "Tema 12",
        descripcionTema = "descripcion12",
        duracionAudio = 230.toDuration(DurationUnit.SECONDS),
        audioUrl = urlAudio,
        imagenUrl = urlImagen,
        trackNumber = 1
    ),
    Tema(
        temaId = TemaId(13),
        nombreTema = "Tema 13",
        descripcionTema = "descripcion13",
        duracionAudio = 240.toDuration(DurationUnit.SECONDS),
        audioUrl = urlAudio,
        imagenUrl = urlImagen,
        trackNumber = 1
    ),
    Tema(
        temaId = TemaId(14),
        nombreTema = "Tema 14",
        descripcionTema = "descripcion14",
        duracionAudio = 250.toDuration(DurationUnit.SECONDS),
        audioUrl = urlAudio,
        imagenUrl = urlImagen,
        trackNumber = 1
    ),
    Tema(
        temaId = TemaId(15),
        nombreTema = "Tema 15",
        descripcionTema = "descripcion15",
        duracionAudio = 260.toDuration(DurationUnit.SECONDS),
        audioUrl = urlAudio,
        imagenUrl = urlImagen,
        trackNumber = 1
    ),
    Tema(
        temaId = TemaId(16),
        nombreTema = "Tema 16",
        descripcionTema = "descripcion16",
        duracionAudio = 270.toDuration(DurationUnit.SECONDS),
        audioUrl = urlAudio,
        imagenUrl = urlImagen,
        trackNumber = 2
    ),
    Tema(
        temaId = TemaId(17),
        nombreTema = "Tema 17",
        descripcionTema = "descripcion17",
        duracionAudio = 280.toDuration(DurationUnit.SECONDS),
        audioUrl = urlAudio,
        imagenUrl = urlImagen,
        trackNumber = 1
    ),
    Tema(
        temaId = TemaId(18),
        nombreTema = "Tema 18",
        descripcionTema = "descripcion18",
        duracionAudio = 290.toDuration(DurationUnit.SECONDS),
        audioUrl = urlAudio,
        imagenUrl = urlImagen,
        trackNumber = 1
    ),
    Tema(
        temaId = TemaId(19),
        nombreTema = "Tema 19",
        descripcionTema = "descripcion19",
        duracionAudio = 300.toDuration(DurationUnit.SECONDS),
        audioUrl = urlAudio,
        imagenUrl = urlImagen,
        trackNumber = 1
    ),
    Tema(
        temaId = TemaId(20),
        nombreTema = "Tema 20",
        descripcionTema = "descripcion20",
        duracionAudio = 310.toDuration(DurationUnit.SECONDS),
        audioUrl = urlAudio,
        imagenUrl = urlImagen,
        trackNumber = 2
    ),
    Tema(
        temaId = TemaId(31),
        nombreTema = "Tema 31",
        descripcionTema = "descripcion31",
        duracionAudio = 320.toDuration(DurationUnit.SECONDS),
        audioUrl = urlAudio,
        imagenUrl = urlImagen,
        trackNumber = 1
    ),
    Tema(
        temaId = TemaId(32),
        nombreTema = "Tema 32",
        descripcionTema = "descripcion32",
        duracionAudio = 330.toDuration(DurationUnit.SECONDS),
        audioUrl = urlAudio,
        imagenUrl = urlImagen,
        trackNumber = 2
    ),
    Tema(
        temaId = TemaId(33),
        nombreTema = "Tema 33",
        descripcionTema = "descripcion33",
        duracionAudio = 340.toDuration(DurationUnit.SECONDS),
        audioUrl = urlAudio,
        imagenUrl = urlImagen,
        trackNumber = 3
    ),
    Tema(
        temaId = TemaId(34),
        nombreTema = "Tema 34",
        descripcionTema = "descripcion34",
        duracionAudio = 350.toDuration(DurationUnit.SECONDS),
        audioUrl = urlAudio,
        imagenUrl = urlImagen,
        trackNumber = 4
    )
)

val PreviewTemasFOL = listOf(
    Tema(
        temaId = TemaId(21),
        nombreTema = "Tema 1 FOL",
        descripcionTema = "descripcion1 FOL",
        duracionAudio = 120.toDuration(DurationUnit.SECONDS),
        audioUrl = urlAudio,
        imagenUrl = urlImagen,
        trackNumber = 1
    ),
    Tema(
        temaId = TemaId(22),
        nombreTema = "Tema 2 FOL",
        descripcionTema = "descripcion2 FOL",
        duracionAudio = 130.toDuration(DurationUnit.SECONDS),
        audioUrl = urlAudio,
        imagenUrl = urlImagen,
        trackNumber = 2
    ),
    Tema(
        temaId = TemaId(23),
        nombreTema = "Tema 3 FOL",
        descripcionTema = "descripcion3 FOL",
        duracionAudio = 140.toDuration(DurationUnit.SECONDS),
        audioUrl = urlAudio,
        imagenUrl = urlImagen,
        trackNumber = 3
    ),
    Tema(
        temaId = TemaId(24),
        nombreTema = "Tema 4 FOL",
        descripcionTema = "descripcion4 FOL",
        duracionAudio = 150.toDuration(DurationUnit.SECONDS),
        audioUrl = urlAudio,
        imagenUrl = urlImagen,
        trackNumber = 4
    ),
    Tema(
        temaId = TemaId(25),
        nombreTema = "Tema 5 FOL",
        descripcionTema = "descripcion5 FOL",
        duracionAudio = 160.toDuration(DurationUnit.SECONDS),
        audioUrl = urlAudio,
        imagenUrl = urlImagen,
        trackNumber = 5
    ),
)

val PreviewAsignaturas = listOf(
    Asignatura(AsignaturaId(1), "Asignatura 1", PreviewTemas.filter{it.temaId.id <= 3}, "icon"),
    Asignatura(AsignaturaId(2), "Asignatura 2", PreviewTemas.filter{it.temaId.id in 4..5}, "icon"),
    Asignatura(AsignaturaId(3), "Asignatura 3", PreviewTemas.filter{it.temaId.id in 6..8}, "icon"),
    Asignatura(AsignaturaId(4), "Asignatura 4", PreviewTemas.filter{it.temaId.id in 9..10}, "icon"),
    Asignatura(AsignaturaId(5), "Asignatura 5", PreviewTemas.filter{it.temaId.id in 11..11}, "icon"),
    Asignatura(AsignaturaId(6), "Asignatura 6", PreviewTemas.filter{it.temaId.id in 12..12}, "icon"),
    Asignatura(AsignaturaId(7), "Asignatura 7", PreviewTemas.filter{it.temaId.id in 13..13}, "icon"),
    Asignatura(AsignaturaId(8), "Asignatura 8", PreviewTemas.filter{it.temaId.id in 14..14}, "icon"),
    Asignatura(AsignaturaId(9), "Asignatura 9", PreviewTemas.filter{it.temaId.id in 15..16}, "icon"),
    Asignatura(AsignaturaId(10), "Asignatura 10", PreviewTemas.filter{it.temaId.id in 17..17}, "icon"),
    Asignatura(AsignaturaId(11), "Asignatura 11", PreviewTemas.filter{it.temaId.id in 18..18}, "icon"),
    Asignatura(AsignaturaId(12), "Asignatura 12", PreviewTemas.filter{it.temaId.id in 19..20}, "icon"),
    Asignatura(AsignaturaId(98), "FOL", PreviewTemasFOL, "icon"),
    Asignatura(AsignaturaId(99), "EMPRESA", PreviewTemas.filter{it.temaId.id in 31..34}, "icon"),
)

val PreviewGrados = listOf(
    Grado(GradoId(1), "Grado 1", PreviewAsignaturas.map { it.asignaturaId }.filter { it.id in 1..3 || it.id == 98 || it.id == 99 }, "icon"),
    Grado(GradoId(2), "Grado 2", PreviewAsignaturas.map { it.asignaturaId }.filter { it.id in 4..6 || it.id == 98 || it.id == 99}, "icon"),
    Grado(GradoId(3), "Grado 3", PreviewAsignaturas.map { it.asignaturaId }.filter { it.id == 98}, "icon"),
    Grado(GradoId(4), "Grado 4", PreviewAsignaturas.map { it.asignaturaId }.filter { it.id in 10..12 || it.id == 98 || it.id == 99}, "icon"),
)

val PreviewAlumno = listOf(
    Alumno(
        "Alumno 1",
        "Steven",
        AlumnoId(1),
        PreviewGrados.map { it.gradoId }.filter { it.id == 1 || it.id == 4 } //Alumno matriculado en el grado 1 y 2
    ),
    Alumno(
        "Prueba",
        "Gabriel",
        AlumnoId(2),
        PreviewGrados.map { it.gradoId }.filter { it.id == 3 } //Alumno matriculado en el grado 3
    ),
    Alumno(
        "Alumno 3",
        "Alberto",
        AlumnoId(3),
        PreviewGrados.map { it.gradoId }.filter { it.id >= 3 } //Alumno matriculado en los grados desde el 3 al último (4, en este caso)
    )
)
