package com.universae.data.local.dataexample

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

const val URL_AUDIO = "https://dl.dropboxusercontent.com/scl/fi/4notzhccgk05jkzcrsg2t/file_example_MP3_1MG.mp3?rlkey=hnvrs05q28fqa2k0wotqf5ph5&st=dg4e706q&dl=0"

const val COMUNICACION_ATENCION_CLIENTE: String = "https://dl.dropboxusercontent.com/scl/fi/qoiiantse3hgf91hs1gn6/Comunicacion_y_Atencion_Cliente.png?rlkey=qt6m38euc9gmmnvk5s9bfx1g0&st=n2vog98a&dl=0"
const val GESTION_DOCUMENTACION_JURIDICA_EMPRESARIAL: String = "https://dl.dropboxusercontent.com/scl/fi/3hyxg32bl5fiolxe6w12y/Gesti-n-de-la-Documentaci-n-Jur-dica-y-Empresarial.png?rlkey=2kj6x6jca0usixgyfkdpsngoc&st=8p86txul&dl=0"
const val OFIMATICA_PROCESO_INFORMACION: String = "https://dl.dropboxusercontent.com/scl/fi/1l1sapioe8575brar8ofh/Ofim-tica-y-Proceso-de-Informaci-n.png?rlkey=5fuvahrjhrbp24npob6oljx6y&st=lmtcktjw&dl=0"
const val GESTION_ECON_FIN_EMPRESA: String = "https://dl.dropboxusercontent.com/scl/fi/abgz372dc5ir6g2iugyou/Gesti-n-Econ-mica-y-Financiera-de-la-Empresa.png?rlkey=o1q3gn5vqbj8etoz1tnpo6t4p&st=twlwlwct&dl=0"
const val LOG_ALMACENAMIENTO: String = "https://dl.dropboxusercontent.com/scl/fi/m6xa8cqkh5wksangakh1v/Log-stica-de-Almacenamiento.png?rlkey=z33ox84s5oiy9m6505uga87h5&st=gwx93kf1&dl=0"
const val GEST_ADMINISTRATIVA_COMERCIO_INTERNACIONAL: String = "https://dl.dropboxusercontent.com/scl/fi/zfdjq8aisafykpjmgp4x8/Gesti-n-Administrativa-del-comercio-internacional.png?rlkey=kulbmz6aznayc7rex0gtvc8al&st=ecu1cwct&dl=0"
const val PROG_MULTI_DISPO_MOVILES: String = "https://dl.dropboxusercontent.com/scl/fi/xn0uk600bdhh9jaue1nmq/Programaci-n-Multimedia-y-Dispositivos-M-viles.png?rlkey=qtdfbupvy8wlfkxbs5tv9e0lo&st=l3c1aa5g&dl=0"
const val PROG_SERVICIOS_PROCESOS: String = "https://dl.dropboxusercontent.com/scl/fi/08bco3lc31a9p1mux5g26/Programaci-n-de-Servicios-y-procesos.png?rlkey=2syi969179zgsta4xto2qdens&st=ls6an42e&dl=0"
const val DESARROLLO_INTERFACES: String = "https://dl.dropboxusercontent.com/scl/fi/skaxdelc2y3mj0x455znq/Desarrollo-de-Interfaces.png?rlkey=80xx9upunkx9qy59llfacpcg1&st=94tsgay5&dl=0"
const val DISPO_VENTA_PRODUCTOS: String = "https://dl.dropboxusercontent.com/scl/fi/sp16c4m7ypwvflc9irqn2/Disposici-n-y-Venta-de-Productos.png?rlkey=99ih0jd09n5iya17znyeqi6hp&st=7gvyxwtb&dl=0"
const val DISP_PROD_FARMACEUTICOS: String = "https://dl.dropboxusercontent.com/scl/fi/68n0zh0u6i4r7svcjikgz/Dispensaci-n-de-productos-farmace-ticos.png?rlkey=xfy5nd0pnjnmekal6zmihxpeq&st=3dcegi3g&dl=0"
const val ANATOMOFISIOLOGIA: String = "https://dl.dropboxusercontent.com/scl/fi/ctcmoe3spu7xpd2vt165u/Anatomofisiolog-a-y-Patolog-a-B-sicas.png?rlkey=7oxsa7jg60qh1rd9pn3x0tji0&st=aakwo3v4&dl=0"
const val EMPRESA_INICIATIVA_EMPRENDEDORA: String = "https://dl.dropboxusercontent.com/scl/fi/jkkr7te0huynjt4hls859/Empresa-e-iniciativa-emprendedora_12.png?rlkey=jem388z4mkfkvibl9h5t3r7dd&st=0sers05q&dl=0"
const val FOL:String = "https://dl.dropboxusercontent.com/scl/fi/blsxpoa2cy5jqpukyfmxe/Formaci-n-y-Orientaci-n-Laboral-_24.png?rlkey=wmbt3n838kz81mn05i70dym0j&st=2b3tq6e5&dl=0"

val PreviewTemas = listOf(
    Tema(
        temaId = TemaId(1),
        nombreTema = "Tema 1",
        descripcionTema = "descripcion1 lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.",
        duracionAudio = 120.toDuration(DurationUnit.SECONDS),
        audioUrl = URL_AUDIO,
        imagenUrl = COMUNICACION_ATENCION_CLIENTE,
        trackNumber = 1,
        terminado = true
    ),
    Tema(
        temaId = TemaId(2),
        nombreTema = "Tema 2",
        descripcionTema = "descripcion2",
        duracionAudio = 130.toDuration(DurationUnit.SECONDS),
        audioUrl = URL_AUDIO,
        imagenUrl = COMUNICACION_ATENCION_CLIENTE,
        trackNumber = 2
    ),
    Tema(
        temaId = TemaId(3),
        nombreTema = "Tema 3",
        descripcionTema = "descripcion3",
        duracionAudio = 140.toDuration(DurationUnit.SECONDS),
        audioUrl = URL_AUDIO,
        imagenUrl = COMUNICACION_ATENCION_CLIENTE,
        trackNumber = 3
    ),
    Tema(
        temaId = TemaId(4),
        nombreTema = "Tema 4",
        descripcionTema = "descripcion4",
        duracionAudio = 150.toDuration(DurationUnit.SECONDS),
        audioUrl = URL_AUDIO,
        imagenUrl = GESTION_DOCUMENTACION_JURIDICA_EMPRESARIAL,
        trackNumber = 1
    ),
    Tema(
        temaId = TemaId(5),
        nombreTema = "Tema 5",
        descripcionTema = "descripcion5",
        duracionAudio = 160.toDuration(DurationUnit.SECONDS),
        audioUrl = URL_AUDIO,
        imagenUrl = GESTION_DOCUMENTACION_JURIDICA_EMPRESARIAL,
        trackNumber = 2
    ),
    Tema(
        temaId = TemaId(6),
        nombreTema = "Tema 6",
        descripcionTema = "descripcion6",
        duracionAudio = 170.toDuration(DurationUnit.SECONDS),
        audioUrl = URL_AUDIO,
        imagenUrl = OFIMATICA_PROCESO_INFORMACION,
        trackNumber = 1
    ),
    Tema(
        temaId = TemaId(7),
        nombreTema = "Tema 7",
        descripcionTema = "descripcion7",
        duracionAudio = 180.toDuration(DurationUnit.SECONDS),
        audioUrl = URL_AUDIO,
        imagenUrl = OFIMATICA_PROCESO_INFORMACION,
        trackNumber = 2
    ),
    Tema(
        temaId = TemaId(8),
        nombreTema = "Tema 8",
        descripcionTema = "descripcion8",
        duracionAudio = 190.toDuration(DurationUnit.SECONDS),
        audioUrl = URL_AUDIO,
        imagenUrl = OFIMATICA_PROCESO_INFORMACION,
        trackNumber = 3
    ),
    Tema(
        temaId = TemaId(9),
        nombreTema = "Tema 9",
        descripcionTema = "descripcion9",
        duracionAudio = 200.toDuration(DurationUnit.SECONDS),
        audioUrl = URL_AUDIO,
        imagenUrl = GESTION_ECON_FIN_EMPRESA,
        trackNumber = 1
    ),
    Tema(
        temaId = TemaId(10),
        nombreTema = "Tema 10",
        descripcionTema = "descripcion10",
        duracionAudio = 210.toDuration(DurationUnit.SECONDS),
        audioUrl = URL_AUDIO,
        imagenUrl = GESTION_ECON_FIN_EMPRESA,
        trackNumber = 2
    ),
    Tema(
        temaId = TemaId(11),
        nombreTema = "Tema 11",
        descripcionTema = "descripcion11",
        duracionAudio = 220.toDuration(DurationUnit.SECONDS),
        audioUrl = URL_AUDIO,
        imagenUrl = LOG_ALMACENAMIENTO,
        trackNumber = 1,
        terminado = true
    ),
    Tema(
        temaId = TemaId(12),
        nombreTema = "Tema 12",
        descripcionTema = "descripcion12",
        duracionAudio = 230.toDuration(DurationUnit.SECONDS),
        audioUrl = URL_AUDIO,
        imagenUrl = GEST_ADMINISTRATIVA_COMERCIO_INTERNACIONAL,
        trackNumber = 1
    ),
    Tema(
        temaId = TemaId(13),
        nombreTema = "Tema 13",
        descripcionTema = "descripcion13",
        duracionAudio = 240.toDuration(DurationUnit.SECONDS),
        audioUrl = URL_AUDIO,
        imagenUrl = DESARROLLO_INTERFACES,
        trackNumber = 1
    ),
    Tema(
        temaId = TemaId(14),
        nombreTema = "Tema 14",
        descripcionTema = "descripcion14",
        duracionAudio = 250.toDuration(DurationUnit.SECONDS),
        audioUrl = URL_AUDIO,
        imagenUrl = PROG_MULTI_DISPO_MOVILES,
        trackNumber = 1
    ),
    Tema(
        temaId = TemaId(15),
        nombreTema = "Tema 15",
        descripcionTema = "descripcion15",
        duracionAudio = 260.toDuration(DurationUnit.SECONDS),
        audioUrl = URL_AUDIO,
        imagenUrl = PROG_SERVICIOS_PROCESOS,
        trackNumber = 1
    ),
    Tema(
        temaId = TemaId(16),
        nombreTema = "Tema 16",
        descripcionTema = "descripcion16",
        duracionAudio = 270.toDuration(DurationUnit.SECONDS),
        audioUrl = URL_AUDIO,
        imagenUrl = PROG_SERVICIOS_PROCESOS,
        trackNumber = 2
    ),
    Tema(
        temaId = TemaId(17),
        nombreTema = "Tema 17",
        descripcionTema = "descripcion17",
        duracionAudio = 280.toDuration(DurationUnit.SECONDS),
        audioUrl = URL_AUDIO,
        imagenUrl = ANATOMOFISIOLOGIA,
        trackNumber = 1
    ),
    Tema(
        temaId = TemaId(18),
        nombreTema = "Tema 18",
        descripcionTema = "descripcion18",
        duracionAudio = 290.toDuration(DurationUnit.SECONDS),
        audioUrl = URL_AUDIO,
        imagenUrl = DISP_PROD_FARMACEUTICOS,
        trackNumber = 1
    ),
    Tema(
        temaId = TemaId(19),
        nombreTema = "Tema 19",
        descripcionTema = "descripcion19",
        duracionAudio = 300.toDuration(DurationUnit.SECONDS),
        audioUrl = URL_AUDIO,
        imagenUrl = DISPO_VENTA_PRODUCTOS,
        trackNumber = 1
    ),
    Tema(
        temaId = TemaId(20),
        nombreTema = "Tema 20",
        descripcionTema = "descripcion20",
        duracionAudio = 310.toDuration(DurationUnit.SECONDS),
        audioUrl = URL_AUDIO,
        imagenUrl = DISPO_VENTA_PRODUCTOS,
        trackNumber = 2
    ),
    Tema(
        temaId = TemaId(31),
        nombreTema = "Tema 31",
        descripcionTema = "descripcion31",
        duracionAudio = 320.toDuration(DurationUnit.SECONDS),
        audioUrl = URL_AUDIO,
        imagenUrl = EMPRESA_INICIATIVA_EMPRENDEDORA,
        trackNumber = 1,
        terminado = true
    ),
    Tema(
        temaId = TemaId(32),
        nombreTema = "Tema 32",
        descripcionTema = "descripcion32",
        duracionAudio = 330.toDuration(DurationUnit.SECONDS),
        audioUrl = URL_AUDIO,
        imagenUrl = EMPRESA_INICIATIVA_EMPRENDEDORA,
        trackNumber = 2,
        terminado = true
    ),
    Tema(
        temaId = TemaId(33),
        nombreTema = "Tema 33",
        descripcionTema = "descripcion33",
        duracionAudio = 340.toDuration(DurationUnit.SECONDS),
        audioUrl = URL_AUDIO,
        imagenUrl = EMPRESA_INICIATIVA_EMPRENDEDORA,
        trackNumber = 3
    ),
    Tema(
        temaId = TemaId(34),
        nombreTema = "Tema 34",
        descripcionTema = "descripcion34",
        duracionAudio = 350.toDuration(DurationUnit.SECONDS),
        audioUrl = URL_AUDIO,
        imagenUrl = EMPRESA_INICIATIVA_EMPRENDEDORA,
        trackNumber = 4
    )
)

val PreviewTemasFOL = listOf(
    Tema(
        temaId = TemaId(21),
        nombreTema = "Tema 1 FOL",
        descripcionTema = "descripcion1 FOL",
        duracionAudio = 120.toDuration(DurationUnit.SECONDS),
        audioUrl = URL_AUDIO,
        imagenUrl = FOL,
        trackNumber = 1
    ),
    Tema(
        temaId = TemaId(22),
        nombreTema = "Tema 2 FOL",
        descripcionTema = "descripcion2 FOL",
        duracionAudio = 130.toDuration(DurationUnit.SECONDS),
        audioUrl = URL_AUDIO,
        imagenUrl = FOL,
        trackNumber = 2
    ),
    Tema(
        temaId = TemaId(23),
        nombreTema = "Tema 3 FOL",
        descripcionTema = "descripcion3 FOL",
        duracionAudio = 140.toDuration(DurationUnit.SECONDS),
        audioUrl = URL_AUDIO,
        imagenUrl = FOL,
        trackNumber = 3,
        terminado = true
    ),
    Tema(
        temaId = TemaId(24),
        nombreTema = "Tema 4 FOL",
        descripcionTema = "descripcion4 FOL",
        duracionAudio = 150.toDuration(DurationUnit.SECONDS),
        audioUrl = URL_AUDIO,
        imagenUrl = FOL,
        trackNumber = 4
    ),
    Tema(
        temaId = TemaId(25),
        nombreTema = "Tema 5 FOL",
        descripcionTema = "descripcion5 FOL",
        duracionAudio = 160.toDuration(DurationUnit.SECONDS),
        audioUrl = URL_AUDIO,
        imagenUrl = FOL,
        trackNumber = 5
    ),
)

val PreviewAsignaturas = listOf(
    Asignatura(AsignaturaId(1), "Comunicacion y Atencion al Cliente", PreviewTemas.filter{it.temaId.id <= 3}, COMUNICACION_ATENCION_CLIENTE),
    Asignatura(AsignaturaId(2), "Gestion de la Documentacion Juridica y Empresarial", PreviewTemas.filter{it.temaId.id in 4..5}, GESTION_DOCUMENTACION_JURIDICA_EMPRESARIAL),
    Asignatura(AsignaturaId(3), "Ofimática y Proceso de Información", PreviewTemas.filter{it.temaId.id in 6..8}, OFIMATICA_PROCESO_INFORMACION),
    Asignatura(AsignaturaId(4), "Gestión Económica y Financiera de la Empresa", PreviewTemas.filter{it.temaId.id in 9..10}, GESTION_ECON_FIN_EMPRESA),
    Asignatura(AsignaturaId(5), "Logística de Almacenamiento", PreviewTemas.filter{it.temaId.id in 11..11}, LOG_ALMACENAMIENTO),
    Asignatura(AsignaturaId(6), "Gestión Administrativa del comercio internacional", PreviewTemas.filter{it.temaId.id in 12..12}, GEST_ADMINISTRATIVA_COMERCIO_INTERNACIONAL),
    Asignatura(AsignaturaId(7), "Desarrollo de Interfaces", PreviewTemas.filter{it.temaId.id in 13..13}, DESARROLLO_INTERFACES),
    Asignatura(AsignaturaId(8), "Programación Multimedia y Dispositivos Móviles", PreviewTemas.filter{it.temaId.id in 14..14}, PROG_MULTI_DISPO_MOVILES),
    Asignatura(AsignaturaId(9), "Programación de Servicios y procesos", PreviewTemas.filter{it.temaId.id in 15..16}, PROG_SERVICIOS_PROCESOS),
    Asignatura(AsignaturaId(10), "Anatomofisiología y Patología Básicas", PreviewTemas.filter{it.temaId.id in 17..17}, ANATOMOFISIOLOGIA),
    Asignatura(AsignaturaId(11), "Dispensación de productos farmaceúticos", PreviewTemas.filter{it.temaId.id in 18..18}, DISP_PROD_FARMACEUTICOS),
    Asignatura(AsignaturaId(12), "Disposición y Venta de Productos", PreviewTemas.filter{it.temaId.id in 19..20}, DISPO_VENTA_PRODUCTOS),
    Asignatura(AsignaturaId(98), "Formación y Orientación Laboral", PreviewTemasFOL, FOL),
    Asignatura(AsignaturaId(99), "Empresa e iniciativa emprendedora", PreviewTemas.filter{it.temaId.id in 31..34}, EMPRESA_INICIATIVA_EMPRENDEDORA),
)

val PreviewGrados = listOf(
    Grado(GradoId(1), "Administracion Y Finanzas", PreviewAsignaturas.map { it.asignaturaId }.filter { it.id in 1..3 || it.id == 98 || it.id == 99 }, "https://dl.dropboxusercontent.com/scl/fi/w64grl0dlwmarv2kk240x/Escudos_Adminsitracion.png?rlkey=rtpqrknxk1h3zmmf65ugvc8h6&st=0hlcrblf&dl=0"),
    Grado(GradoId(2), "Comercio Internacional", PreviewAsignaturas.map { it.asignaturaId }.filter { it.id in 4..6 || it.id == 98 || it.id == 99}, "https://dl.dropboxusercontent.com/scl/fi/bjoq446e8ljf1qs2xl54f/Escudos_ComercioyMarketing.png?rlkey=k4jpt72nq1sf8unoyhvyn3jwe&st=e32rnj5r&dl=0"),
    Grado(GradoId(3), "Desarrollo De Aplicaciones Multiplataforma", PreviewAsignaturas.map { it.asignaturaId }.filter { it.id in 7..9 || it.id == 98}, "https://dl.dropboxusercontent.com/scl/fi/ivc5xvtx5t9tso1d6uk44/Escudos_Informatica.png?rlkey=se3g490crwv6xeo216zquolg9&st=oiguiovv&dl=0"),
    Grado(GradoId(4), "Farmacia Y Parafarmacia", PreviewAsignaturas.map { it.asignaturaId }.filter { it.id in 10..12 || it.id == 98 || it.id == 99}, "https://dl.dropboxusercontent.com/scl/fi/mvorog10plfjc74qtwogu/Escudos_Sanidad.png?rlkey=lbk250fva668rf0baf7c9soan&st=6urb11yy&dl=0"),
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