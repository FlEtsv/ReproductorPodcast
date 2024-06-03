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

val urlAudio = "https://drive.google.com/uc?export=download&id=12j5NkdFRbmQYmZK1xbIQeU8oGbsgc4s8"

val comunicacionYAtencionCliente: String = "https://drive.google.com/uc?export=download&id=1mOjxI4Ra2tifnO_UQasQwpizv0Bfsv4k"
val GestionDocumentacionJuridicaEmpresarial: String = "https://drive.google.com/uc?export=download&id=1Ni9H8Fgqm8pD1CSa-TtCchFd7ZFRU4Fv"
val OfimaticaProcesoInformacion: String = "https://drive.google.com/uc?export=download&id=1hit5Bt2DbAYrgllasDwn82xTL5F8rD3o"
val GestionEconFinEmpresa: String = "https://drive.google.com/uc?export=download&id=1-EdJarOIXT3iWPM3DmNmBcOywStMstj1"
val LogAlmacenamiento: String = "https://drive.google.com/uc?export=download&id=1EMhvAS9zuLf70zYqumqpt-NP5m3AuMml"
val GestAdministrativaComercioInternacional: String = "https://drive.google.com/uc?export=download&id=16XQGixUuZOrUDXJ8L0da3U_dTr0mlbDs"
val ProgMultiDispoMoviles: String = "https://drive.google.com/uc?export=download&id=12Gq9C7ZBnSlgOX3KNKuQRX7tzWj-MR4U"
val ProgServiciosProcesos: String = "https://drive.google.com/uc?export=download&id=1uN55RvDvEE0WOew84_0JwAtE69y9ndG6"
val DesarrolloInterfaces: String = "https://drive.google.com/uc?export=download&id=16nhQAAkDfAxspP2idgqjTPhcVT3REFAy"
val DispoVentaProductos: String = "https://drive.google.com/uc?export=download&id=1utuGJbk9OWDO5F0b7hdDXOeRZ-Q8Lvg2"
val DispProdFarmaceuticos: String = "https://drive.google.com/uc?export=download&id=1Rmu9Ui7oZtLuSzKGSzn-Te9OihTxOw4R"
val Anatomofisiologia: String = "https://drive.google.com/uc?export=download&id=1tFYyquBJp108ypOY-ETMzVWkssSLzkRh"
val EmpresaIniciativaEmprendedora: String = "https://drive.google.com/uc?export=download&id=1djwzW1gipFG1hEyfb_o9K6qVssunzNnc"
val FOL:String = "https://drive.google.com/uc?export=download&id=1PuMVfH2gL_kA4uirfaiIi8iBvMV-4Dle"

val PreviewTemas = listOf(
    Tema(
        temaId = TemaId(1),
        nombreTema = "Tema 1",
        descripcionTema = "descripcion1",
        duracionAudio = 120.toDuration(DurationUnit.SECONDS),
        audioUrl = urlAudio,
        imagenUrl = comunicacionYAtencionCliente,
        trackNumber = 1,
        terminado = true
    ),
    Tema(
        temaId = TemaId(2),
        nombreTema = "Tema 2",
        descripcionTema = "descripcion2",
        duracionAudio = 130.toDuration(DurationUnit.SECONDS),
        audioUrl = urlAudio,
        imagenUrl = comunicacionYAtencionCliente,
        trackNumber = 2
    ),
    Tema(
        temaId = TemaId(3),
        nombreTema = "Tema 3",
        descripcionTema = "descripcion3",
        duracionAudio = 140.toDuration(DurationUnit.SECONDS),
        audioUrl = urlAudio,
        imagenUrl = comunicacionYAtencionCliente,
        trackNumber = 3
    ),
    Tema(
        temaId = TemaId(4),
        nombreTema = "Tema 4",
        descripcionTema = "descripcion4",
        duracionAudio = 150.toDuration(DurationUnit.SECONDS),
        audioUrl = urlAudio,
        imagenUrl = GestionDocumentacionJuridicaEmpresarial,
        trackNumber = 1
    ),
    Tema(
        temaId = TemaId(5),
        nombreTema = "Tema 5",
        descripcionTema = "descripcion5",
        duracionAudio = 160.toDuration(DurationUnit.SECONDS),
        audioUrl = urlAudio,
        imagenUrl = GestionDocumentacionJuridicaEmpresarial,
        trackNumber = 2
    ),
    Tema(
        temaId = TemaId(6),
        nombreTema = "Tema 6",
        descripcionTema = "descripcion6",
        duracionAudio = 170.toDuration(DurationUnit.SECONDS),
        audioUrl = urlAudio,
        imagenUrl = OfimaticaProcesoInformacion,
        trackNumber = 1
    ),
    Tema(
        temaId = TemaId(7),
        nombreTema = "Tema 7",
        descripcionTema = "descripcion7",
        duracionAudio = 180.toDuration(DurationUnit.SECONDS),
        audioUrl = urlAudio,
        imagenUrl = OfimaticaProcesoInformacion,
        trackNumber = 2
    ),
    Tema(
        temaId = TemaId(8),
        nombreTema = "Tema 8",
        descripcionTema = "descripcion8",
        duracionAudio = 190.toDuration(DurationUnit.SECONDS),
        audioUrl = urlAudio,
        imagenUrl = OfimaticaProcesoInformacion,
        trackNumber = 3
    ),
    Tema(
        temaId = TemaId(9),
        nombreTema = "Tema 9",
        descripcionTema = "descripcion9",
        duracionAudio = 200.toDuration(DurationUnit.SECONDS),
        audioUrl = urlAudio,
        imagenUrl = GestionEconFinEmpresa,
        trackNumber = 1
    ),
    Tema(
        temaId = TemaId(10),
        nombreTema = "Tema 10",
        descripcionTema = "descripcion10",
        duracionAudio = 210.toDuration(DurationUnit.SECONDS),
        audioUrl = urlAudio,
        imagenUrl = GestionEconFinEmpresa,
        trackNumber = 2
    ),
    Tema(
        temaId = TemaId(11),
        nombreTema = "Tema 11",
        descripcionTema = "descripcion11",
        duracionAudio = 220.toDuration(DurationUnit.SECONDS),
        audioUrl = urlAudio,
        imagenUrl = LogAlmacenamiento,
        trackNumber = 1,
        terminado = true
    ),
    Tema(
        temaId = TemaId(12),
        nombreTema = "Tema 12",
        descripcionTema = "descripcion12",
        duracionAudio = 230.toDuration(DurationUnit.SECONDS),
        audioUrl = urlAudio,
        imagenUrl = GestAdministrativaComercioInternacional,
        trackNumber = 1
    ),
    Tema(
        temaId = TemaId(13),
        nombreTema = "Tema 13",
        descripcionTema = "descripcion13",
        duracionAudio = 240.toDuration(DurationUnit.SECONDS),
        audioUrl = urlAudio,
        imagenUrl = DesarrolloInterfaces,
        trackNumber = 1
    ),
    Tema(
        temaId = TemaId(14),
        nombreTema = "Tema 14",
        descripcionTema = "descripcion14",
        duracionAudio = 250.toDuration(DurationUnit.SECONDS),
        audioUrl = urlAudio,
        imagenUrl = ProgMultiDispoMoviles,
        trackNumber = 1
    ),
    Tema(
        temaId = TemaId(15),
        nombreTema = "Tema 15",
        descripcionTema = "descripcion15",
        duracionAudio = 260.toDuration(DurationUnit.SECONDS),
        audioUrl = urlAudio,
        imagenUrl = ProgServiciosProcesos,
        trackNumber = 1
    ),
    Tema(
        temaId = TemaId(16),
        nombreTema = "Tema 16",
        descripcionTema = "descripcion16",
        duracionAudio = 270.toDuration(DurationUnit.SECONDS),
        audioUrl = urlAudio,
        imagenUrl = ProgServiciosProcesos,
        trackNumber = 2
    ),
    Tema(
        temaId = TemaId(17),
        nombreTema = "Tema 17",
        descripcionTema = "descripcion17",
        duracionAudio = 280.toDuration(DurationUnit.SECONDS),
        audioUrl = urlAudio,
        imagenUrl = Anatomofisiologia,
        trackNumber = 1
    ),
    Tema(
        temaId = TemaId(18),
        nombreTema = "Tema 18",
        descripcionTema = "descripcion18",
        duracionAudio = 290.toDuration(DurationUnit.SECONDS),
        audioUrl = urlAudio,
        imagenUrl = DispProdFarmaceuticos,
        trackNumber = 1
    ),
    Tema(
        temaId = TemaId(19),
        nombreTema = "Tema 19",
        descripcionTema = "descripcion19",
        duracionAudio = 300.toDuration(DurationUnit.SECONDS),
        audioUrl = urlAudio,
        imagenUrl = DispoVentaProductos,
        trackNumber = 1
    ),
    Tema(
        temaId = TemaId(20),
        nombreTema = "Tema 20",
        descripcionTema = "descripcion20",
        duracionAudio = 310.toDuration(DurationUnit.SECONDS),
        audioUrl = urlAudio,
        imagenUrl = DispoVentaProductos,
        trackNumber = 2
    ),
    Tema(
        temaId = TemaId(31),
        nombreTema = "Tema 31",
        descripcionTema = "descripcion31",
        duracionAudio = 320.toDuration(DurationUnit.SECONDS),
        audioUrl = urlAudio,
        imagenUrl = EmpresaIniciativaEmprendedora,
        trackNumber = 1,
        terminado = true
    ),
    Tema(
        temaId = TemaId(32),
        nombreTema = "Tema 32",
        descripcionTema = "descripcion32",
        duracionAudio = 330.toDuration(DurationUnit.SECONDS),
        audioUrl = urlAudio,
        imagenUrl = EmpresaIniciativaEmprendedora,
        trackNumber = 2,
        terminado = true
    ),
    Tema(
        temaId = TemaId(33),
        nombreTema = "Tema 33",
        descripcionTema = "descripcion33",
        duracionAudio = 340.toDuration(DurationUnit.SECONDS),
        audioUrl = urlAudio,
        imagenUrl = EmpresaIniciativaEmprendedora,
        trackNumber = 3
    ),
    Tema(
        temaId = TemaId(34),
        nombreTema = "Tema 34",
        descripcionTema = "descripcion34",
        duracionAudio = 350.toDuration(DurationUnit.SECONDS),
        audioUrl = urlAudio,
        imagenUrl = EmpresaIniciativaEmprendedora,
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
        imagenUrl = FOL,
        trackNumber = 1
    ),
    Tema(
        temaId = TemaId(22),
        nombreTema = "Tema 2 FOL",
        descripcionTema = "descripcion2 FOL",
        duracionAudio = 130.toDuration(DurationUnit.SECONDS),
        audioUrl = urlAudio,
        imagenUrl = FOL,
        trackNumber = 2
    ),
    Tema(
        temaId = TemaId(23),
        nombreTema = "Tema 3 FOL",
        descripcionTema = "descripcion3 FOL",
        duracionAudio = 140.toDuration(DurationUnit.SECONDS),
        audioUrl = urlAudio,
        imagenUrl = FOL,
        trackNumber = 3,
        terminado = true
    ),
    Tema(
        temaId = TemaId(24),
        nombreTema = "Tema 4 FOL",
        descripcionTema = "descripcion4 FOL",
        duracionAudio = 150.toDuration(DurationUnit.SECONDS),
        audioUrl = urlAudio,
        imagenUrl = FOL,
        trackNumber = 4
    ),
    Tema(
        temaId = TemaId(25),
        nombreTema = "Tema 5 FOL",
        descripcionTema = "descripcion5 FOL",
        duracionAudio = 160.toDuration(DurationUnit.SECONDS),
        audioUrl = urlAudio,
        imagenUrl = FOL,
        trackNumber = 5
    ),
)

val PreviewAsignaturas = listOf(
    Asignatura(AsignaturaId(1), "Comunicacion y Atencion al Cliente", PreviewTemas.filter{it.temaId.id <= 3}, comunicacionYAtencionCliente),
    Asignatura(AsignaturaId(2), "Gestion de la Documentacion Juridica y Empresarial", PreviewTemas.filter{it.temaId.id in 4..5}, GestionDocumentacionJuridicaEmpresarial),
    Asignatura(AsignaturaId(3), "Ofimática y Proceso de Información", PreviewTemas.filter{it.temaId.id in 6..8}, OfimaticaProcesoInformacion),
    Asignatura(AsignaturaId(4), "Gestión Económica y Financiera de la Empresa", PreviewTemas.filter{it.temaId.id in 9..10}, GestionEconFinEmpresa),
    Asignatura(AsignaturaId(5), "Logística de Almacenamiento", PreviewTemas.filter{it.temaId.id in 11..11}, LogAlmacenamiento),
    Asignatura(AsignaturaId(6), "Gestión Administrativa del comercio internacional", PreviewTemas.filter{it.temaId.id in 12..12}, GestAdministrativaComercioInternacional),
    Asignatura(AsignaturaId(7), "Desarrollo de Interfaces", PreviewTemas.filter{it.temaId.id in 13..13}, DesarrolloInterfaces),
    Asignatura(AsignaturaId(8), "Programación Multimedia y Dispositivos Móviles", PreviewTemas.filter{it.temaId.id in 14..14}, ProgMultiDispoMoviles),
    Asignatura(AsignaturaId(9), "Programación de Servicios y procesos", PreviewTemas.filter{it.temaId.id in 15..16}, ProgServiciosProcesos),
    Asignatura(AsignaturaId(10), "Anatomofisiología y Patología Básicas", PreviewTemas.filter{it.temaId.id in 17..17}, Anatomofisiologia),
    Asignatura(AsignaturaId(11), "Dispensación de productos farmaceúticos", PreviewTemas.filter{it.temaId.id in 18..18}, DispProdFarmaceuticos),
    Asignatura(AsignaturaId(12), "Disposición y Venta de Productos", PreviewTemas.filter{it.temaId.id in 19..20}, DispoVentaProductos),
    Asignatura(AsignaturaId(98), "Formación y Orientación Laboral", PreviewTemasFOL, FOL),
    Asignatura(AsignaturaId(99), "Empresa e iniciativa emprendedora", PreviewTemas.filter{it.temaId.id in 31..34}, EmpresaIniciativaEmprendedora),
)

val PreviewGrados = listOf(
    Grado(GradoId(1), "Administracion Y Finanzas", PreviewAsignaturas.map { it.asignaturaId }.filter { it.id in 1..3 || it.id == 98 || it.id == 99 }, "https://drive.google.com/uc?export=download&id=1JMMom6-nVaFCRO5QTu5wrGXKtumt8KSZ"),
    Grado(GradoId(2), "Comercio Internacional", PreviewAsignaturas.map { it.asignaturaId }.filter { it.id in 4..6 || it.id == 98 || it.id == 99}, "https://drive.google.com/uc?export=download&id=1AUviEHgDJSBPN0BYHSfnZQQDYQu4muR0"),
    Grado(GradoId(3), "Desarrollo De Aplicaciones Multiplataforma", PreviewAsignaturas.map { it.asignaturaId }.filter { it.id in 7..9 || it.id == 98}, "https://drive.google.com/uc?export=download&id=1AdE_vnHVVd-1DTMlHMog9TG83zzR_uCY"),
    Grado(GradoId(4), "Farmacia Y Parafarmacia", PreviewAsignaturas.map { it.asignaturaId }.filter { it.id in 10..12 || it.id == 98 || it.id == 99}, "https://drive.google.com/uc?export=download&id=1XyahEA49jnY-IsMpj67ng1jOVG99E-7N"),
)

val PreviewAlumno = listOf(
    Alumno(
        "Alumno 1",
        "Steven",
        AlumnoId(1),
        PreviewGrados.map { it.gradoId }.filter { it.id == 1 || it.id == 2 } //Alumno matriculado en el grado 1 y 2
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