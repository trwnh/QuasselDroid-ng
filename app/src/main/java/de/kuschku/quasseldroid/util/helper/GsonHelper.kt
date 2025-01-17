/*
 * Quasseldroid - Quassel client for Android
 *
 * Copyright (c) 2019 Janne Mareike Koschinski
 * Copyright (c) 2019 The Quassel Project
 *
 * This program is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 3 as published
 * by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package de.kuschku.quasseldroid.util.helper

import com.google.gson.Gson
import java.io.File
import java.io.Reader

inline fun <reified T> Gson.fromJson(file: File): T = this.fromJson(file.reader(), T::class.java)
inline fun <reified T> Gson.fromJson(text: String): T = this.fromJson(text, T::class.java)
inline fun <reified T> Gson.fromJson(reader: Reader): T = this.fromJson(reader, T::class.java)
