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

package de.kuschku.libquassel.util.compatibility

import de.kuschku.libquassel.util.compatibility.backport.BackportedSyncFlushDeflaterOutputStream
import de.kuschku.libquassel.util.compatibility.backport.ProperlyClosingSyncFlushDeflaterOutputStream
import java.io.OutputStream
import java.util.zip.DeflaterOutputStream

object CompatibilityUtils {
  var supportsKeepAlive = true
  var supportsCompression = true

  /**
   * Creates a SyncFlush output stream, even if the current device does not support doing so
   * natively.
   *
   * @param rawOut the raw output stream to be wrapped
   * @return The wrapping output stream
   */
  fun createDeflaterOutputStream(rawOut: OutputStream?): DeflaterOutputStream {
    return if (supportsCompression) {
      ProperlyClosingSyncFlushDeflaterOutputStream(rawOut)
    } else {
      BackportedSyncFlushDeflaterOutputStream(rawOut)
    }
  }
}
