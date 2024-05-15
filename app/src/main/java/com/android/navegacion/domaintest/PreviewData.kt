package com.universae.reproductor.domaintest

import com.universae.reproductor.domain.entities.alumno.Alumno
import com.universae.reproductor.domain.entities.alumno.AlumnoId
import com.universae.reproductor.domain.entities.asignatura.Asignatura
import com.universae.reproductor.domain.entities.asignatura.AsignaturaId
import com.universae.reproductor.domain.entities.grado.Grado
import com.universae.reproductor.domain.entities.grado.GradoId
import com.universae.reproductor.domain.entities.tema.Tema
import com.universae.reproductor.domain.entities.tema.TemaId
import kotlin.time.DurationUnit
import kotlin.time.toDuration

val PreviewTemas = listOf(
    Tema(
        TemaId(1),
        "Tema 1",
        "descripcion1",
        120.toDuration(DurationUnit.SECONDS),
        "https://www.youtube.com/watch?v=1"
    ),
    Tema(
        TemaId(2),
        "Tema 2",
        "descripcion2",
        13000.toDuration(DurationUnit.SECONDS),
        "https://www.youtube.com/watch?v=2"
    ),
    Tema(
        TemaId(3),
        "Tema 3",
        "descripcion3",
        140.toDuration(DurationUnit.SECONDS),
        "https://www.youtube.com/watch?v=3"
    ),
    Tema(
        TemaId(4),
        "Tema 4",
        "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris ",
        150.toDuration(DurationUnit.SECONDS),
        "https://www.youtube.com/watch?v=4"
    ),
    Tema(
        TemaId(5),
        "Tema 5",
        "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum",
        160.toDuration(DurationUnit.SECONDS),
        "https://www.youtube.com/watch?v=5"
    ),
    Tema(
        TemaId(6),
        "Tema 6",
        "descripcion6",
        170.toDuration(DurationUnit.SECONDS),
        "https://www.youtube.com/watch?v=6"
    ),
    Tema(
        TemaId(7),
        "Tema 7",
        "descripcion7",
        180.toDuration(DurationUnit.SECONDS),
        "https://www.youtube.com/watch?v=7"
    ),
    Tema(
        TemaId(8),
        "Tema 8",
        "descripcion8",
        190.toDuration(DurationUnit.SECONDS),
        "https://www.youtube.com/watch?v=8"
    ),
    Tema(
        TemaId(9),
        "Tema 9",
        "descripcion9",
        200.toDuration(DurationUnit.SECONDS),
        "https://www.youtube.com/watch?v=9"
    ),
    Tema(
        TemaId(10),
        "Tema 10",
        "descripcion10",
        210.toDuration(DurationUnit.SECONDS),
        "https://www.youtube.com/watch?v=10"
    )
)

val PreviewAsignaturas = listOf(
    Asignatura(AsignaturaId(1), "Asignatura 1", PreviewTemas, "icon"),
    Asignatura(AsignaturaId(2), "Asignatura 2", PreviewTemas, "icon"),
    Asignatura(AsignaturaId(3), "Asignatura 3", PreviewTemas, "icon"),
    Asignatura(AsignaturaId(4), "Asignatura 4", PreviewTemas, "icon"),
)

val PreviewGrados = listOf(
    Grado(GradoId(1), "Grado 1", PreviewAsignaturas.map { it.asignaturaId }, "icon"),
    Grado(GradoId(2), "Grado 2", PreviewAsignaturas.map { it.asignaturaId }, "icon"),
    Grado(GradoId(3), "Grado 3", PreviewAsignaturas.map { it.asignaturaId }, "icon"),
    Grado(GradoId(4), "Grado 4", PreviewAsignaturas.map { it.asignaturaId }, "icon"),
)

val PreviewAlumno = listOf(
    Alumno(
        "Alumno 1",
        AlumnoId(1),
        PreviewGrados.map { it.gradoId }.filter { it.id <= 2 }
    ),
    Alumno(
        "Prueba",
        AlumnoId(2),
        PreviewGrados.map { it.gradoId }.filter { it.id == 3 }
    ),
    Alumno(
        "Alumno 3",
        AlumnoId(3),
        PreviewGrados.map { it.gradoId }.filter { it.id >= 3 }
    )
)
