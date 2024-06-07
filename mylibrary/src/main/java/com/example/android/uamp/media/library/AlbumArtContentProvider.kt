/*
 * Copyright 2019 Google Inc. All rights reserved.
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

package com.example.android.uamp.media.library

import android.content.ContentProvider
import android.content.ContentResolver
import android.content.ContentValues
import android.database.Cursor
import android.net.Uri
import android.os.ParcelFileDescriptor
import com.bumptech.glide.Glide
import java.io.File
import java.io.FileNotFoundException
import java.util.concurrent.TimeUnit

// El tiempo de espera para que se descargue el archivo de arte del álbum antes de que se agote el tiempo.
const val DOWNLOAD_TIMEOUT_SECONDS = 30L

/**
 * Clase AlbumArtContentProvider.
 * Esta clase es un proveedor de contenido que se utiliza para descargar y proporcionar arte de álbumes.
 */
internal class AlbumArtContentProvider : ContentProvider() {
    /**
     * Objeto complementario que contiene un mapa de URIs para mapear URIs remotas a URIs de contenido.
     */
    companion object {
        private val uriMap = mutableMapOf<Uri, Uri>()

        /**
         * Función para mapear una URI a una URI de contenido.
         */
        fun mapUri(uri: Uri): Uri {
            val path = uri.encodedPath?.substring(1)?.replace('/', ':') ?: return Uri.EMPTY
            val contentUri = Uri.Builder()
                .scheme(ContentResolver.SCHEME_CONTENT)
                .authority("com.example.android.uamp")
                .path(path)
                .build()
            uriMap[contentUri] = uri
            return contentUri
        }
    }

    /**
     * Función onCreate que se llama cuando se crea el proveedor de contenido.
     */
    override fun onCreate() = true

    /**
     * Función para abrir un archivo en el proveedor de contenido.
     */
    override fun openFile(uri: Uri, mode: String): ParcelFileDescriptor? {
        val context = this.context ?: return null

        var file = File(context.cacheDir, uri.path!!)
        if (!file.exists()) {
            val remoteUri = uriMap[uri] ?: throw FileNotFoundException(uri.path)
            // Use Glide to download the album art.
            val cacheFile = Glide.with(context)
                .asFile()
                .load(remoteUri)
                .submit()
                .get(DOWNLOAD_TIMEOUT_SECONDS, TimeUnit.SECONDS)

            // Rename the file Glide created to match our own scheme.
            cacheFile.renameTo(file)

            file = cacheFile
        }
        return ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY)
    }

    /**
     * Función para insertar un valor en el proveedor de contenido.
     */
    override fun insert(uri: Uri, values: ContentValues?): Uri? = null

    /**
     * Función para consultar el proveedor de contenido.
     */
    override fun query(
        uri: Uri,
        projection: Array<String>?,
        selection: String?,
        selectionArgs: Array<String>?,
        sortOrder: String?
    ): Cursor? = null

    /**
     * Función para actualizar un valor en el proveedor de contenido.
     */
    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<String>?
    ) = 0

    /**
     * Función para eliminar un valor del proveedor de contenido.
     */
    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?) = 0

    /**
     * Función para obtener el tipo de un valor en el proveedor de contenido.
     */
    override fun getType(uri: Uri): String? = null

}
