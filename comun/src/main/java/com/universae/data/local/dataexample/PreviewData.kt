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

var urlAudio = "https://file-examples.com/storage/fe83e1f11c664c2259506f1/2017/11/file_example_MP3_700KB.mp3"

val PreviewTemas = listOf(
    Tema(
        TemaId(1),
        "Tema 1",
        "descripcion1",
        120.toDuration(DurationUnit.SECONDS),
        urlAudio
    ),
    Tema(
        TemaId(2),
        "Tema 2",
        "descripcion2",
        13000.toDuration(DurationUnit.SECONDS),
        urlAudio
    ),
    Tema(
        TemaId(3),
        "Tema 3",
        "descripcion3",
        140.toDuration(DurationUnit.SECONDS),
        urlAudio
    ),
    Tema(
        TemaId(4),
        "Tema 4",
        "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris ",
        150.toDuration(DurationUnit.SECONDS),
        urlAudio
    ),
    Tema(
        TemaId(5),
        "Tema 5",
        "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum",
        160.toDuration(DurationUnit.SECONDS),
        urlAudio
    ),
    Tema(
        TemaId(6),
        "Tema 6",
        "descripcion6",
        170.toDuration(DurationUnit.SECONDS),
        urlAudio
    ),
    Tema(
        TemaId(7),
        "Tema 7",
        "descripcion7",
        180.toDuration(DurationUnit.SECONDS),
        urlAudio
    ),
    Tema(
        TemaId(8),
        "Tema 8",
        "descripcion8",
        190.toDuration(DurationUnit.SECONDS),
        urlAudio
    ),
    Tema(
        TemaId(9),
        "Tema 9",
        "descripcion9",
        200.toDuration(DurationUnit.SECONDS),
        urlAudio
    ),
    Tema(
        TemaId(10),
        "Tema 10",
        "descripcion10",
        210.toDuration(DurationUnit.SECONDS),
        urlAudio
    ),
    Tema(
        TemaId(11),
        "Tema 11",
        "descripcion11",
        220.toDuration(DurationUnit.SECONDS),
        urlAudio
    ),
    Tema(
        TemaId(12),
        "Tema 12",
        "descripcion12",
        230.toDuration(DurationUnit.SECONDS),
        urlAudio
    ),
    Tema(
        TemaId(13),
        "Tema 13",
        "descripcion13",
        240.toDuration(DurationUnit.SECONDS),
        urlAudio
    ),
    Tema(
        TemaId(14),
        "Tema 14",
        "descripcion14",
        250.toDuration(DurationUnit.SECONDS),
        urlAudio
    ),
    Tema(
        TemaId(15),
        "Tema 15",
        "descripcion15",
        260.toDuration(DurationUnit.SECONDS),
        urlAudio
    ),
    Tema(
        TemaId(16),
        "Tema 16",
        "descripcion16",
        270.toDuration(DurationUnit.SECONDS),
        urlAudio
    ),
    Tema(
        TemaId(17),
        "Tema 17",
        "descripcion17",
        280.toDuration(DurationUnit.SECONDS),
        urlAudio
    ),
    Tema(
        TemaId(18),
        "Tema 18",
        "descripcion18",
        290.toDuration(DurationUnit.SECONDS),
        urlAudio
    ),
    Tema(
        TemaId(19),
        "Tema 19",
        "descripcion19",
        300.toDuration(DurationUnit.SECONDS),
        urlAudio
    ),
    Tema(
        TemaId(20),
        "Tema 20",
        "descripcion20",
        310.toDuration(DurationUnit.SECONDS),
        urlAudio
    ),
    Tema(
        TemaId(31),
        "Tema 21",
        "descripcion21",
        320.toDuration(DurationUnit.SECONDS),
        urlAudio
    ),
    Tema(
        TemaId(32),
        "Tema 22",
        "descripcion22",
        330.toDuration(DurationUnit.SECONDS),
        urlAudio
    ),
    Tema(
        TemaId(33),
        "Tema 23",
        "descripcion23",
        340.toDuration(DurationUnit.SECONDS),
        urlAudio
    ),
    Tema(
        TemaId(34),
        "Tema 24",
        "descripcion24",
        350.toDuration(DurationUnit.SECONDS),
        urlAudio
    ),
)

val PreviewTemasFOL = listOf(
    Tema(
        TemaId(21),
        "Tema 1 FOL",
        "descripcion1",
        120.toDuration(DurationUnit.SECONDS),
        urlAudio
    ),
    Tema(
        TemaId(22),
        "Tema 2 FOL",
        "descripcion2",
        13000.toDuration(DurationUnit.SECONDS),
        urlAudio
    ),
    Tema(
        TemaId(23),
        "Tema 3 FOL",
        "descripcion3",
        140.toDuration(DurationUnit.SECONDS),
        urlAudio
    ),
    Tema(
        TemaId(24),
        "Tema 4 FOL",
        "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris ",
        150.toDuration(DurationUnit.SECONDS),
        urlAudio
    ),
    Tema(
        TemaId(25),
        "Tema 5 FOL",
        "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum",
        160.toDuration(DurationUnit.SECONDS),
        urlAudio
    )
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
