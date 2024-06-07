/*
 * Copyright 2022 Google Inc. All rights reserved.
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

package com.example.android.uamp.media.extensions

import androidx.media3.common.Player
/**
 * Esta extensión de la clase Player proporciona una propiedad que indica si el comando de reproducción está habilitado.
 * Devuelve verdadero si el comando de reproducción está disponible y el reproductor no está actualmente en reproducción.
 */
inline val Player.isPlayEnabled
    get() = (availableCommands.contains(Player.COMMAND_PLAY_PAUSE)) &&
            (!playWhenReady)
/**
 * Esta extensión de la clase Player proporciona una propiedad que indica si la reproducción ha terminado.
 * Devuelve verdadero si el estado de reproducción es igual a STATE_ENDED.
 */
inline val Player.isEnded
    get() = playbackState == Player.STATE_ENDED
