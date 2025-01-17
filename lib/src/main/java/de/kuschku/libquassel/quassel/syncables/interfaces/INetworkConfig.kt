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

package de.kuschku.libquassel.quassel.syncables.interfaces

import de.kuschku.libquassel.annotations.Slot
import de.kuschku.libquassel.annotations.Syncable
import de.kuschku.libquassel.protocol.ARG
import de.kuschku.libquassel.protocol.QVariantMap
import de.kuschku.libquassel.protocol.Type

@Syncable(name = "NetworkConfig")
interface INetworkConfig : ISyncableObject {

  fun initProperties(): QVariantMap
  fun initSetProperties(properties: QVariantMap)

  @Slot
  fun requestSetAutoWhoDelay(i: Int) {
    REQUEST("requestSetAutoWhoDelay", ARG(i, Type.Int))
  }

  @Slot
  fun requestSetAutoWhoEnabled(b: Boolean) {
    REQUEST("requestSetAutoWhoEnabled", ARG(b, Type.Bool))
  }

  @Slot
  fun requestSetAutoWhoInterval(i: Int) {
    REQUEST("requestSetAutoWhoInterval", ARG(i, Type.Int))
  }

  @Slot
  fun requestSetAutoWhoNickLimit(i: Int) {
    REQUEST("requestSetAutoWhoNickLimit", ARG(i, Type.Int))
  }

  @Slot
  fun requestSetMaxPingCount(i: Int) {
    REQUEST("requestSetMaxPingCount", ARG(i, Type.Int))
  }

  @Slot
  fun requestSetPingInterval(i: Int) {
    REQUEST("requestSetPingInterval", ARG(i, Type.Int))
  }

  @Slot
  fun requestSetPingTimeoutEnabled(b: Boolean) {
    REQUEST("requestSetPingTimeoutEnabled", ARG(b, Type.Bool))
  }

  @Slot
  fun requestSetStandardCtcp(b: Boolean) {
    REQUEST("requestSetStandardCtcp", ARG(b, Type.Bool))
  }

  @Slot
  fun setAutoWhoDelay(delay: Int) {
    SYNC("setAutoWhoDelay", ARG(delay, Type.Int))
  }

  @Slot
  fun setAutoWhoEnabled(enabled: Boolean) {
    SYNC("setAutoWhoEnabled", ARG(enabled, Type.Bool))
  }

  @Slot
  fun setAutoWhoInterval(interval: Int) {
    SYNC("setAutoWhoInterval", ARG(interval, Type.Int))
  }

  @Slot
  fun setAutoWhoNickLimit(limit: Int) {
    SYNC("setAutoWhoNickLimit", ARG(limit, Type.Int))
  }

  @Slot
  fun setMaxPingCount(count: Int) {
    SYNC("setMaxPingCount", ARG(count, Type.Int))
  }

  @Slot
  fun setPingInterval(interval: Int) {
    SYNC("setPingInterval", ARG(interval, Type.Int))
  }

  @Slot
  fun setPingTimeoutEnabled(enabled: Boolean) {
    SYNC("setPingTimeoutEnabled", ARG(enabled, Type.Bool))
  }

  @Slot
  fun setStandardCtcp(standardCtcp: Boolean) {
    SYNC("setStandardCtcp", ARG(standardCtcp, Type.Bool))
  }

  @Slot
  override fun update(properties: QVariantMap) {
    super.update(properties)
  }
}
