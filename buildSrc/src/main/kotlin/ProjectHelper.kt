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

import org.gradle.api.Project
import java.io.ByteArrayOutputStream
import java.util.*

fun Project.cmd(vararg command: String) = try {
  val stdOut = ByteArrayOutputStream()
  exec {
    commandLine(*command)
    standardOutput = stdOut
  }
  stdOut.toString(Charsets.UTF_8.name()).trim()
} catch (e: Throwable) {
  e.printStackTrace()
  null
}

fun Project.properties(fileName: String): Properties? {
  val file = file(fileName)
  if (!file.exists())
    return null
  val props = Properties()
  props.load(file.inputStream())
  return props
}

data class SigningData(
  val storeFile: String,
  val storePassword: String,
  val keyAlias: String,
  val keyPassword: String
) {
  companion object {
    fun of(properties: Properties?): SigningData? {
      if (properties == null) return null

      val storeFile = properties.getProperty("storeFile") ?: return null
      val storePassword = properties.getProperty("storePassword") ?: return null
      val keyAlias = properties.getProperty("keyAlias") ?: return null
      val keyPassword = properties.getProperty("keyPassword") ?: return null

      return SigningData(storeFile, storePassword, keyAlias, keyPassword)
    }
  }
}
