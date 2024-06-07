/*
 * Copyright 2018 Google Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.uamp.media

import android.Manifest.permission.MEDIA_CONTENT_CONTROL
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageInfo.REQUESTED_PERMISSION_GRANTED
import android.content.pm.PackageManager
import android.content.res.XmlResourceParser
import android.os.Process
import android.support.v4.media.session.MediaSessionCompat
import android.util.Base64
import android.util.Log
import androidx.annotation.XmlRes
import androidx.core.app.NotificationManagerCompat
import androidx.media.MediaBrowserServiceCompat
import org.xmlpull.v1.XmlPullParserException
import java.io.IOException
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.Locale

/**
 * Valida que el paquete que llama esté autorizado para navegar en un [MediaBrowserServiceCompat].
 *
 * La lista de certificados de firma permitidos y sus nombres de paquete correspondientes se define en
 * res/xml/allowed_media_browser_callers.xml.
 *
 * Si deseas agregar un nuevo llamador a allowed_media_browser_callers.xml y no conoces
 * su firma, esta clase imprimirá en logcat (nivel INFO) un mensaje con las etiquetas XML adecuadas
 * para permitir al llamador.
 *
 * Para más información, consulta res/xml/allowed_media_browser_callers.xml.
 */
internal class PackageValidator(context: Context, @XmlRes xmlResId: Int) {
    private val context: Context
    private val packageManager: PackageManager

    private val certificateAllowList: Map<String, KnownCallerInfo>
    private val platformSignature: String

    private val callerChecked = mutableMapOf<String, Pair<Int, Boolean>>()

    init {
        val parser = context.resources.getXml(xmlResId)
        this.context = context.applicationContext
        this.packageManager = this.context.packageManager

        certificateAllowList = buildCertificateAllowList(parser)
        platformSignature = getSystemSignature()
    }

    /**
     * Verifica si el llamador que intenta conectarse a un [MediaBrowserServiceCompat] es conocido.
     * Consulta [MusicService.onGetRoot] para ver dónde se utiliza esto.
     *
     * @param callingPackage El nombre del paquete del llamador.
     * @param callingUid El ID de usuario del llamador.
     * @return `true` si el llamador es conocido, `false` en caso contrario.
     */
    fun isKnownCaller(callingPackage: String, callingUid: Int): Boolean {
        // Si el llamador ya ha sido verificado, devuelve el resultado anterior aquí.
        val (checkedUid, checkResult) = callerChecked[callingPackage] ?: Pair(0, false)
        if (checkedUid == callingUid) {
            return checkResult
        }

        // Construir la información del llamador para el resto de las verificaciones aquí.
        val callerPackageInfo = buildCallerInfo(callingPackage)
            ?: throw IllegalStateException("Caller wasn't found in the system?")

        // Verificar que las cosas no estén rotas. (Esta prueba siempre debe pasar).
        if (callerPackageInfo.uid != callingUid) {
            throw IllegalStateException("Caller's package UID doesn't match caller's actual UID?")
        }

        val callerSignature = callerPackageInfo.signature
        val isPackageInAllowList = certificateAllowList[callingPackage]?.signatures?.first {
            it.signature == callerSignature
        } != null

        val isCallerKnown = when {
            // Si es nuestra propia aplicación haciendo la llamada, permítelo.
            callingUid == Process.myUid() -> true
            // Si es una de las aplicaciones en la lista permitida, permítelo.
            isPackageInAllowList -> true
            // Si el sistema está haciendo la llamada, permítelo.
            callingUid == Process.SYSTEM_UID -> true
            // Si la aplicación fue firmada con el mismo certificado que la plataforma en sí, también permítelo.
            callerSignature == platformSignature -> true
            // Permitir si la aplicación tiene el permiso MEDIA_CONTENT_CONTROL.
            callerPackageInfo.permissions.contains(MEDIA_CONTENT_CONTROL) -> true
            // Permitir si la aplicación tiene un listener de notificaciones.
            NotificationManagerCompat.getEnabledListenerPackages(this.context)
                .contains(callerPackageInfo.packageName) -> true

            // Si ninguna de las verificaciones anteriores tuvo éxito, entonces el llamador no es reconocido.
            else -> false
        }

        if (!isCallerKnown) {
            logUnknownCaller(callerPackageInfo)
        }

        // Guardar el resultado para la próxima vez.
        callerChecked[callingPackage] = Pair(callingUid, isCallerKnown)
        return isCallerKnown
    }

    /**
     * Registra un mensaje a nivel de información con detalles sobre cómo agregar un llamador a la lista de llamadores permitidos
     * cuando la aplicación es depurable.
     */
    private fun logUnknownCaller(callerPackageInfo: CallerPackageInfo) {
        if (callerPackageInfo.signature != null) {
            val formattedLog =
                context.getString(
                    R.string.allowed_caller_log,
                    callerPackageInfo.name,
                    callerPackageInfo.packageName,
                    callerPackageInfo.signature
                )
            Log.i(TAG, formattedLog)
        }
    }

    /**
     * Construye un [CallerPackageInfo] para un paquete dado que puede ser usado para todas las
     * varias verificaciones que se realizan antes de permitir que una aplicación se conecte a un
     * [MediaBrowserServiceCompat].
     */
    private fun buildCallerInfo(callingPackage: String): CallerPackageInfo? {
        val packageInfo = getPackageInfo(callingPackage) ?: return null

        val appName = packageInfo.applicationInfo.loadLabel(packageManager).toString()
        val uid = packageInfo.applicationInfo.uid
        val signature = getSignature(packageInfo)

        val requestedPermissions = packageInfo.requestedPermissions
        val permissionFlags = packageInfo.requestedPermissionsFlags
        val activePermissions = mutableSetOf<String>()
        requestedPermissions?.forEachIndexed { index, permission ->
            if (permissionFlags[index] and REQUESTED_PERMISSION_GRANTED != 0) {
                activePermissions += permission
            }
        }

        return CallerPackageInfo(appName, callingPackage, uid, signature, activePermissions.toSet())
    }

    /**
     * Busca el [PackageInfo] para un nombre de paquete.
     * Esto solicita tanto las firmas (para verificar si una aplicación está en la lista permitida) como
     * los permisos de la aplicación, lo que permite más flexibilidad en la lista permitida.
     *
     * @return [PackageInfo] para el nombre del paquete o null si no se encuentra.
     */
    @Suppress("deprecation")
    @SuppressLint("PackageManagerGetSignatures")
    private fun getPackageInfo(callingPackage: String): PackageInfo? =
        packageManager.getPackageInfo(
            callingPackage,
            PackageManager.GET_SIGNATURES or PackageManager.GET_PERMISSIONS
        )

    /**
     * Obtiene la firma del [PackageInfo] de un paquete dado.
     *
     * La "firma" es un hash SHA-256 de la clave pública del certificado de firma utilizado por
     * la aplicación.
     *
     * Si la aplicación no se encuentra, o si la aplicación no tiene exactamente una firma, este método
     * devuelve `null` como la firma.
     */
    @Suppress("deprecation")
    private fun getSignature(packageInfo: PackageInfo): String? =
        if (packageInfo.signatures == null || packageInfo.signatures.size != 1) {
            // Las mejores prácticas de seguridad dictan que una aplicación debe estar firmada con exactamente una (1)
            // firma. Debido a esto, si hay múltiples firmas, recházalo.
            null
        } else {
            val certificate = packageInfo.signatures[0].toByteArray()
            getSignatureSha256(certificate)
        }

    private fun buildCertificateAllowList(parser: XmlResourceParser): Map<String, KnownCallerInfo> {
        val certificateAllowList = LinkedHashMap<String, KnownCallerInfo>()
        try {
            var eventType = parser.next()
            while (eventType != XmlResourceParser.END_DOCUMENT) {
                if (eventType == XmlResourceParser.START_TAG) {
                    val callerInfo = when (parser.name) {
                        "signing_certificate" -> parseV1Tag(parser)
                        "signature" -> parseV2Tag(parser)
                        else -> null
                    }

                    callerInfo?.let { info ->
                        val packageName = info.packageName
                        val existingCallerInfo = certificateAllowList[packageName]
                        if (existingCallerInfo != null) {
                            existingCallerInfo.signatures += callerInfo.signatures
                        } else {
                            certificateAllowList[packageName] = callerInfo
                        }
                    }
                }

                eventType = parser.next()
            }
        } catch (xmlException: XmlPullParserException) {
            Log.e(TAG, "No se pudo leer los llamadores permitidos desde XML.", xmlException)
        } catch (ioException: IOException) {
            Log.e(TAG, "No se pudo leer los llamadores permitidos desde XML.", ioException)
        }

        return certificateAllowList
    }

    /**
     * Analiza una etiqueta de formato v1. Consulta allowed_media_browser_callers.xml para más detalles.
     */
    private fun parseV1Tag(parser: XmlResourceParser): KnownCallerInfo {
        val name = parser.getAttributeValue(null, "name")
        val packageName = parser.getAttributeValue(null, "package")
        val isRelease = parser.getAttributeBooleanValue(null, "release", false)
        val certificate = parser.nextText().replace(WHITESPACE_REGEX, "")
        val signature = getSignatureSha256(certificate)

        val callerSignature = KnownSignature(signature, isRelease)
        return KnownCallerInfo(name, packageName, mutableSetOf(callerSignature))
    }

    /**
     * Analiza una etiqueta de formato v2. Consulta allowed_media_browser_callers.xml para más detalles.
     */
    private fun parseV2Tag(parser: XmlResourceParser): KnownCallerInfo {
        val name = parser.getAttributeValue(null, "name")
        val packageName = parser.getAttributeValue(null, "package")

        val callerSignatures = mutableSetOf<KnownSignature>()
        var eventType = parser.next()
        while (eventType != XmlResourceParser.END_TAG) {
            val isRelease = parser.getAttributeBooleanValue(null, "release", false)
            val signature =
                parser.nextText().replace(WHITESPACE_REGEX, "").lowercase(Locale.getDefault())
            callerSignatures += KnownSignature(signature, isRelease)

            eventType = parser.next()
        }

        return KnownCallerInfo(name, packageName, callerSignatures)
    }

    /**
     * Encuentra la firma de la clave de firma de la plataforma Android. Esta clave nunca es nula.
     */
    private fun getSystemSignature(): String =
        getPackageInfo(ANDROID_PLATFORM)?.let { platformInfo ->
            getSignature(platformInfo)
        } ?: throw IllegalStateException("Firma de la plataforma no encontrada")

    /**
     * Crea una firma SHA-256 dada una certificación codificada en Base64.
     */
    private fun getSignatureSha256(certificate: String): String {
        return getSignatureSha256(Base64.decode(certificate, Base64.DEFAULT))
    }

    /**
     * Crea una firma SHA-256 dada una matriz de bytes de certificado.
     */
    private fun getSignatureSha256(certificate: ByteArray): String {
        val md: MessageDigest
        try {
            md = MessageDigest.getInstance("SHA256")
        } catch (noSuchAlgorithmException: NoSuchAlgorithmException) {
            Log.e(TAG, "No such algorithm: $noSuchAlgorithmException")
            throw RuntimeException("No se pudo encontrar el algoritmo de hash SHA256", noSuchAlgorithmException)
        }
        md.update(certificate)

        // Este código toma la matriz de bytes generada por `md.digest()` y une cada uno de los bytes
        // a una cadena, aplicando el formato de cadena `%02x` en cada dígito antes de que se agregue, con
        // dos puntos (':') entre cada uno de los elementos.
        // Por ejemplo: input=[0,2,4,6,8,10,12], output="00:02:04:06:08:0a:0c"
        return md.digest().joinToString(":") { String.format("%02x", it) }
    }

    private data class KnownCallerInfo(
        internal val name: String,
        internal val packageName: String,
        internal val signatures: MutableSet<KnownSignature>
    )

    private data class KnownSignature(
        internal val signature: String,
        internal val release: Boolean
    )

    /**
     * Clase de conveniencia para mantener toda la información sobre una aplicación que se está verificando
     * para ver si es un llamador conocido.
     */
    private data class CallerPackageInfo(
        internal val name: String,
        internal val packageName: String,
        internal val uid: Int,
        internal val signature: String?,
        internal val permissions: Set<String>
    )
}

private const val TAG = "PackageValidator"
private const val ANDROID_PLATFORM = "android"
private val WHITESPACE_REGEX = "\\s|\\n".toRegex()
